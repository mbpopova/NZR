package mycompany.nzr.dc;

import java.util.List;

import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.common.Output;
import mycompany.nzr.dc.dao.DataCaptureDdlSourceDAO;
import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.ddlfactory.DdlFactory;
import mycompany.nzr.dc.dto.ColumnChangeIndicatorsDTO;
import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
import mycompany.nzr.dc.dto.CreateTableDTO;
import mycompany.nzr.dc.dto.DcSetDetailDTO;
import mycompany.nzr.dc.dto.RenameColumnDTO;
import mycompany.nzr.dc.dto.TableChangeIndicatorsDTO;
import mycompany.nzr.dc.dto.UpdateOrganizeOnDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureDdlSourceSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureSrcSqlBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

public class DdlManager {

	@Autowired
	private DataCaptureDdlSourceDAO ddlSourceDAO;

	@Autowired
	private DataCaptureRepoDAO repoDAO;

	@Autowired
	private DataCaptureRepoSqlBuilder repoSqlBuilder;

	@Autowired
	private DdlFactory ddlFactory;

	@Autowired
	private DataCaptureDdlSourceSqlBuilder ddlSourceSqlBuilder;

	@Autowired
	private DataCaptureSrcSqlBuilder srcSqlBuilder;

	@Autowired
	private DataCaptureDdlWriter fw;

	public int createBaselineDdl(long batchSkey, long batchDtlSkey,
			long batchFolderSkey, DcSetDetailDTO table,
			FileLocation fileLocation, FileFormat fileFormat) throws Exception {

		String sql = ddlSourceSqlBuilder.getTableSQL();

		long tableSkey = table.getTableSkey();
		String tableName = table.getTableName();
		List<CreateTableDTO> dtos = ddlSourceDAO
				.getTableDdlData(sql, tableSkey);
		if (dtos == null || dtos.isEmpty()) {
			return 0;
		}
		String ddl = (ddlFactory.getCreateTableDDL(dtos, tableName));
		fw.write(batchSkey, batchDtlSkey, batchFolderSkey,
				table.getTableName(), ddl, false, Output.DDL_TABLE,
				fileLocation, fileFormat);
		return 1;

	}

	public boolean isDdlChanged(long prevBatchSkey, long curBatchSkey,
			String tableName) {
		String changedTblSql = repoSqlBuilder.getTableChangedIndSql();
		TableChangeIndicatorsDTO tableChangeInds = null;
		try {
			tableChangeInds = repoDAO.getTableChangeIndicators(changedTblSql,
					prevBatchSkey, curBatchSkey, tableName);
		} catch (EmptyResultDataAccessException e) {
			// tableChangeInds == null;
		}

		if (tableChangeInds != null) {
			if (tableChangeInds.getTableRebuiltInd() == 1
					|| tableChangeInds.getTableRenamedInd() == 1) {
				return false;
			}

			if (tableChangeInds.getTableCommentChangedInd() == 1) {
				return true;
			}
		}

		String changedOrgTableSql = repoSqlBuilder.getTableOrgChangedIndSql();
		TableChangeIndicatorsDTO tableOrgChangeInds = null;

		try {
			tableOrgChangeInds = repoDAO.getTableChangeIndicators(
					changedOrgTableSql, prevBatchSkey, curBatchSkey, tableName);
		} catch (EmptyResultDataAccessException e) {

		}

		if (tableChangeInds != null) {
			if (tableOrgChangeInds.getTableOrgChangedInd() == 1
					|| tableOrgChangeInds.getTableColumnsChangedInd() == 1) {
				return true;
			}
		}
		return false;
	}

