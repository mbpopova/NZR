package mycompany.nzr.dc;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mycompany.nzr.common.BatchDetailStatus;
import mycompany.nzr.common.BatchStatus;
import mycompany.nzr.common.BatchType;
import mycompany.nzr.common.ConfiguredSystem;
import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.dao.DataCaptureSrcDAO;
import mycompany.nzr.dc.dto.ConstraintDTO;
import mycompany.nzr.dc.dto.DcSetDetailDTO;
import mycompany.nzr.dc.dto.SrcColumnDTO;
import mycompany.nzr.dc.dto.SrcConstraintAttributeDTO;
import mycompany.nzr.dc.dto.SrcTableConstraintDTO;
import mycompany.nzr.dc.dto.SrcTableDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureSrcSqlBuilder;
import mycompany.nzr.sm.StorageManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

class DataCaptureSet {

}
public class DataCaptureManager {

	Logger logger = Logger.getLogger(DataCaptureManager.class);
	
	public static String ENABLED_FLAG_FIELD = "ENABLED_FLAG";
	public static String DC_SKEY_FIELD = "DC_SKEY";

	@Autowired
	private DataCaptureRepoDAO repoDAO;

	@Autowired
	private DataCaptureRepoSqlBuilder repoSqlBuilder;

	@Autowired
	private DataCaptureSrcDAO srcDAO;

	@Autowired
	private DataCaptureSrcSqlBuilder srcSqlBuilder;

	@Autowired
	private TransferManager transferManager;

	@Autowired
	private DdlManager ddlManager;

    private Map<Long, DataCaptureSet> workingDataCaptureSets  = new TreeMap<Long, DataCaptureSet>();
	/**
	 * IMPORTANT: LOW XID is per table, HIGH XID is per batch. This is because
	 * we transfer in Sets. A table can be missing from one set/batch but appear
	 * in another. HIGH XID is always the current state of the SRC wso it is
	 * single per batch.
	 */
    
    public synchronized void init() {
    	repoDAO.resetBatchStatus_All(repoSqlBuilder.getRunningToErrorBatchStatusAllSets_Sql());
    	repoDAO.resetBatchStatus_All(repoSqlBuilder.getRunningToErrorBatchDetailStatusAllSets_Sql());
    }

	public void startBatch(long dcSetSkey, BatchType batchType)
			throws Throwable {
		logger.info("At the beginning of StartBatch");
		DataCaptureSet dcSet = null;
		
		synchronized (workingDataCaptureSets) {
			if (workingDataCaptureSets.containsKey(dcSetSkey)) {
				dcSet = workingDataCaptureSets.get(dcSetSkey);
			} else {
				dcSet = new DataCaptureSet();
				workingDataCaptureSets.put(dcSetSkey, dcSet);
			}
		}

		synchronized (dcSet) {
			logger.info("Starting Batch for Data Capture Set " + dcSetSkey);
			List<ConfiguredSystem> systems = this.repoDAO
					.getConfiguredSystemByDcSkey(
							repoSqlBuilder.getConfiguredSystemByDcSkey_Sql(),
							dcSetSkey);
			if (systems.size() == 0) {
				return ;
			}
			ConfiguredSystem system = systems.get(0);
			logger.info("Data Capture source system: " + system.getHostSkey());
			srcDAO.createAndStoreTemplate(system);
			long batchSkey = repoDAO.getNewBatchSkey(repoSqlBuilder
					.getBatchSkeySQL());
			Map<String, Object> dcSetConfig = getDataCaptureSetConfig(dcSetSkey);
			logger.info("Data Capture Set config fetched");
			FileFormat format = FileFormat.valueOf((String) dcSetConfig
					.get("STG_FORMAT"));
			FileLocation location = FileLocation.valueOf((String) dcSetConfig
					.get("STG_LOCATION"));

			repoDAO.resetBatchStatus(
					this.repoSqlBuilder.getRunningToErrorBatchStatus_Sql(),
					dcSetSkey); // nzr_dc_batch
			repoDAO.resetBatchStatus(this.repoSqlBuilder.getRunningToErrorBatchStatus_Sql(), dcSetSkey); //nzr_dc_batch_detail
			
            logger.info("Reset incomplete batch statuses to ERROR");
            
			try {
				if (BatchType.I == batchType) {
					logger.info("Ready to start Incremental Batch");
					startIncrementalBatch_(dcSetSkey, batchSkey, format,
							location, system);
				}

				if (BatchType.F == batchType) {
					logger.info("ready to start Full Batch");
					startFullBatch_(dcSetSkey, batchSkey, format, location, system);
				}
				if (BatchType.T == batchType) {
					logger.info("Ready to start Trustme batch");
					startTrustmeBatch_(dcSetSkey, batchSkey, system.getHostSkey());
				}
			} catch (Throwable t) {
				// Batch Failure
				logger.error("Error occured: " + t.getMessage());
				repoDAO.updateBatchInfo(repoSqlBuilder.getUpdateBatchInfoSql(),
						BatchStatus.ERROR.toString(), batchSkey);
				logger.info("Error status has been set for DC Batch " + batchSkey);
				throw t;
			}
			// Batch Success
			repoDAO.updateBatchInfo(repoSqlBuilder.getUpdateBatchInfoSql(),
					BatchStatus.COMPLETED.toString(), batchSkey);
			logger.info("Batch Status has been updated to COMPLETED");
		}
	}

