package mycompany.nzr.dp.sqlbuilder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class RepoCommonSqlBuilder {

	private Map<Object, Object> sql;
	
	private static String GET_PK_COLS_LIST_SQL = "pkColsListSql";
	private static String GET_PK_COLS_DETAILS_SQL = "pkColsDetailsSql";
	private static String BATCH_FILE_TO_PROPAGATE_SQL = "batchFileToPropagateSql";
	private static String BATCH_TO_PROPAGATE_SQL = "batchToPropagateSql";
	private static String START_BATCH_SQL = "startBatch_Sql";
	private static String END_BATCH_SQL = "endBatch_Sql";
	private static String START_BATCH_TABLE_SQL = "startBatchTable_Sql";
	private static String UPDATE_BATCH_TABLE_SQL = "updateBatchTable_Sql";
	private static String END_BATCH_TABLE_SQL = "endBatchTableSql";
	private static String BATCH_PK_VALUE_SQL = "batchPkValueSql";
	private static String BATCH_DETAIL_PK_VALUE_SQL = "batchDetailPkValueSql";
	private static String TARGET_SYSTEMS_WITH_PROPAGATABLE_BATCHES = "targetSystemsWithPropagatableBatchesSql";
	
	
	public String getTargetSystemsWithPropagatableBatchesSql () {
		return (String) sql.get(TARGET_SYSTEMS_WITH_PROPAGATABLE_BATCHES);
	}
	
	public String getBatchPkValueSql() {
		return (String) sql.get(BATCH_PK_VALUE_SQL);
	}

	public String getBatchDetailPkValueSql() {
		return (String) sql.get(BATCH_DETAIL_PK_VALUE_SQL);
	}

	public String getStartBatchSql() {
		return (String) sql.get(START_BATCH_SQL);
	}

	public String getEndBatchSql() {
		return (String) sql.get(END_BATCH_SQL);
	}

	public String getStartBatchTableSql() {
		return (String) sql.get(START_BATCH_TABLE_SQL);
	}

	public String getUpdateBatchTableSql() {
		return (String) sql.get(UPDATE_BATCH_TABLE_SQL);
	}

	public String getEndBatchTableSql() {
		return (String) sql.get(END_BATCH_TABLE_SQL);
	}

	public String getBatchFilesToPropagateSql() {
		return (String) sql.get(BATCH_FILE_TO_PROPAGATE_SQL);
	}

	public String getBatchToPropagateSql() {
		return (String) sql.get(BATCH_TO_PROPAGATE_SQL);
	}

	public String getPkColsListSql() {
		return (String) sql.get(GET_PK_COLS_LIST_SQL);
	}

	public String getPkColsDetailsSql() {
		return (String) sql.get(GET_PK_COLS_DETAILS_SQL);
	}

	public Map<Object, Object> getSql() {
		return sql;
	}

	public void setSql(Map<Object, Object> sql) {
		this.sql = sql;
	}

}