	public boolean createDiffDdl(long prevBatchSkey, long curBatchSkey, long batchDtlSkey, long batchFolderSkey,
			String tableName, FileLocation fileLocation, FileFormat fileFormat) throws Exception {

		boolean diffDdlCreated = false;

		String changedTblSql = repoSqlBuilder.getTableChangedIndSql();
		TableChangeIndicatorsDTO tableChangeInds = repoDAO
				.getTableChangeIndicators(changedTblSql, prevBatchSkey,
						curBatchSkey, tableName);

		StringBuffer ddl = new StringBuffer();

		if (tableChangeInds.getTableRebuiltInd() == 1 || tableChangeInds.getTableRenamedInd() == 1) {
			// For now its been decided to not handle rebuilt or renamed tables;
				return false;
		}

		else if (tableChangeInds.getTableCommentChangedInd() == 1) {
			ddl.append(generateUpdateTableCommentDdl(curBatchSkey,
					tableChangeInds.getTableName()));
			diffDdlCreated = true;
			fw.write(curBatchSkey, batchDtlSkey, batchFolderSkey, tableChangeInds.getTableName(),
					ddl.toString(), false, Output.DDL_MISC,
					fileLocation, fileFormat);
		}


		String changedOrgTableSql = repoSqlBuilder.getTableOrgChangedIndSql();
		
		TableChangeIndicatorsDTO tableOrgChangeInds = repoDAO
				.getTableChangeIndicators(changedOrgTableSql, prevBatchSkey,
						curBatchSkey, tableName);// MOMMY IS THE BEST AND
													// SOMETIMES DOESNT FORGET
													// TO GIVE ME BREAKFAST;

		if (tableOrgChangeInds.getTableOrgChangedInd() == 1) {
			fw.write(curBatchSkey, batchDtlSkey, batchFolderSkey, tableOrgChangeInds.getTableName(),
					generateUpdateTableOrganizeOnDdl(prevBatchSkey, curBatchSkey, tableName),
					false, Output.DDL_MISC, fileLocation, fileFormat);
			diffDdlCreated = true;
		}

		if (tableOrgChangeInds.getTableColumnsChangedInd() == 1) {

			String columnChangedIndSql = repoSqlBuilder
					.getColumnChangedIndSql();

			List<ColumnChangeIndicatorsDTO> colChangeInds = repoDAO
					.getColumnChangeIndicators(columnChangedIndSql,
							prevBatchSkey, curBatchSkey, tableName);

			for (ColumnChangeIndicatorsDTO dto : colChangeInds) {
				String colName = dto.getColumnName();
				long tableSkey = tableOrgChangeInds.getTableSkey();

				ddl = new StringBuffer();

				if (dto.getColumnCommentChangedInd() == 1) {
					ddl.append(generateUpdateColumnCommentDdl(curBatchSkey,
							tableName, colName));
				}
				if (dto.getColumnDefaultChangedInd() == 1) {
					ddl.append(generateUpdateColumnDefaultDdl(curBatchSkey,
							tableName, colName));
				}
				if (dto.getColumnLengthChangedInd() == 1) {
					ddl.append(generateUpdateColumnLengthDdl(tableSkey,
							colName, curBatchSkey));
				}
				if (dto.getColumnRenamedInd() == 1) {
					ddl.append(generateRenameColumnDdl(prevBatchSkey, curBatchSkey, tableSkey,
							colName));
				}
			}
			fw.write(curBatchSkey, batchDtlSkey, batchFolderSkey, tableName, ddl.toString(), false,
					Output.DDL_MISC, FileLocation.REMOTE, fileFormat);
			diffDdlCreated = true;
		}
		return diffDdlCreated;
	}

	// Tested
	public String generateUpdateColumnCommentDdl(long batchSkey,
			String tableName, String colName) {
		return ddlFactory.getUpdateColumnCommentDDL(ddlSourceDAO
				.getUpdateColumnCommentDdlData(
						ddlSourceSqlBuilder.getUpdateColumnCommentSQL(),
						batchSkey, tableName, colName));
	}

	// Tested
	public String generateUpdateColumnDefaultDdl(long batchSkey,
			String tableName, String colName) {
		return ddlFactory.getUpdateDefaultValueDDL(ddlSourceDAO
				.getUpdateDefaultValueDdlData(
						ddlSourceSqlBuilder.getUpdateDefaultValueSQL(),
						batchSkey, tableName, colName));
	}

	// Tested
	public String generateUpdateColumnLengthDdl(long tableSkey, String colName,
			long batchSkey) {
		return ddlFactory.getExtendVarcharColumnDDL(ddlSourceDAO
				.getExtendVarcharColumnDdlData(
						ddlSourceSqlBuilder.getExtendVarcharColumnSQL(),
						batchSkey, tableSkey, colName));
	}

	// Tested, TODO - modify sql to handle column name change with other
	// modifications
	public String generateRenameColumnDdl(long prevBatchSkey, long batchSkey, long tableSkey,
			String colName) {
		
		List<RenameColumnDTO> dtos = ddlSourceDAO.getRenameColumnDdlData(
				ddlSourceSqlBuilder.getRenameColumnSQL(), prevBatchSkey,  batchSkey, tableSkey, colName);
		
		// DdlFactory accepts a list of column name changes (=several
		// "old columns") but we choose to get ddl src data for a single column
		// at a time hence the check:
		if (dtos.size() > 1) {
			throw new IllegalStateException(
					"Should return only one RenameColumnDTO for column: "
							+ colName);

		}
		return ddlFactory.getRenameColumnDDL(dtos);
	}

	// Tested
	private String generateUpdateTableOrganizeOnDdl(long prevBatchSkey, long batchSkey, String tableName) {
		List<UpdateOrganizeOnDTO> dtos = ddlSourceDAO
		.getUpdateOrganizeOnDdlData(
				ddlSourceSqlBuilder.getUpdateOrganizeOnSQL(),
				prevBatchSkey, batchSkey, tableName);
		if (dtos != null && dtos.isEmpty()) {
			UpdateOrganizeOnDTO dto = new UpdateOrganizeOnDTO();
			dtos.add(dto);
		}
		for (UpdateOrganizeOnDTO dto : dtos) {
			dto.setTableName(tableName);
		}
		return ddlFactory.getUpdateOrganizeOn(dtos		
				);
	}