	private void startTrustmeBatch_(long dcSetSkey, long batchSkey,
			long hostSkey) {
		long highxid = srcDAO.getHighStableXid(
				srcSqlBuilder.getHighStableXid(), hostSkey);
		logger.info("High XID for batch " + batchSkey + "fetched");
		repoDAO.insertNewBatchInfo(repoSqlBuilder.getInsertBatchInfoSQL(),
				batchSkey, dcSetSkey, highxid, BatchStatus.RUNNING.toString(),
				BatchType.T);
		logger.info("Batch Info entered. High XID captured");

		List<DcSetDetailDTO> dcSetTables = repoDAO.fetchDataCaptureSetDetails(
				repoSqlBuilder.getSelectTablesInDataCaptureSetSQL(), dcSetSkey);

		logger.info("Tables fetched in DC Set: " + dcSetTables.toString());

		moveScrDdlToWrkTables(dcSetTables, dcSetSkey, batchSkey, hostSkey);

		for (DcSetDetailDTO dto : dcSetTables) {
			long batchDtlSkey = repoDAO.getNewBatchDtlSkey();
			repoDAO.insertIntoBatchDetail(
					repoSqlBuilder.getInsertBatchDetailSql(), batchDtlSkey,
					BatchDetailStatus.RUNNING.toString(), batchSkey,
					dto.getTableSkey(), dto.getSrcObjectId(), -1);
			try {
				moveWrkToHist(dto.getTableSkey(), batchSkey, batchDtlSkey);
				repoDAO.updateBatchDetail(
						repoSqlBuilder.getUpdateBatchDetailSql(), batchDtlSkey,
						BatchDetailStatus.COMPLETED.toString(), 0, 0, 0);
				logger.info("Batch Detail Info has been updated. Status = COMPLETED. Tbl: "
						+ dto.getTableName());
			} catch (Exception e) {
				e.printStackTrace();
				repoDAO.updateBatchDetail(
						repoSqlBuilder.getUpdateBatchDetailSql(), batchDtlSkey,
						BatchDetailStatus.ERROR.toString(), 0, 0, 0);
				logger.info("Batch Detail Status is set to ERROR");
				throw e;
			}

		}
	}

	@Autowired
	StorageManager sm;
	
