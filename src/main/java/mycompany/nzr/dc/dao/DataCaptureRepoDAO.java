package mycompany.nzr.dc.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mycompany.nzr.common.BatchType;
import mycompany.nzr.common.ConfiguredSystem;
import mycompany.nzr.common.Output;
import mycompany.nzr.dc.dto.ColumnChangeIndicatorsDTO;
import mycompany.nzr.dc.dto.ConstraintDTO;
import mycompany.nzr.dc.dto.DcSetDetailDTO;
import mycompany.nzr.dc.dto.OnDemandScheduleDTO;
import mycompany.nzr.dc.dto.ScheduleDTO;
import mycompany.nzr.dc.dto.SrcColumnDTO;
import mycompany.nzr.dc.dto.SrcConstraintAttributeDTO;
import mycompany.nzr.dc.dto.SrcTableConstraintDTO;
import mycompany.nzr.dc.dto.SrcTableDTO;
import mycompany.nzr.dc.dto.TableChangeIndicatorsDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DataCaptureRepoDAO {

	@Autowired
	private JdbcTemplate jdbcTemplateOra;

	public long getBatchFilderSkey() {
		return this.jdbcTemplateOra.queryForLong("SELECT NZR_FOLDER_SEQ.nextval FROM dual");
	}
	public void insertBatchFile(String sql, long dcBatchSkey, long folderSkey,
			long dcBatchDtlSkey, String fileName, String fileFormat,
			Output fileContent, String fileLocation) {
		
		jdbcTemplateOra.update(sql, new Object[] { dcBatchSkey,
				folderSkey, dcBatchDtlSkey, fileName, fileFormat.toString(),
				fileContent.toString(), fileLocation });
	}
	
	public void insertBatchFolder(String sql, long folderSkey, long dcBatchSkey, String folderName) {
		jdbcTemplateOra.update(sql, new Object[] {folderSkey, dcBatchSkey, folderName});
	}
	
	public Map<String, Object> fetchDataCaptureSetConfig(String sql,
			long dcSetSkey) {
		return jdbcTemplateOra.queryForMap(sql, dcSetSkey);
	}

	public long getPreviousBatchSkey(String sql, long curBatchSkey, long dcSetSkey, long tableSkey) {
		return this.jdbcTemplateOra.queryForLong(sql, curBatchSkey, dcSetSkey, tableSkey);
	}
	// public List<Map<String, Object>> fetchTablesInDataCaptureSet(String sql,
	// long dcSetSkey) {
	public List<DcSetDetailDTO> fetchDataCaptureSetDetails(String sql,
			long dcSetSkey) {
		// return jdbcTemplateOra.queryForList(sql, dcSetSkey);
		Object[] params = new Object[] { dcSetSkey };
		return jdbcTemplateOra.query(sql, params,
				ParameterizedBeanPropertyRowMapper
						.newInstance(DcSetDetailDTO.class));
	}

	public void insertNewBatchInfo(String sql, long batchSkey, long dcSetSkey,
			long highxid, String status, BatchType batchType ) {
		Object[] params = new Object[] { batchSkey, dcSetSkey, highxid,
				status, batchType.toString()};
		jdbcTemplateOra.update(sql, params);
	}

	public void insertTableInfoWrk(String sql, long dataSetSkey,
			long batchSkey, List<SrcTableDTO> srcTables) {
		List<Object[]> params = new ArrayList<Object[]>();
		for (SrcTableDTO table : srcTables) {
			params.add(new Object[] { dataSetSkey, batchSkey,
					table.getTableSkey(), table.getTableName(),
					table.getCreateDate(), table.getObjId(),
					table.getRelnatts(), table.getDescription() });
		}
		jdbcTemplateOra.batchUpdate(sql, params);
	}

	public void insertColumnInfoWrk(String sql, long dataSetSkey,
			long batchSkey, List<SrcColumnDTO> srcColumns) {
		List<Object[]> params = new ArrayList<Object[]>();
		for (SrcColumnDTO column : srcColumns) {
			params.add(new Object[] { dataSetSkey, batchSkey,
					column.getTableSkey(), column.getColumnName(),
					column.getColumnPosition(), column.getDataType(),
					column.getNullable(), column.getDistPosition(),
					column.getOrgPosition(), column.getDefaultValue(),
					column.getColumnComment(), column.getDataLength(),
					column.getDataPrecision(), column.getDataScale() });
		}
		jdbcTemplateOra.batchUpdate(sql, params);
	}

	public void insertTableConstraintInfoWrk(String sql, long dataSetSkey,
			long batchSkey, List<SrcTableConstraintDTO> srcTableConstraints) {
		List<Object[]> params = new ArrayList<Object[]>();
		for (SrcTableConstraintDTO c : srcTableConstraints) {
			params.add(new Object[] { dataSetSkey, batchSkey, c.getTableSkey(),
					c.getConstraintName(), c.getConType(), c.getPkRelation() });
		}
		jdbcTemplateOra.batchUpdate(sql, params);
	}

	public void insertConstraintColumnsInfoWrk(String sql, long dataSetSkey,
			long batchSkey, List<SrcConstraintAttributeDTO> srcConstraintCols) {
		List<Object[]> params = new ArrayList<Object[]>();
		for (SrcConstraintAttributeDTO c : srcConstraintCols) {
			params.add(new Object[] { dataSetSkey, batchSkey, c.getTableSkey(),
					c.getConstraintName(), c.getAttName(), c.getConseq(), c.getPkAttName() });
		}
		jdbcTemplateOra.batchUpdate(sql, params);
	}

	public long getLowStableXID(String sql, long tableSkey, long curBatchSkey) {
		return jdbcTemplateOra.queryForLong(sql, tableSkey, curBatchSkey);
	}

	public long getNewBatchSkey(String sql) {
		return jdbcTemplateOra.queryForLong(sql);
	}

	public TableChangeIndicatorsDTO getTableChangeIndicators(String sql,
			 long prevBatchSkey, long curBatchSkey, String tableName) {

		Object[] params = new Object[] {prevBatchSkey, curBatchSkey , tableName};
		return jdbcTemplateOra.queryForObject(sql, params,
				ParameterizedBeanPropertyRowMapper
						.newInstance(TableChangeIndicatorsDTO.class));
	}

	public List<ColumnChangeIndicatorsDTO> getColumnChangeIndicators(
			String sql, long prevBatchSkey, long curBatchSkey, String tableName) {
		Object[] params = new Object[] { prevBatchSkey, curBatchSkey , tableName};
		return jdbcTemplateOra.query(sql, params,
				ParameterizedBeanPropertyRowMapper
						.newInstance(ColumnChangeIndicatorsDTO.class));
	}

	public long getNewBatchDtlSkey() {
		return jdbcTemplateOra
				.queryForLong("SELECT nzr_dc_batch_dtl_seq.nextval FROM dual");
	}

	public void insertIntoBatchDetail(String sql, long batchDtlSkey,
			String status, long dcSetSkey, long batchSkey, long srcObjId, long lowxid) {
		Object[] params = new Object[] { batchDtlSkey, dcSetSkey, batchSkey,
				srcObjId, status , lowxid};
		jdbcTemplateOra.update(sql, params);
	}

	public void updateBatchDetail(String sql, long batchDtlSkey, String status,
			long insertCount, long deleteCount, int ddlFlag) {
		Object[] params = new Object[] { status, insertCount, deleteCount,
				ddlFlag, batchDtlSkey};
		jdbcTemplateOra.update(sql, params);
	}

	/*
	 * public void updateBatchDetailFailure(String sql, long batchDtlSkey) {
	 * 
	 * jdbcTemplateOra.update(sql, batchDtlSkey); }
	 */
	public void updateBatchInfo(String sql, String status, long batchSkey) {
		Object[] params = new Object[] { status, batchSkey };
		jdbcTemplateOra.update(sql, params);
	}

	public List<Map<String, Object>> getPkCols(String sql, long tableSkey) {
		return jdbcTemplateOra.queryForList(sql, tableSkey);
	}

	public List<ScheduleDTO> getSchedules(String sql) {
		List l = jdbcTemplateOra.query(sql, ParameterizedBeanPropertyRowMapper
				.newInstance(ScheduleDTO.class));
		return l;
	}
	
	public List<OnDemandScheduleDTO> getOnDemandSchedules(String sql) {
		return jdbcTemplateOra.query(sql, ParameterizedBeanPropertyRowMapper
				.newInstance(OnDemandScheduleDTO.class));
	}
	
	public int moveWrkTableOrTableAttr(String sql, long batchDtlSkey, long tableSkey, long batchSkey) {
		return jdbcTemplateOra.update(sql, batchDtlSkey, tableSkey, batchSkey);
	}
	
	public Map<String, Object> moveWrkConstr(String sql, long batchDtlSkey,	long tableSkey, long batchSkey) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(sql, new int[]{java.sql.Types.BIGINT, java.sql.Types.BIGINT, java.sql.Types.BIGINT})
				.newPreparedStatementCreator(new Object[] { batchDtlSkey, tableSkey, batchSkey });
		jdbcTemplateOra.update(psc);//(psc, keyHolder);
		return keyHolder.getKeys();
	}
	
	public void moveWrkConstrAttr(String sql, long constrSkey, long batchDtlSkey, String constrName, long batchSkey) {
		jdbcTemplateOra.update(sql, constrSkey, batchDtlSkey, constrName, batchSkey);
	}
	
	public long getConstraintSkey() {
		return this.jdbcTemplateOra.queryForLong("SELECT nzr_constraint_seq.nextval FROM dual");
	}
	
	public List<ConstraintDTO> getConstraintNameAndSkey(String sql, long tableSkey, long batchSkey) {
		return jdbcTemplateOra.query(sql, new Object[]{tableSkey, batchSkey}, ParameterizedBeanPropertyRowMapper
				.newInstance(ConstraintDTO.class));
	}
	
	public void resetBatchStatus(String sql, Long dcSetSkey) {
			jdbcTemplateOra.update(sql, dcSetSkey);
	}
	
	public void resetBatchStatus_All(String sql) {
			jdbcTemplateOra.update(sql);
	}
		

	public List<ConfiguredSystem> getConfiguredSystemByDcSkey(String sql,
			long dcSetSkey) {
        return jdbcTemplateOra.query(sql,  new Object[]{dcSetSkey}, ParameterizedBeanPropertyRowMapper
				.newInstance(ConfiguredSystem.class));
         
	}
	
	public int getPreviousFullBatchCount(String sql, long curBatchSkey, long dcSkey, long tableSkey) {
		return this.jdbcTemplateOra.queryForInt(sql, new Object[]{curBatchSkey, dcSkey, tableSkey});
	}
	
	
}