	// TODO - remove rename table logic for now
	private String generateRenameTableDdl(long batchSkey, String tableName) {
		// return ddlFactory.getRenameTableDDL(ddlSourceDAO.get
		return null;
	}

	// Tested
	private String generateUpdateTableCommentDdl(long batchSkey,
			String tableName) {
		return ddlFactory.getUpdateTableCommentDDL(ddlSourceDAO
				.getUpdateTableCommentDdlData(
						ddlSourceSqlBuilder.getUpdateTableCommentSQL(),
						batchSkey, tableName));
	}

	/*
	 * 
	 * FKs for a table in a full batch, no constraint name passed(no ind sql).
	 */

	public void generateFksDdl(long batchSkey, long batchDtlSkey, long batchFolderSkey, long tableSkey,
			String tableName, FileLocation location, FileFormat fileFormat) throws Exception {

		String ddlSrcSql = ddlSourceSqlBuilder.getCreateFKsForTable_Sql();
		List<CreateConstraintColumnDTO> dtos = ddlSourceDAO.getConstraintData(
				ddlSrcSql, tableSkey, batchSkey);

		if (dtos != null && !dtos.isEmpty()) {
			List<String> ddls = ddlFactory.getCreateFKConstraintDDL(dtos);
			for (String ddl : ddls) {
				fw.write(batchSkey, batchDtlSkey, batchFolderSkey, tableName, ddl, false,
						Output.DDL_FK, location, fileFormat);
			}
		}
	}

	/*
	 * 
	 * PKs for a table in a full batch, no constraint name passed(no ind sql).
	 */

	public void generatePksDdl(long batchSkey, long batchDtlSkey, long batchFolderSkey, long tableSkey,
			String tableName, FileLocation location, FileFormat fileFormat) throws Exception {

		List<CreateConstraintColumnDTO> dtos = ddlSourceDAO.getConstraintData(
				ddlSourceSqlBuilder.getCreatePKsForTable_Sql(), tableSkey,
				batchSkey);

		if (dtos != null && !dtos.isEmpty()) {
			List<String> ddls = ddlFactory.getCreatePKConstraintDDL(dtos);
			for (String ddl : ddls) {
				fw.write(batchSkey, batchDtlSkey, batchFolderSkey, tableName, ddl, false,
						Output.DDL_PK, location, fileFormat);
			}
		}

	}

	/*
	 * 
	 * Unique Constraints for a table in a full batch, no constraint name
	 * passed(no ind sql).
	 */

	public void generateUniqueConstraintDdl(long batchSkey, long batchDtlSkey, long batchFolderSkey, long tableSkey,
			String tableName, FileLocation location, FileFormat fileFormat) throws Exception {

		List<CreateConstraintColumnDTO> dtos = ddlSourceDAO.getConstraintData(
				ddlSourceSqlBuilder.getCreateUniqueConstrantsForTable_Sql(),
				tableSkey, batchSkey);
		if (dtos != null && !dtos.isEmpty()) {
			List<String> ddls = ddlFactory.getCreateUniqueConstraintDDL(dtos);
			for (String ddl : ddls) {
				fw.write(batchSkey, batchDtlSkey, batchFolderSkey, tableName, ddl, false, Output.DDL_MISC,
						location, fileFormat);
			}
		}
	}

	// TODO - no ind sql yet
	@SuppressWarnings("unused")
	private void createFkConstraintDdl(long batchSkey, long tableSkey,
			String constraintName) {

		String ddlSrcSql = ddlSourceSqlBuilder.getCreateFKConstraintSQL();
		// Get data to build DDL:
		List<CreateConstraintColumnDTO> dtos = ddlSourceDAO.getConstraintData(
				ddlSrcSql, tableSkey, constraintName, batchSkey);
		// Actually build DDL:
		ddlFactory.getCreateFKConstraintDDL(dtos);
	}

	@SuppressWarnings("unused")
	private void createPkConstraintDdl(long batchSkey, long tableSkey,
			String constraintName) {

		String ddlSrcSql = ddlSourceSqlBuilder.getCreatePKConstraintSQL();
		// Get data to build DDL:
		List<CreateConstraintColumnDTO> dtos = ddlSourceDAO.getConstraintData(
				ddlSrcSql, tableSkey, constraintName, batchSkey);
		// Actually build DDL:
		ddlFactory.getCreatePKConstraintDDL(dtos);
	}

	@SuppressWarnings("unused")
	private void createUniqueConstraintDdl(long batchSkey, long tableSkey,
			String constraintName) {

		String ddlSrcSql = ddlSourceSqlBuilder
				.getCreateUniqueKeyConstraintSQL();
		// Get data to build DDL:
		List<CreateConstraintColumnDTO> dtos = ddlSourceDAO.getConstraintData(
				ddlSrcSql, tableSkey, constraintName, batchSkey);
		// Actually build DDL:
		ddlFactory.getCreateUniqueConstraintDDL(dtos);
	}

}
