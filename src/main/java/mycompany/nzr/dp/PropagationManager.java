package mycompany.nzr.dp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mycompany.nzr.common.BatchDetailStatus;
import mycompany.nzr.common.BatchStatus;
import mycompany.nzr.common.BatchType;
import mycompany.nzr.common.ConfiguredSystem;
import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.common.Output;
import mycompany.nzr.dc.ddlfactory.DdlFactory;
import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
import mycompany.nzr.dc.dto.CreateTableDTO;
import mycompany.nzr.dp.dao.RepoDAO;
import mycompany.nzr.dp.dao.TargetDAO;
import mycompany.nzr.dp.dto.CapturedFile;
import mycompany.nzr.dp.dto.PropagatableBatch;
import mycompany.nzr.dp.sqlbuilder.PropagationSqlBuilder;
import mycompany.nzr.dp.sqlbuilder.RepoCommonSqlBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class PropagationManager {

	@Autowired
	private RepoCommonSqlBuilder repoSqlBuilder;

	@Autowired
	private PropagationSqlBuilder propSqlBuilder;

	@Autowired
	private RepoDAO repoDAO;

	@Autowired
	private DdlFactory ddlFactory;

	@Autowired
	private TargetDAO targetDAO;

	private String logDirectory;

	private Logger logger = Logger.getLogger(PropagationManager.class);

	private Map<Long, ConfiguredSystem> targetSystems = new HashMap<Long, ConfiguredSystem>();

	public void propagate() throws PropagationException {
		logger.info("Preparing to read target systems for propagatable batches");
		List<ConfiguredSystem> targetSystemsWithPropagatableBatches = this.repoDAO
				.getTargetSystemsWithPropagatableBatches(repoSqlBuilder
						.getTargetSystemsWithPropagatableBatchesSql());

		logger.info("Found: " + targetSystemsWithPropagatableBatches.size()
				+ " target system(s) to propagate to");

		for (ConfiguredSystem system_ : targetSystemsWithPropagatableBatches) {
			ConfiguredSystem system = null;

			synchronized (this) {
				if (targetSystems.containsKey(system_.getHostSkey())) {
					system = targetSystems.get(system_.getHostSkey());
				} else {
					system = system_;
					targetSystems.put(system_.getHostSkey(), system_);
				}
			}

			synchronized (system) {
				logger.info("Starting propagation for System: "
						+ system.getHostSkey());
				targetDAO.createAndStoreTemplate(system);

				List<PropagatableBatch> propBatches = repoDAO
						.getBatchToPropagate(
								repoSqlBuilder.getBatchToPropagateSql(),
								system.getHostSkey(), system.getDatabaseName());
				logger.info("Found " + propBatches.size()
						+ " propagatable batches for the system");

				String endBatchSql = repoSqlBuilder.getEndBatchSql();

				for (PropagatableBatch dcBatch : propBatches) {
					long dpBatchSkey = repoDAO.getNextvalFromSeq(repoSqlBuilder
							.getBatchPkValueSql());
					try {
						repoDAO.startBatch(repoSqlBuilder.getStartBatchSql(),
								dpBatchSkey, dcBatch.getSubscriptionSkey(),
								dcBatch.getDcSkey(), dcBatch.getDcBatchSkey(),
								BatchStatus.RUNNING,
								BatchType.valueOf(dcBatch.getDcBatchType()));
						logger.info("Started propagation batch " + dpBatchSkey);
						List<CapturedFile> batchFiles = repoDAO
								.getBatchFilesToPropagate(repoSqlBuilder
										.getBatchFilesToPropagateSql(), dcBatch
										.getDcBatchSkey());
						if (batchFiles != null && !batchFiles.isEmpty()) {
							doPropagate(dcBatch, dpBatchSkey, batchFiles,
									logDirectory, system.getHostSkey());
						} else {
							logger.info("The DC batch"
									+ dcBatch.getDcBatchSkey() + "was empty");
						}

						repoDAO.endBatch(endBatchSql, BatchStatus.COMPLETED,
								dpBatchSkey);
						logger.info("Propagation batch " + dpBatchSkey
								+ " ended");
					} catch (Exception e) {
						repoDAO.endBatch(endBatchSql, BatchStatus.ERROR,
								dpBatchSkey);
						throw new PropagationException(e.getMessage());
					}
				}
			}
		}
	}

	private void createBatchDetailEntries(PropagatableBatch dcBatch,
			long dpBatchSkey, List<CapturedFile> batchFiles) {
		String startTableSql = this.repoSqlBuilder.getStartBatchTableSql();
		for (CapturedFile file : batchFiles) {
			long dpBatchDtlSkey = repoDAO
					.getNextvalFromSeq("SELECT NZR_DP_BATCH_DTL_SEQ.nextval FROM dual");
			file.setDpBatchDtlSkey(dpBatchDtlSkey);
			// SrcObjectId needed!?
			repoDAO.startBatchTable(startTableSql, dpBatchDtlSkey, dpBatchSkey,
					dcBatch.getSubscriptionSkey(), dcBatch.getDcBatchSkey(),
					file.getDcBatchDtlSkey(), -1, file.getTableSkey(),
					BatchDetailStatus.RUNNING, Action.JUST_STARTED.getCode());
		}
	}

	private void doPropagate(PropagatableBatch dcBatch, long dpBatchSkey,
			List<CapturedFile> batchFiles, String logDirectory, long hostSkey)
			throws Exception {

		// TODO - verify that this logic is fine. Should an exception instead be
		// thrown to cause ERROR status.
		// Problem with ERROR - no further batches will propagate because will
		// attampt again (still propagate-able)
		logger.info("Checking if files in DC batch actually exist on the file system");
		for (CapturedFile cf : batchFiles) {

			File file = new File(cf.getFolderName() + cf.getFileName());
			if (!file.exists()) {
				logger.info("File " + file.getAbsolutePath() + file.getName()
						+ " does not exist on the file system");
				return;
			}
		}

		createBatchDetailEntries(dcBatch, dpBatchSkey, batchFiles);
		BatchType dcBatchType = BatchType.valueOf(dcBatch.getDcBatchType());
		try {
			if (dcBatchType == BatchType.I) {
				logger.info("Ready to propagate Incremental Batch");
				doPropagateIncrementalBatch(batchFiles, logDirectory, hostSkey);
			} else if (dcBatchType == BatchType.F) {
				logger.info("Ready to propagate Full Batch");
				doPropagateFullBatch(dcBatch, batchFiles, logDirectory,
						hostSkey);
			} else {
				throw new IllegalStateException("Unsupported batch type");
			}

			for (CapturedFile file : batchFiles) {
				repoDAO.endBatchTable(repoSqlBuilder.getEndBatchTableSql(),
						BatchDetailStatus.COMPLETED, Action.NONE,
						file.getDpBatchDtlSkey());
			}
		} catch (Exception e) { // TODO: what should be the final action and
								// status for all tables in a batch if only one
								// threw an exception?
			// for (CapturedFile file : batchFiles) {
			// repoDAO.endBatchTable(endTableSql, BatchDetailStatus.ERROR,
			// Action.NONE.getCode(), file.getDpBatchDtlSkey());
			// }
			throw e;
		}
	}

	private void doPropagateIncrementalBatch(List<CapturedFile> files,
			String logDir, long hostSkey) throws Exception {
		executeMiscDdl(files, hostSkey);
		logger.info("Misc DDL executed");
		executePkFkIncrementalBatchLogic(files, hostSkey);
		logger.info("PK-FK logic executed");
		executeDeletes(files, logDir, hostSkey);
		logger.info("Deletes propagated");
		executeInserts(files, logDir, hostSkey);
		logger.info("Inserts propagated");
	}

	private void doPropagateFullBatch(PropagatableBatch dcBatch,
			List<CapturedFile> files, String logDir, long hostSkey)
			throws Exception {
		executeCreateTableDdl(files, dcBatch, hostSkey);
		logger.info("Create Table logic executed");
		executeCreateConstraints(files, hostSkey);
		logger.info("Create Constraints logic executed");
		executeInserts(files, logDir, hostSkey);
		logger.info("Inserts propagated");
	}

	private void executeCreateTableDdl(List<CapturedFile> files,
			PropagatableBatch dcBatch, long hostSkey) throws Exception {
		String checkTableExists_Sql = propSqlBuilder.getCheckTableExistsSql();
		List<CapturedFile> createTableDdlsToExecute = new ArrayList<CapturedFile>();
		List<String> dropDdls = new ArrayList<String>(); // DROP TABLE ddl
		List<String> truncateTableSql = new ArrayList<String>(); // params for
																	// TRUNCATE
																	// TABLE sql
		Map<String, CapturedFile> outputFiles = subset(files, Output.DDL_TABLE);// CREATE
																				// TABLE
																				// ddl
		Set<String> tableNames = outputFiles.keySet();

		for (String tableName : tableNames) {
			CapturedFile file = outputFiles.get(tableName);
			if (targetDAO.checkTableExists(checkTableExists_Sql,
					file.getTableName(), hostSkey) == 0) { // table does not
															// exists => execute
															// CREATE TABLE
				createTableDdlsToExecute.add(file);
			} else if (dcBatch.getDropIfExists() == 1) { // table exists, drop
															// it, then create
				dropDdls.add(ddlFactory.getDropTableDDL(Arrays
						.asList(new String[] { file.getTableName() })));
				createTableDdlsToExecute.add(file);
			} else { // table exists, do not drop => truncate
				truncateTableSql.add("TRUNCATE TABLE " + file.getTableName());
			}
		}

		String dropDdls_[] = new String[dropDdls.size()];
		dropDdls.toArray(dropDdls_);
		if (dropDdls_.length > 0)
			targetDAO.executeDdlBatched(dropDdls_, hostSkey);
		if (createTableDdlsToExecute.size() > 0)
			executeCapturedDdl(createTableDdlsToExecute,
					Action.DDL_CREATE_TABLE, Output.DDL_TABLE, hostSkey);
		if (truncateTableSql.size() > 0) {
			String[] sql = new String[truncateTableSql.size()];
			truncateTableSql.toArray(sql);
			targetDAO.executeDdlBatched(sql, hostSkey);
		}

	}

	private void executeCreateConstraints(List<CapturedFile> files,
			long hostSkey) throws Exception {
		// TODO: Unique Constraints - add here.
		executeCapturedDdl(files, Action.DDL_CREATE_PK, Output.DDL_PK, hostSkey);
		executeCapturedDdl(files, Action.DDL_CREATE_FK, Output.DDL_FK, hostSkey);
	}

	private void executeMiscDdl(List<CapturedFile> files, long hostSkey)
			throws Exception {
		executeCapturedDdl(files, Action.DDL_MISC, Output.DDL_MISC, hostSkey);
	}

	private void executeCapturedDdl(List<CapturedFile> allFiles, Action action,
			Output output, long hostSkey) throws Exception {
		Map<String, CapturedFile> outputFiles = subset(allFiles, output);
		if (outputFiles == null || outputFiles.isEmpty())
			return;
		Map<String, String[]> tableDdls = readDdls(outputFiles);
		Set<Map.Entry<String, String[]>> mapentries = tableDdls.entrySet();

		for (Map.Entry<String, String[]> mapentry : mapentries) {
			String tableName = mapentry.getKey();

			repoDAO.updateBatchTable(repoSqlBuilder.getUpdateBatchTableSql(),
					0, 0, 0, action.getCode(), outputFiles.get(tableName)
							.getDpBatchDtlSkey());
			try {

				String[] ddls = mapentry.getValue();
				targetDAO.executeDdlBatched(ddls, hostSkey);
				repoDAO.updateBatchTable(repoSqlBuilder
						.getUpdateBatchTableSql(), 0, 0, 1, action.getCode(),
						outputFiles.get(tableName).getDpBatchDtlSkey());
			} catch (Exception e) {
				repoDAO.endBatchTable(repoSqlBuilder.getEndBatchTableSql(),
						BatchDetailStatus.ERROR, action,
						outputFiles.get(tableName).getDpBatchDtlSkey());
				throw e;
			}

		}
	}

	private void executePkFkIncrementalBatchLogic(List<CapturedFile> allfiles,
			long hostSkey) throws Exception {
		Map<String, CapturedFile> pkFiles = subset(allfiles, Output.DDL_PK);
		Map<String, CapturedFile> fkFiles = subset(allfiles, Output.DDL_FK);

		if (pkFiles.size() > 0) {
			List<CreateConstraintColumnDTO> targetFks = getTargetFksByPkTable(
					pkFiles.keySet(), hostSkey);
			dropConstraints(targetFks, fkFiles, hostSkey);

			List<CreateConstraintColumnDTO> targetPks = getTargetPkNames(
					pkFiles.keySet(), hostSkey);
			dropConstraints(targetPks, pkFiles, hostSkey);

			Set<String> diffFkTableNames = compareTables(fkFiles.keySet(),
					targetFks);
			executeCapturedDdl(allfiles, Action.DDL_CREATE_PK, Output.DDL_PK,
					hostSkey);
			restoreTargetFks(targetFks, diffFkTableNames, hostSkey);
			executeCapturedDdl(allfiles, Action.DDL_CREATE_FK, Output.DDL_FK,
					hostSkey);
		} else if (fkFiles.size() > 0) {
			List<CreateConstraintColumnDTO> targetFkNames = getTargetFksByFkTable(
					fkFiles.keySet(), hostSkey);
			dropConstraints(targetFkNames, fkFiles, hostSkey);
			executeCapturedDdl(allfiles, Action.DDL_CREATE_FK, Output.DDL_FK,
					hostSkey);
		}
	}

	private void executeDeletes(List<CapturedFile> files, String logDir,
			long hostSkey) throws Exception {
		Map<String, CapturedFile> deleteFiles = subset(files, Output.DELETES);
		Set<Map.Entry<String, CapturedFile>> mapEntries = deleteFiles
				.entrySet();
		for (Map.Entry<String, CapturedFile> me : mapEntries) {
			CapturedFile file = me.getValue();
			try {
				repoDAO.updateBatchTable(
						repoSqlBuilder.getUpdateBatchTableSql(), 0, 0, 0,
						Action.DELETES.getCode(), file.getDpBatchDtlSkey());
				int count = doDelete(file, logDir, hostSkey);
				repoDAO.updateBatchTable(
						repoSqlBuilder.getUpdateBatchTableSql(), 0, count, 0,
						Action.DELETES.getCode(), file.getDpBatchDtlSkey());
			} catch (Exception e) {
				repoDAO.endBatchTable(repoSqlBuilder.getEndBatchTableSql(),
						BatchDetailStatus.ERROR, Action.DELETES,
						file.getDpBatchDtlSkey());
				throw e;
			}

		}
	}

	private void executeInserts(List<CapturedFile> files, String logDir,
			long hostSkey) throws Exception {
		Map<String, CapturedFile> insertFiles = subset(files, Output.INSERTS);
		if (insertFiles.isEmpty())
			return;

		for (CapturedFile cp : files) {
			if (cp.getInsertCnt() == 0)
				// return;
				insertFiles.remove(cp);
		}
		Set<Map.Entry<String, CapturedFile>> mapEntries = insertFiles
				.entrySet();
		for (Map.Entry<String, CapturedFile> mapEntry : mapEntries) {
			CapturedFile file = mapEntry.getValue();
			String tableName = file.getTableName();
			String externalFile = file.getFolderName() + file.getFileName();
			String sql = getInsertFromExternalFileSql(tableName,
					FileFormat.valueOf(file.getFileFormat()),
					FileLocation.valueOf(file.getFileLocation()), externalFile);
			try {
				repoDAO.updateBatchTable(
						repoSqlBuilder.getUpdateBatchTableSql(), 0, 0, 0,
						Action.INSERTS.getCode(), file.getDpBatchDtlSkey());
				int count = targetDAO.insertFromExternalFile(sql, tableName,
						externalFile, logDir, hostSkey);
				repoDAO.updateBatchTable(
						repoSqlBuilder.getUpdateBatchTableSql(), count, 0, 0,
						Action.INSERTS.getCode(), file.getDpBatchDtlSkey());
			} catch (Exception e) {
				repoDAO.endBatchTable(repoSqlBuilder.getEndBatchTableSql(),
						BatchDetailStatus.ERROR, Action.INSERTS,
						file.getDpBatchDtlSkey());
				throw e;
			}

		}
	}

	private int doDelete(CapturedFile pObj, String logDir, long hostSkey) {
		if (pObj.getDeleteCnt() == 0)
			return 0;

		String tableName = pObj.getTableName();
		List<CreateTableDTO> dtos = repoDAO.getTempTableCols(
				repoSqlBuilder.getPkColsDetailsSql(), pObj.getTableSkey(),
				pObj.getDcBatchSkey());
		String pkColsAsString = (String) repoDAO
				.getPkCols(repoSqlBuilder.getPkColsListSql(),
						pObj.getTableSkey(), pObj.getDcBatchSkey()).get(0)
				.get("cols");
		String tempTable = // pObj.get + "_" +
		tableName + "_" + pObj.getDcBatchSkey();

		String createTempTblSql = ddlFactory.getCreateTempTableDDL(dtos,
				tempTable);
		String externalFile = pObj.getFolderName() + pObj.getFileName();
		String insertIntoTempTblSql = getInsertFromExternalFileSql(tempTable,
				FileFormat.valueOf(pObj.getFileFormat()),
				FileLocation.valueOf(pObj.getFileLocation()), externalFile);

		String deleteFromTargetTblSql = propSqlBuilder
				.getDeleteFromTargetTableSql(tableName, tempTable,
						pkColsAsString);
		return targetDAO.performDelete(createTempTblSql, insertIntoTempTblSql,
				deleteFromTargetTblSql, tempTable, externalFile, logDir,
				hostSkey);
	}

	private List<CreateConstraintColumnDTO> getTargetFksByFkTable(
			Set<String> tablesWithFkChanges, long hostSkey) {
		String sql = propSqlBuilder.getFkByFkTableSql(tablesWithFkChanges
				.size());
		return targetDAO.getConstraints(sql, tablesWithFkChanges, hostSkey);
	}

	// Table name- pk constraint name
	private List<CreateConstraintColumnDTO> getTargetPkNames(
			Set<String> tablesWithPkChanges, long hostSkey) {
		String sql = this.propSqlBuilder.getPkByTableSql(tablesWithPkChanges
				.size());
		return targetDAO.getConstraints(sql, tablesWithPkChanges, hostSkey);
	}

	private List<CreateConstraintColumnDTO> getTargetFksByPkTable(
			Set<String> tablesWithPkChanges, long hostSkey) {
		String sql = this.propSqlBuilder.getFkByPkTableSql(tablesWithPkChanges
				.size());
		return targetDAO.getConstraints(sql, tablesWithPkChanges, hostSkey);
	}

	private Set<String> compareTables(Set<String> batchFkTableNames,
			List<CreateConstraintColumnDTO> srcFkCols) {
		Set<String> srcFkTableNames = new HashSet<String>();
		for (CreateConstraintColumnDTO fk : srcFkCols) {
			srcFkTableNames.add(fk.getTableName());
		}
		srcFkTableNames.removeAll(batchFkTableNames);
		return srcFkTableNames;
	}

	private Map<String, CapturedFile> subset(List<CapturedFile> files,
			Output output) {
		Map<String, CapturedFile> subset = new HashMap<String, CapturedFile>();
		for (CapturedFile file : files) {
			if (file.getFileName().endsWith(output.getFileName())) {
				subset.put(file.getTableName(), file);
			}
		}
		// Map: tableName - file name of a ddl type. Example;
		// 'CUSTOMERS'-'fk.sql', CUSTOMERS-inserts.sql
		return subset;
	}

	private Map<String, String[]> readDdls(Map<String, CapturedFile> pkFiles)
			throws IOException {
		Set<Map.Entry<String, CapturedFile>> files = pkFiles.entrySet();
		Iterator<Map.Entry<String, CapturedFile>> iter = files.iterator();
		Map<String, String[]> tableDdls = new HashMap<String, String[]>();
		while (iter.hasNext()) {
			StringBuffer bfr = new StringBuffer();
			Map.Entry<String, CapturedFile> mapentry = iter.next();
			CapturedFile pkFile = mapentry.getValue();
			String fullFilePath = pkFile.getFolderName() + pkFile.getFileName();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(new File(fullFilePath)));

				String line = br.readLine();
				while (line != null) {
					bfr.append(line);
					line = br.readLine();
				}
			} finally {
				br.close();
			}
			String tableName = mapentry.getKey();
			String[] thisFileDdls = StringUtils.tokenizeToStringArray(bfr
					.toString().trim(), ";");

			List<String> allDdls_l = new ArrayList<String>();
			allDdls_l.addAll(Arrays.asList(thisFileDdls));
			tableDdls.put(tableName,
					allDdls_l.toArray(new String[allDdls_l.size()]));
		}

		return tableDdls;
	}

	// Do we need to log this? It may not be a batch table...
	private void restoreTargetFks(List<CreateConstraintColumnDTO> targetFkCols,
			Set<String> diffFkTableNames, long hostSkey) {
		for (CreateConstraintColumnDTO srcFkCol : targetFkCols) {
			if (!diffFkTableNames.contains(srcFkCol.getTableName())) {
				targetFkCols.remove(srcFkCol);
			}
		}
		List<String> ddls = ddlFactory.getCreateFKConstraintDDL(targetFkCols);

		for (String ddl : ddls) {
			targetDAO.executeDdl(ddl, hostSkey);
		}
	}

	private void dropConstraints(List<CreateConstraintColumnDTO> dtos,
			Map<String, CapturedFile> pkFiles, long hostSkey) {
		for (CreateConstraintColumnDTO dto : dtos) {
			String tableName = dto.getTableName();
			repoDAO.updateBatchTable(repoSqlBuilder.getUpdateBatchTableSql(),
					0, 0, 0, Action.DDL_MISC.getCode(), pkFiles.get(tableName)
							.getDpBatchDtlSkey());
			targetDAO.executeDdl(propSqlBuilder.getDropConstraintSql(dto),
					hostSkey);
		}
	}

	private String getInsertFromExternalFileSql(String srcTable,
			FileFormat format, FileLocation location, String externalFile) {
		if (format == FileFormat.Z)
			throw new IllegalStateException(
					"Transfer with Compression is currently not supported");

		String sql_ = null;
		if (format == FileFormat.I) {
			if (location == FileLocation.LOCAL) {
				sql_ = propSqlBuilder
						.getInsertFromExternal_LocalInternalSql(srcTable);
			}
			if (location == FileLocation.REMOTE) {
				sql_ = propSqlBuilder
						.getInsertFromExternal_RemoteInternal(srcTable);
			}
		} else if (format == FileFormat.T) {
			if (location == FileLocation.LOCAL) {
				sql_ = propSqlBuilder
						.getInsertFromExternal_LocalTextSql(srcTable);
			}
			if (location == FileLocation.REMOTE) {
				sql_ = propSqlBuilder
						.getInsertFromExternal_RemoteText(srcTable);
			}
		}
		return StringUtils.replace(sql_, "**", '\'' + externalFile + '\'');
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

}
