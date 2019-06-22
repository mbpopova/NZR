package mycompany.nzr.dc.sqlbuilder;

import java.util.Map;

public class DataCaptureDdlSourceSqlBuilder {

	private Map<Object, Object> sql;

	private String CREATE_FK_CONSTRAINT_SQL = "createFKConstraintSQL";
	private String EXTEND_VARCHAR_COLUMN_SQL = "extendVarcharColumnSQL";
	private String PRIMARY_KEY_CONTRAINT_SQL = "primaryKeyConstraintSQL";
	private String RENAME_COLUMN_SQL = "renameColumnSQL";
	private String TABLE_SQL = "tableSQL";
	private String UNIQUE_KEY_CONSTRAINT_SQL = "uniqueKeyConstraintSQL";
	private String UPDATE_COLUMN_COMMENT_SQL = "updateColumnCommentSQL";
	private String UPDATE_DEFAULT_VALUE_SQL = "updateDefaultValueSQL";
	private String UPDATE_ORGANIZEON_SQL = "updateOrganizeOnSQL";
	private String UPDATE_TABLE_COMMENT_SQL = "updateTableCommentSQL";
	
	private static String CREATE_PKS_FOR_TABLE_SQL = "createPKsForTable_Sql";
	private static String CREATE_FKS_FOR_TABLE_SQL = "createFKsForTable_Sql";
	private static String CREATE_UCS_FOR_TABLE_SQL = "createUniqueConstrsForTable_Sql";
	

	
	public String getCreatePKsForTable_Sql() {
		return (String)sql.get(CREATE_PKS_FOR_TABLE_SQL);
	}

	public String getCreateUniqueConstrantsForTable_Sql() {
		return (String)sql.get(CREATE_UCS_FOR_TABLE_SQL);
	}
	
	public String getCreateFKsForTable_Sql() {
		return (String)sql.get(CREATE_FKS_FOR_TABLE_SQL);
	}
	
	public String getCreateFKConstraintSQL() {
		return (String)sql.get(CREATE_FK_CONSTRAINT_SQL);
	}
	
	public String getExtendVarcharColumnSQL() {
		return (String)sql.get(EXTEND_VARCHAR_COLUMN_SQL);
	}
	
	public String getCreatePKConstraintSQL() {
		return (String)sql.get(PRIMARY_KEY_CONTRAINT_SQL);
	}
	
	public String getRenameColumnSQL() {
		return (String)sql.get(RENAME_COLUMN_SQL);
	}
	public String getTableSQL() {
		return (String)sql.get(TABLE_SQL);
	}
	
	public String getCreateUniqueKeyConstraintSQL() {
		return (String)sql.get(UNIQUE_KEY_CONSTRAINT_SQL);
	}
	public String getUpdateColumnCommentSQL () {
		return (String)sql.get(UPDATE_COLUMN_COMMENT_SQL);
	}
	public String getUpdateDefaultValueSQL() {
		return (String)sql.get(this.UPDATE_DEFAULT_VALUE_SQL);
	}
	
	public String getUpdateOrganizeOnSQL() {
		return (String)sql.get(UPDATE_ORGANIZEON_SQL);
	}
	
	public String getUpdateTableCommentSQL() {
		return (String)sql.get(UPDATE_TABLE_COMMENT_SQL);
	}
	
	public Map<Object, Object> getSql() {
		return sql;
	}
	
	public void setSql(Map<Object, Object> sql) {
		this.sql = sql;
	}
	
}
