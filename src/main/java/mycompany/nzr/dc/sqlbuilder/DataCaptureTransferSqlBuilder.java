package mycompany.nzr.dc.sqlbuilder;

import java.util.Map;

public class DataCaptureTransferSqlBuilder {

	private Map<Object, Object> sql;

	private static String LOCAL_TEXT_FORMAT_SQL = "localTextFormatSQL";
	private static String REMOTE_TEXT_FORMAT_SQL = "remoteTextFormatSQL";
	private static String LOCAL_INTERNAL_FORMAT_SQL = "localInteralFormatSQL";
	private static String REMOTE_INTERNAL_FORMAT_SQL = "remoteInteralFormatSQL";

	// Inserts Sql:
	public String getDataCaptureLocalTextInsertsSql(String srcTable) {
		String _sql = (String) sql.get(LOCAL_TEXT_FORMAT_SQL);
		return getInsertsStmt(_sql, srcTable);
	}

	public String getDataCaptureRemoteTextInsertsSql(String srcTable) {
		String _sql = (String) sql.get(REMOTE_TEXT_FORMAT_SQL);
		return getInsertsStmt(_sql, srcTable);
	}

	public String getDataCaptureLocalInternalInsertsSql(String srcTable) {
		String _sql = (String) sql.get(LOCAL_INTERNAL_FORMAT_SQL);
		return getInsertsStmt(_sql, srcTable);
	}

	public String getDataCaptureRemoteInternalInsertsSql(String srcTable) {
		String _sql = (String) sql.get(REMOTE_INTERNAL_FORMAT_SQL);
		return getInsertsStmt(_sql, srcTable);
	}

	//Deletes Sql:
	
	public String getDataCaptureLocalTextDeletesSql(String srcTable, String pkColsString) {
		String _sql = (String) sql.get(LOCAL_TEXT_FORMAT_SQL);
		return getDeletesStmt(_sql, srcTable, pkColsString);
	}

	public String getDataCaptureRemoteTextDeletesSql(String srcTable, String pkColsString) {
		String _sql = (String) sql.get(REMOTE_TEXT_FORMAT_SQL);
		return getDeletesStmt(_sql, srcTable, pkColsString);
	}

	public String getDataCaptureLocalInternalDeletesSql(String srcTable, String pkColsString) {
		String _sql = (String) sql.get(LOCAL_INTERNAL_FORMAT_SQL);
		return getDeletesStmt(_sql, srcTable, pkColsString);
	}

	public String getDataCaptureRemoteInternalDeletesSql(String srcTable, String pkColsString) {
		String _sql = (String) sql.get(REMOTE_INTERNAL_FORMAT_SQL);
		return getDeletesStmt(_sql, srcTable, pkColsString);
	}
	
	private String getInsertsStmt(String sql, String tableName) {
		StringBuffer sb = new StringBuffer(sql);
		sb.append("SELECT * FROM ").append(tableName).toString();
		sb.append(" WHERE Mod(datasliceid, ?) = ? AND createxid > ? AND createxid < ?");
		return sb.toString();
	}
	
	private String getDeletesStmt (String sql,  String tableName, String pkCols) {
		StringBuffer bfr = new StringBuffer(sql);
		bfr.append("SELECT ").append(pkCols).append(" FROM ")
		   .append(tableName).append(" WHERE deletexid > ? AND deletexid < ? AND createxid <?");
		return bfr.toString();
	}
	
	public Map<Object, Object> getSql() {
		return sql;
	}

	public void setSql(Map<Object, Object> sql) {
		this.sql = sql;
	}

}
