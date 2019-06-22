package mycompany.nzr.dp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mycompany.nzr.common.BatchDetailStatus;
import mycompany.nzr.common.BatchStatus;
import mycompany.nzr.common.BatchType;
import mycompany.nzr.common.ConfiguredSystem;
import mycompany.nzr.dc.dto.CreateTableDTO;
import mycompany.nzr.dp.Action;
import mycompany.nzr.dp.dto.CapturedFile;
import mycompany.nzr.dp.dto.PropagatableBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

public class RepoDAO {

	@Autowired
	private JdbcTemplate jdbcTemplateOra;

	public List<Map<String, Object>> getPkCols(String sql, long tableSkey,
			long batchSkey) {
		return jdbcTemplateOra.queryForList(sql, tableSkey);
	}

	public List<CreateTableDTO> getTempTableCols(String sql, long tableSkey,
			long dcBatchSkey) {
		// ParameterizedBeanPropertyRowMapper.newInstance(DcSetDetailDTO.class)
		return jdbcTemplateOra.query(sql,
				new Object[] { tableSkey, dcBatchSkey },
				ParameterizedBeanPropertyRowMapper
						.newInstance(CreateTableDTO.class));
	}

	public List<CapturedFile> getBatchFilesToPropagate(String sql,
			long batchSkey) {
		return jdbcTemplateOra.query(sql, new Object[] { batchSkey },
				ParameterizedBeanPropertyRowMapper
						.newInstance(CapturedFile.class));
	}

	public List<PropagatableBatch> getBatchToPropagate(String sql,
			long hostSkey, String srcDatabaseName) {
		return jdbcTemplateOra.query(sql, new Object[] { hostSkey,
				srcDatabaseName }, ParameterizedBeanPropertyRowMapper
				.newInstance(PropagatableBatch.class));
	}

	public long getNextvalFromSeq(String sql) {
		return jdbcTemplateOra.queryForInt(sql);
	}

	public void startBatch(String sql, long dpBatchSkey, long subscriptionSkey,
			long dcSkey, long dcBatchSkey, BatchStatus dpBatchStatus,
			BatchType dpBatchType) {
		jdbcTemplateOra.update(sql, dpBatchSkey, subscriptionSkey, dcSkey,
				dcBatchSkey, dpBatchStatus.toString(), dpBatchType.toString());
	}

	public void endBatch(String sql, BatchStatus status, long dpBatchSkey) {
		jdbcTemplateOra.update(sql, new Date(), status.name(), dpBatchSkey);
	}

	public void startBatchTable(String sql, long dpBatchDtlSkey,
			long dpBatchSkey, long subscriptionSkey, long dcBatchSkey,
			long dcBatchDtlSkey, long dstObjectId, long tableSkey,
			BatchDetailStatus dpBatchDtlStatus, int actionCode) {

		jdbcTemplateOra.update(sql, dpBatchDtlSkey, dpBatchSkey,
				subscriptionSkey, dcBatchSkey, dcBatchDtlSkey, dstObjectId,
				tableSkey, dpBatchDtlStatus.name(), actionCode);
	}

	public void updateBatchTable(String sql, int insertCnt, int deleteCnt,
			int ddlFlag, int actionCode, long dpBatchDtlSkey) {
		jdbcTemplateOra.update(sql, insertCnt, deleteCnt, ddlFlag, actionCode,
				dpBatchDtlSkey);
	}

	public void endBatchTable(String sql, BatchDetailStatus dpBatchDtlStatus,
			Action action, long dpBatchDtlSkey) {
		jdbcTemplateOra.update(sql, dpBatchDtlStatus.name(), action.getCode(),
				dpBatchDtlSkey);
	}

	public List<ConfiguredSystem> getTargetSystemsWithPropagatableBatches(
			String sql) {
		return jdbcTemplateOra.query(sql, ParameterizedBeanPropertyRowMapper
				.newInstance(ConfiguredSystem.class));
	}

}
