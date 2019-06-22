package mycompany.nzr.dc.sqlbuilder;

import java.util.Map;


public class DataCaptureSrcSqlBuilder {
	
	private static String HIGH_STABLE_XID_SQL = "highStableXidSql";
	private static String TABLE_ROW_COUNT_SQL = "tableRowCount";
	
	private Map<Object, Object> sql;
	public static String EOL = System.getProperty("line.separator");

	public Map<Object, Object> getSql() {
		return sql;
	}

	public void setSql(Map<Object, Object> sql) {
		this.sql = sql;
	}
	
	public String getSelectTableRowCount() {
		return (String)sql.get(TABLE_ROW_COUNT_SQL);
	}
	
	public String getHighStableXid() {
		return (String) sql.get(HIGH_STABLE_XID_SQL);
	}

	public String getSelectTableInfoSQL(int tableCount) {
		StringBuffer sb = new StringBuffer();

		sb
		.append("SELECT ").append(EOL)
		.append("TABLENAME,").append(EOL)
		.append("CREATEDATE,").append(EOL)
		.append("OBJID,").append(EOL)
		.append("RELNATTS,").append(EOL)
		.append("DESCRIPTION")	.append(EOL)
		.append("FROM ")	.append(EOL)
		.append("_V_TABLE")	.append(EOL)
		.append("WHERE").append(EOL)
		.append("TABLENAME in (");

		for (int i = 0; i < tableCount; i++) {
			sb.append("?").append(", ");
		}
		sb.deleteCharAt(sb.length() - 2);
		sb.append(")");
		return sb.toString();
	}
	
	public String getSelectStableXidSQL() {
		StringBuffer sb = new StringBuffer();

		sb
		.append("select ").append(EOL)
		.append("stable_xid ").append(EOL)
	    .append("from ").append(EOL)
		.append("_VT_HOSTTXMGR").append(EOL)
	;
        return sb.toString();
	}
	
	public String getSelectColumnInfoSQL(int tableCount ) {
		StringBuffer sb = new StringBuffer();

		sb
		.append("select").append(EOL)
	    .append("c.attcolleng as data_length, c.OBJID,").append(EOL)
	    .append("c.NAME as TABLE_NAME,").append(EOL)
	    .append("c.ATTNAME as COLUMN_NAME,").append(EOL)
	    .append("c.FORMAT_TYPE as DATATYPE,").append(EOL)
	    .append("case istrue(c.ATTNOTNULL) ").append(EOL)
	    .append("    when true then 0 ").append(EOL)
	    .append("   else 1 ").append(EOL)
	    .append("end as NULLABLE,").append(EOL)
	    .append("c.COLDEFAULT as DEFAULT_VALUE,").append(EOL)
	    .append("c.DESCRIPTION as COLUMN_COMMENT,").append(EOL)
	    .append("d.DISTSEQNO as DIST_POSITION,").append(EOL)
	    .append("c.ATTNUM as COLUMN_POSITION,").append(EOL)
	    .append("o.ORGSEQNO as ORG_POSITION").append(EOL)
	    .append("from").append(EOL)
	    .append("    _V_RELATION_COLUMN c").append(EOL) 
	    .append("left join").append(EOL)
	    .append("_V_TABLE_DIST_MAP d").append(EOL) 
	    .append("   on d.OBJID = c.OBJID ").append(EOL)
	    .append("   and d.ATTNUM = c.ATTNUM ").append(EOL)
	    .append("left join").append(EOL)
	    .append(" _V_TABLE_ORGANIZE_COLUMN o").append(EOL) 
	    .append("    on o.OBJID = c.OBJID ").append(EOL)
	    .append("    and o.ATTNUM = c.ATTNUM ").append(EOL)
	    .append("where").append(EOL)
	    .append("c.NAME IN(");
		
		for (int i = 0; i < tableCount; i++) {
			sb.append("?").append(", ");
		}
		sb.deleteCharAt(sb.length() - 2);
		sb.append(")").append(EOL) 
	    .append("order by").append(EOL)
	    .append("  c.OBJID,").append(EOL)
	    .append("  c.ATTNUM").append(EOL)
	;
        return sb.toString();
	}
	
	public String getSelectTableConstraintInfoSQL(int tableCount) {
		StringBuffer sb = new StringBuffer();
		sb
		.append("select distinct").append(EOL)
		.append("RELATION,").append(EOL)
		.append("CONSTRAINTNAME,").append(EOL)
		.append("CONTYPE,").append(EOL)
		.append("PKRELATION").append(EOL)
	    .append("from").append(EOL)
		.append("_V_RELATION_KEYDATA").append(EOL)
	    .append("where").append(EOL)
		.append("RELATION in (").append(EOL);
		
		for (int i = 0; i < tableCount; i++) {
			sb.append("?").append(", ");
		}
		sb.deleteCharAt(sb.length() - 2);
		sb.append(")").append(EOL) ;
		return sb.toString();
	}
	
	public String getSelectConstraintColsInfoSQL(int tableCount) {
		StringBuffer sb = new StringBuffer();
		
		sb
		.append("SELECT ").append(EOL)
		.append("RELATION as tableName,").append(EOL)
		.append("CONSTRAINTNAME,").append(EOL)
		.append("ATTNAME,").append(EOL)
		.append("CONSEQ,").append(EOL)
		.append("PKATTNAME").append(EOL)
	    .append("from").append(EOL)
		.append("_V_RELATION_KEYDATA").append(EOL)
	    .append("where").append(EOL)
		.append("RELATION in (").append(EOL);
		
		for (int i = 0; i < tableCount; i++) {
			sb.append("?").append(", ");
		}
		sb.deleteCharAt(sb.length() - 2);
		sb.append(")").append(EOL) ;
		
		return sb.toString();
	}
	

	
	 public static void main(String a[]) {
		  //System.out.print(new  DataCaptureSrcSqlBuilder().getSelectConstraintColsInfoSQL(2));
		  System.out.print(new  DataCaptureSrcSqlBuilder().getSelectColumnInfoSQL(1));
	   }

}
