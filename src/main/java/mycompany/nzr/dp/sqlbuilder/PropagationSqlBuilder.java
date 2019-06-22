package mycompany.nzr.dp.sqlbuilder;

import java.util.Map;

import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;

import org.springframework.util.StringUtils;

public class PropagationSqlBuilder {

	private static String LOCALTEXT_PROPAGATION_SQL = "insertFromExternal_LocalText";
	private static String LOCALINTERNAL_PROPAGATION_SQL = "insertFromExternal_LocalInternal";
	private static String REMOTETEXT_PROPAGATION_SQL = "insertFromExternal_RemoteText";
	private static String REMOTEINTERNAL_PROPAGATION_SQL = "insertFromExternal_RemoteInternal";
	private static String PK_SQL = "pkByTableSql";
	private static String FK_BY_PK_TABLE_SQL = "fkByPkTableSql";
	private static String FK_BY_FK_TABLE_SQL = "fkByFkTableSql";
	private static String CHECK_TABLE_EXISTS_SQL = "checkTableExists_Sql";
	
//	private static String DELETE_FROM_TARGET_TABLE_SQL = "deleteFromTargetTable";
	
	private Map<Object, Object> sql;
	
	public String getCheckTableExistsSql() {
		return (String) sql.get(CHECK_TABLE_EXISTS_SQL);
	}

	private String replacePlaceholders(int tableCount, String sql_) {
        StringBuffer placeholdersLine = new StringBuffer();
		
		for (int i = 0 ; i < tableCount - 1; i++) {
		   placeholdersLine.append("?, ");
		}
		placeholdersLine.append("?");	
		return StringUtils.replace(sql_, "?", placeholdersLine.toString());
	}
	
	public String getPkByTableSql(int tableCount) {
		return replacePlaceholders(tableCount, (String) sql.get(PK_SQL));
	}
	
	public String getFkByFkTableSql(int tableCount) {
		return replacePlaceholders(tableCount,
				(String) sql.get(FK_BY_FK_TABLE_SQL));
	}
	
	public String getFkByPkTableSql(int tableCount) {
		return replacePlaceholders(tableCount,
				(String) sql.get(FK_BY_PK_TABLE_SQL));
	}

	public String getInsertFromExternal_LocalTextSql(String tableName) {
		return StringUtils.replace((String) sql.get(LOCALTEXT_PROPAGATION_SQL),
				"??", tableName);
	}

	public String getInsertFromExternal_LocalInternalSql(String tableName) {
		return StringUtils.replace(
				(String) sql.get(LOCALINTERNAL_PROPAGATION_SQL), "??",
				tableName);
	}

	public String getInsertFromExternal_RemoteText(String tableName) {
		return StringUtils.replace(
				(String) sql.get(REMOTETEXT_PROPAGATION_SQL), "??", tableName);
	}

	public String getInsertFromExternal_RemoteInternal(String tableName) {
		return StringUtils.replace(
				(String) sql.get(REMOTEINTERNAL_PROPAGATION_SQL), "??",
				tableName);
	}

	public Map<Object, Object> getSql() {
		return sql;
	}

	public String getDeleteFromTargetTableSql(String targetTable,
			String tempTable, String pkColsAsString) {
		String[] pkCols = StringUtils
				.commaDelimitedListToStringArray(pkColsAsString);
		// return (String) sql.get(DELETE_FROM_TARGET_TABLE_SQL);
		StringBuffer bfr = new StringBuffer();
		bfr
		        .append("DELETE FROM ").append(targetTable)
				.append(" WHERE rowid IN (SELECT tgt.rowid FROM ")
				.append(targetTable).append(" tgt INNER JOIN ")
				.append(tempTable).append(" dels ON (");

		for (int i = 0; i < pkCols.length; i++) {
			bfr.append("tgt").append(".").append(pkCols[i].trim());
			bfr.append("=");
			bfr.append("dels").append(".").append(pkCols[i].trim());
			bfr.append(" AND ");
		}
		bfr.delete(bfr.lastIndexOf("AND"), bfr.lastIndexOf("AND") + 3);
		bfr.append("))");
		return bfr.toString();
	}

	public String getDropConstraintSql(CreateConstraintColumnDTO dto) {
		return new StringBuffer("ALTER TABLE ").append(dto.getTableName())
				.append(" DROP CONSTRAINT ").append(dto.getConstraintName())
				.toString();
	}
	
	public void setSql(Map<Object, Object> sql) {
		this.sql = sql;
	}
	
	public static void main(String args[]) {
		//String sql = new PropagationSqlBuilder().getDeleteFromTargetTableSql("targettbl", "tempTbl", "pl1, pk2, pk3");
		
		String sql = "select * from tbl where col in (?)";
	    System.out.print(new PropagationSqlBuilder().replacePlaceholders(5, sql));
		
		//System.out.print(StringUtils.replace("select * from tbl where tblname in (?)", "?", "?, ?, ?"));
	}

}