	private void startFullBatch_(long dcSetSkey, long batchSkey,
			FileFormat fileFormat, FileLocation fileLocation, ConfiguredSystem system) throws Exception {
		logger.info("Full batch started. Low XID = 0");
		long lowxid = 0;
		long highxid = srcDAO
				.getHighStableXid(srcSqlBuilder.getHighStableXid(), system.getHostSkey());
        logger.info("High XID fetched");
		repoDAO.insertNewBatchInfo(repoSqlBuilder.getInsertBatchInfoSQL(),
				batchSkey, dcSetSkey, highxid,
				BatchStatus.RUNNING.toString(), BatchType.F);
		long batchFolderSkey = insertBatchFolder(batchSkey);
		
		List<DcSetDetailDTO> dcSetTables = repoDAO.fetchDataCaptureSetDetails(
				repoSqlBuilder.getSelectTablesInDataCaptureSetSQL(), dcSetSkey);
		/*
        logger.info("Moving Data Dictionary data from SRC to WRK tables...");
		moveTableInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, system.getHostSkey());
		moveTableConstraintInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, system.getHostSkey());
		moveColumnInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, system.getHostSkey());
		moveColumnConstraintInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, system.getHostSkey());
        logger.info("Done");
        */
		moveScrDdlToWrkTables(dcSetTables, dcSetSkey, batchSkey, system.getHostSkey());
		
		// if (generateDdl) { //TODO - future of dc set
		long insertsCnt;
		int ddlFlag ;
		
		for (DcSetDetailDTO table : dcSetTables) {
			insertsCnt = 0;
			ddlFlag = 0;

			long batchDtlSkey = repoDAO.getNewBatchDtlSkey();
			repoDAO.insertIntoBatchDetail(
					repoSqlBuilder.getInsertBatchDetailSql(), batchDtlSkey,
					BatchDetailStatus.RUNNING.toString(), batchSkey,
					table.getTableSkey(), table.getSrcObjectId(), lowxid);
			try {
				logger.info("Moving WRK to HIST for table "
						+ table.getTableName());
				moveWrkToHist(table.getTableSkey(), batchSkey, batchDtlSkey);
				logger.info("Done");
				ddlFlag = ddlManager.createBaselineDdl(batchSkey, batchDtlSkey,
						batchFolderSkey, table, fileLocation, fileFormat);
				logger.info("Baseline (CREATE TABLE) ddl created for table "
						+ table.getTableName());
				long tableSkey = table.getTableSkey();
				String tableName = table.getTableName();

				ddlManager.generatePksDdl(batchSkey, batchDtlSkey,
						batchFolderSkey, tableSkey, tableName, fileLocation,
						fileFormat);
				logger.info("Primary Key ddl created for table "
						+ table.getTableName());
				ddlManager.generateFksDdl(batchSkey, batchDtlSkey,
						batchFolderSkey, tableSkey, tableName, fileLocation,
						fileFormat);
				logger.info("Foreign Key ddl created for table "
						+ table.getTableName());
				ddlManager.generateUniqueConstraintDdl(batchSkey, batchDtlSkey,
						batchFolderSkey, tableSkey, tableName, fileLocation,
						fileFormat);
				logger.info("Unique Constraint ddl created for table "
						+ table.getTableName());

				// TODO: location and format - attributes of dc set?
				logger.info("Transfering inserts");
				insertsCnt = transferManager.transferInserts(batchSkey,
						table.getSrcObjectId(), table.getTableName(),
						fileFormat, fileLocation, lowxid, highxid,
						batchFolderSkey, batchDtlSkey, system);
				logger.info("Done tranfering inserts. Cnt = " + insertsCnt);
				repoDAO.updateBatchDetail(
						repoSqlBuilder.getUpdateBatchDetailSql(), batchDtlSkey,
						BatchDetailStatus.COMPLETED.toString(), insertsCnt, 0,
						ddlFlag);
				logger.info("Batch Detail updated. Status = COMPLETED");
			} catch (Exception e) {
				// Batch Detail (table) failure:
				logger.error("Error in Batch: " + e.getMessage());
				repoDAO.updateBatchDetail(
						repoSqlBuilder.getUpdateBatchDetailSql(), batchDtlSkey,
						BatchDetailStatus.ERROR.toString(), insertsCnt, 0,
						ddlFlag);
				logger.info("Batch Detail Status is set to ERROR");
				throw e;
			}
		}
	}

	private long  insertBatchFolder(long batchSkey) throws Exception {
		String batchFolder = sm.getOrCreateDirectory(batchSkey);
		long batchFolderSkey = repoDAO.getBatchFilderSkey();
		repoDAO.insertBatchFolder(repoSqlBuilder.getInsertBatchFolder_Sql(), batchFolderSkey, batchSkey, batchFolder);
		return batchFolderSkey;
		
	}

	private void moveScrDdlToWrkTables(List<DcSetDetailDTO> dcSetTables, long dcSetSkey, long batchSkey, long hostSkey) {
		
		logger.info("Ready to move data dictionary data from SRC to WRK tables");
		moveTableInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, hostSkey);
		logger.info("Table info copied");
		
		moveTableConstraintInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, hostSkey);
		logger.info("Table Constraint data copied");
		
		moveColumnInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, hostSkey);
		logger.info("Column Info copied");
		
		moveColumnConstraintInfoToWrkTbl(dcSetTables, dcSetSkey, batchSkey, hostSkey);
		logger.info("Column Constraint info copied");
	}
	
	
	private void startIncrementalBatch_(long dcSetSkey, long batchSkey,
			FileFormat fileFormat, FileLocation fileLocation, ConfiguredSystem system) throws Exception {

		long highxid = srcDAO
				.getHighStableXid(srcSqlBuilder.getHighStableXid(), system.getHostSkey());
		logger.info("Got High XID for the batch");
		
		repoDAO.insertNewBatchInfo(repoSqlBuilder.getInsertBatchInfoSQL(),
				batchSkey, dcSetSkey, highxid,
				BatchStatus.RUNNING.toString(), BatchType.I);
		
		// validateDataCaptureSet(dcSetSkey);
		List<DcSetDetailDTO> dcSetTables = repoDAO.fetchDataCaptureSetDetails(
				repoSqlBuilder.getSelectTablesInDataCaptureSetSQL(), dcSetSkey);
		//for (DcSetDetailDTO dto : dcSetTables) {
			//String sql = this.repoSqlBuilder.getCountPrevFullBatch_Sql();
		//	if (repoDAO.getPreviousFullBatchCount(sql, batchSkey, dcSetSkey, dto.getTableSkey()) == 0) {
			//	throw new IllegalStateException("A Full Batch should be run before an Incremental one for the requested Data Set(every table)");
			//	startFullBatch_(dcSetSkey, batchSkey, fileFormat, fileLocation, system);
			//	return;
			//}
		//}
		
		logger.info("Fetched tables in the DC Set");
		long batchFolderSkey = this.insertBatchFolder(batchSkey);

		moveScrDdlToWrkTables(dcSetTables, dcSetSkey, batchSkey, system.getHostSkey());

		BatchDetailStatus bdStatus = BatchDetailStatus.COMPLETED;

		// TODO - either do diff ddl table by table or get tables with ddl to
		// insert correct ddlFlag;

		for (DcSetDetailDTO table : dcSetTables) {

			long lowxid = repoDAO.getLowStableXID(
					repoSqlBuilder.getLowStableXid(), table.getTableSkey(),
					batchSkey);
			logger.info("Low XID for the table '" + table + "' fetched");
			long batchDtlSkey = repoDAO.getNewBatchDtlSkey();
			repoDAO.insertIntoBatchDetail(
					repoSqlBuilder.getInsertBatchDetailSql(), batchDtlSkey,
					BatchDetailStatus.RUNNING.toString(), batchSkey,
					table.getTableSkey(), table.getSrcObjectId(), lowxid);
			long tableSkey = table.getTableSkey();
			long previousBatchSkey = repoDAO.getPreviousBatchSkey(
					repoSqlBuilder.getPreviousBatchSql(), batchSkey, dcSetSkey, tableSkey);

			long insertCnt = 0;
			long deleteCnt = 0;
			boolean ddlFlag = false;

			try {  // For some tables there may be no previous Full batch(=no History). Hence - we can not detect changes. 
				   // It was decided not to generate CREATE TABLE for Incremental batches.
				   // Thus - skip ddl for tables with lowxid = 0. Just do data logic for them (inserts, deletes)
				if (lowxid > 0 && ddlManager.isDdlChanged(previousBatchSkey, batchSkey,
						table.getTableName())) {
					logger.info("Determined DDL changes, moving WRK to HIST...");
					moveWrkToHist(tableSkey, batchSkey, batchDtlSkey);
					logger.info("Moved WRK to HIST. Creating Diff DDL");
					
					ddlFlag = ddlManager.createDiffDdl(previousBatchSkey,
							batchSkey, batchDtlSkey, batchFolderSkey,
							table.getTableName(), fileLocation, fileFormat);
					logger.info("Diff DDL created");
				}
				
				insertCnt = transferManager.transferInserts(batchSkey,
						table.getSrcObjectId(), table.getTableName(),
						fileFormat, fileLocation, lowxid, highxid,
						batchFolderSkey, batchDtlSkey, system);
				logger.info("Inserts captured");
				deleteCnt = transferManager.transferDeletes(batchSkey,
						table.getTableSkey(), table.getSrcObjectId(),
						table.getTableName(), fileFormat, fileLocation, lowxid,
						highxid, batchFolderSkey, batchDtlSkey, system.getHostSkey());
                logger.info("Deletes captured");
				repoDAO.updateBatchDetail(
						repoSqlBuilder.getUpdateBatchDetailSql(), batchDtlSkey,
						bdStatus.toString(), insertCnt, deleteCnt, ddlFlag? 1 : 0);
				logger.info("Batch Detail updated");
			} catch (Exception e) {
				// Batch Detail (table) failure:
				logger.info("Error : " + e.getMessage());
				repoDAO.updateBatchDetail(
						repoSqlBuilder.getUpdateBatchDetailSql(), batchDtlSkey,
						BatchDetailStatus.ERROR.toString(), insertCnt,
						deleteCnt, ddlFlag? 1 : 0);
				logger.info("Status is set to error for batch table" + batchDtlSkey );
				throw e;
			}
		}
	}

	private void moveWrkToHist(long tableSkey, long batchSkey, long batchDtlSkey) {
		repoDAO.moveWrkTableOrTableAttr(repoSqlBuilder.getMoveWrkTable_Sql(),
				batchDtlSkey, tableSkey, batchSkey);
		repoDAO.moveWrkTableOrTableAttr(
				repoSqlBuilder.getMoveWrkTableAttr_Sql(), batchDtlSkey,
				tableSkey, batchSkey);

		repoDAO.moveWrkConstr(repoSqlBuilder.getMoveWrkConstr_Sql(),
				batchDtlSkey, tableSkey, batchSkey);
		List<ConstraintDTO> constraints = repoDAO
				.getConstraintNameAndSkey(
						repoSqlBuilder.getConstrNameAndSkey_Sql(), tableSkey,
						batchSkey);

		for (ConstraintDTO constr : constraints) {
			long constrSkey = constr.getConstraintSkey();
			String constrName = constr.getConstraintName();

			repoDAO.moveWrkConstrAttr(
					repoSqlBuilder.getMoveWrkConstrAttr_Sql(), constrSkey,
					batchDtlSkey, constrName, batchSkey);
		}
	}

	private void moveTableInfoToWrkTbl(List<DcSetDetailDTO> dcSetTables,
			long dcSetSkey, long batchSkey, long hostSkey) {
		String getTableInfoSql = srcSqlBuilder
				.getSelectTableInfoSQL(dcSetTables.size());

		List<SrcTableDTO> srcTables = srcDAO.fetchTableInfo(getTableInfoSql,
				getTableNames(dcSetTables), hostSkey);

		for (SrcTableDTO srcTable : srcTables) {
			String srcTableName = srcTable.getTableName();
			// srcTable.setDescription("ABC");
			for (DcSetDetailDTO dcSetTable : dcSetTables) {
				if (srcTableName.equals(dcSetTable.getTableName())) {
					srcTable.setTableSkey(dcSetTable.getTableSkey());
					break;
				}
			}
		}

		String insertTableInfoSql = repoSqlBuilder.getInsertTableInfoSQL();
		repoDAO.insertTableInfoWrk(insertTableInfoSql, dcSetSkey, batchSkey,
				srcTables);
	}

	private String[] getTableNames(List<DcSetDetailDTO> dcSetTables) {
		String[] dcSetTableNames = new String[dcSetTables.size()];
		int i = -1;
		for (DcSetDetailDTO dto : dcSetTables) {
			dcSetTableNames[++i] = dto.getTableName();
		}
		return dcSetTableNames;
	}

	private void moveTableConstraintInfoToWrkTbl(
			List<DcSetDetailDTO> dcSetTables, long dcSetSkey, long batchSkey, long hostSkey) {
		String getInfoSql = srcSqlBuilder
				.getSelectTableConstraintInfoSQL(dcSetTables.size());
		List<SrcTableConstraintDTO> dtos = srcDAO.fetchTableConstraintInfo(
				getInfoSql, getTableNames(dcSetTables), hostSkey);

		for (SrcTableConstraintDTO dto : dtos) {
			String srcTableName = dto.getRelation();
			for (DcSetDetailDTO dcSetTable : dcSetTables) {
				if (srcTableName.equals(dcSetTable.getTableName())) {
					dto.setTableSkey(dcSetTable.getTableSkey());
					break;
				}
			}
		}

		String insertInfoSql = repoSqlBuilder.getInsertTableConstraintInfoSQL();
		repoDAO.insertTableConstraintInfoWrk(insertInfoSql, dcSetSkey,
				batchSkey, dtos);
	}

	private void moveColumnInfoToWrkTbl(List<DcSetDetailDTO> dcSetTables,
			long dcSetSkey, long batchSkey, long hostSkey) {
		String getInfoSql = srcSqlBuilder.getSelectColumnInfoSQL(dcSetTables
				.size());
		List<SrcColumnDTO> dtos = srcDAO.fetchColumnInfo(getInfoSql,
				getTableNames(dcSetTables), hostSkey);

		// TODO: dataLength, dataPrecision and dataScale are not fetched from
		// src but they are not null-able columns in wrk table. Fix the query!!!
		for (SrcColumnDTO dto : dtos) {
			String srcTableName = dto.getTableName();
			for (DcSetDetailDTO dcSetTable : dcSetTables) {
				if (srcTableName.equals(dcSetTable.getTableName())) {
					dto.setTableSkey(dcSetTable.getTableSkey());
					break;
				}
			}
		}
		String insertInfoSql = repoSqlBuilder.getInsertColumnInfoSQL();
		repoDAO.insertColumnInfoWrk(insertInfoSql, dcSetSkey, batchSkey, dtos);
	}

	private void moveColumnConstraintInfoToWrkTbl(
			List<DcSetDetailDTO> dcSetTables, long dcSetSkey, long batchSkey, long hostSkey) {
		String getInfoSql = srcSqlBuilder
				.getSelectConstraintColsInfoSQL(dcSetTables.size());
		List<SrcConstraintAttributeDTO> dtos = srcDAO.fetchConstraintColsInfo(
				getInfoSql, getTableNames(dcSetTables), hostSkey);

		for (SrcConstraintAttributeDTO dto : dtos) {
			String srcTableName = dto.getTableName();
			for (DcSetDetailDTO dcSetTable : dcSetTables) {
				if (srcTableName.equals(dcSetTable.getTableName())) {
					dto.setTableSkey(dcSetTable.getTableSkey());
					break;
				}
			}
		}

		String insertInfoSql = repoSqlBuilder
				.getInsertConstraintColumnInfoSQL();
		repoDAO.insertConstraintColumnsInfoWrk(insertInfoSql, dcSetSkey,
				batchSkey, dtos);
	}

	private Map<String, Object> getDataCaptureSetConfig(long dcSetSkey)
			throws DataCaptureException {
		String sql = repoSqlBuilder.getSelectDataCaptureSetInfoSQL();
		Map<String, Object> dcConfig = repoDAO.fetchDataCaptureSetConfig(sql,
				dcSetSkey);

		/*
		 * if (dcConfig.size() != 1) { throw new DataCaptureException(
		 * "Data Set Config is invalid. Repo should have exactly one record for a name. Returned: "
		 * + dcConfig.size()); }
		 */
		if (!String.valueOf(dcConfig.get(ENABLED_FLAG_FIELD)).equals("1")) {
			throw new DataCaptureException("The requested Dataset is disabled");
		}
		return dcConfig;
	}

}
/*
 * private void generateBaselineDDL() { } private void generateDiffDDL(String
 * dataCaptureSet) { } public void startTrustmeBatch(Destination destination,
 * String dataCaptureSet) { startBatch(BatchType.TRUSTME, destination,
 * dataCaptureSet); } public void startBaselineBatch(Destination destination,
 * String dataCaptureSet) { startBatch(BatchType.BASELINE, destination,
 * dataCaptureSet) ; } public void startIncrementalBatch(Destination
 * destination, String dataCaptureSet) { if (destination ==
 * Destination.DATABASE) { throw new IllegalArgumentException
 * ("Not supported Destination"); } startBatch(BatchType.INCREMENTAL,
 * destination, dataCaptureSet); } public static String ENABLED_FLAG_FIELD =
 * "ENABLED_FLAG"; public static String DC_SKEY_FIELD = "DC_SKEY";
 */