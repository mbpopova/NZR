package mycompany.nzr.dc.sqlbuilder;

import java.util.Map;

public class DataCaptureRepoSqlBuilder {

    private static String LOW_STABLE_XID_SQL = "lowStableXidSql";
	private static String TABLE_CHANGED_IND_SQL = "tableChangedIndSql";
	private static String TABLE_ORG_CHANGED_IND_SQL = "tableOrgChangedIndSql";
	private static String COLUMNS_CHANGED_IND_SQL = "columnsChangedIndSql";
	private static String INSERT_BATCH_DETAIL_SQL = "insertBatchDetailInfoSql";
	private static String UPDATE_BATCH_DETAIL_SQL = "updateBatchDetailSql";
	private static String UPDATE_BATCH_INFO_SQL = "updateBatchInfoSql";
    private static String SELECT_ALL_SCHEDULES_SQL = "selectAllSchedulesSql";
    private static String SELECT_ON_DEMAND_SCHEDULES_SQL = "selectOnDemandSchedulesSql";
	private static String MOVE_WRK_TABLE_SQL = "moveWrkTable_Sql";
	private static String MOVE_WRK_TABLE_ATTR_SQL = "moveWrkTableAttr_Sql";
	private static String MOVE_WRK_CONSTR_SQL = "moveWrkConstr_Sql";
	private static String MOVE_WRK_CONSTR_ATTR_SQL = "moveWrkConstrAttr_Sql";
	private static String CONSTR_NAME_AND_SKEY_SQL = "getConstrNameAndSkey_Sql";
	
	private static String PREV_BATCH_SQL = "previousBatch_Sql";
	private static String COUNT_PREV_FULL_BATCH_SQL = "countPreviousFullBatch_Sql";
	private static String INSERT_BATCH_FOLDER = "insertBatchFolder_Sql";
	private static String INSERT_BATCH_FILE = "insertBatchFile_Sql";
	private static String RUNNING_ERROR_BATCH_STATUS_SQL = "runningToErrorBatchStatus_Sql";
	private static String RUNNING_ERROR_BATCH_STATUS_ALL_SQL = "runningToErrorBatchStatusAllSets_Sql";
	
	private static String RUNNING_ERROR_BATCHDETAIL_STATUS_SQL = "runningToErrorBatchDetailStatus_Sql";
	private static String RUNNING_ERROR_BATCHDETAIL_STATUS_ALL_SQL = "runningToErrorBatchDetailStatusAllSets_Sql";
	
	
	private static String CONFIGURED_SYSTEM_BY_DC_SKEY = "configuredSystemByDcSkey_Sql";
	
	public String getCountPrevFullBatch_Sql() {
		return (String) sql.get(COUNT_PREV_FULL_BATCH_SQL);
	}

	public String getOnDemandSchedules_Sql() {
		return (String) sql.get(SELECT_ON_DEMAND_SCHEDULES_SQL);
	}

	public String getRunningToErrorBatchDetailStatusAllSets_Sql() {
		return (String) sql.get(RUNNING_ERROR_BATCHDETAIL_STATUS_ALL_SQL);
	}

	public String getRunningToErrorBatchDetailStatus_Sql() {
		return (String) sql.get(RUNNING_ERROR_BATCHDETAIL_STATUS_SQL);
	}
	
	public String getConfiguredSystemByDcSkey_Sql() {
		return (String)sql.get(CONFIGURED_SYSTEM_BY_DC_SKEY);
	}
	
	public String getRunningToErrorBatchStatusAllSets_Sql() {
		return (String) sql.get(RUNNING_ERROR_BATCH_STATUS_ALL_SQL);
	}


	public String getRunningToErrorBatchStatus_Sql() {
		return (String) sql.get(RUNNING_ERROR_BATCH_STATUS_SQL);
	}
	public String getInsertBatchFolder_Sql() {
		return (String) sql.get(INSERT_BATCH_FOLDER);
	}
	
	public String getInsertBatchFile_Sql() {
		return (String) sql.get(INSERT_BATCH_FILE);
	}
	
	//@Autowired
	private Map<Object, Object> sql;
	
	public static String EOL = System.getProperty("line.separator");
	
	private static String GET_PK_COLS_SQL = "getPKColsSql";
	

	public String getPreviousBatchSql() {
		return (String) sql.get(PREV_BATCH_SQL);
	}
	public String getConstrNameAndSkey_Sql() {
		return (String) sql.get(CONSTR_NAME_AND_SKEY_SQL);
	}
	public String getMoveWrkTableAttr_Sql() {
		return (String) sql.get(MOVE_WRK_TABLE_ATTR_SQL);
	}
	
	public String getMoveWrkConstr_Sql() {
		return (String) sql.get(MOVE_WRK_CONSTR_SQL);
	}
	
	public String getMoveWrkConstrAttr_Sql() {
		return (String) sql.get(MOVE_WRK_CONSTR_ATTR_SQL);
	}
	
	public String getMoveWrkTable_Sql() {
		return (String) sql.get(MOVE_WRK_TABLE_SQL);
	}
	
	public String getSelectAllSchedulesSql() {
		return (String) sql.get(SELECT_ALL_SCHEDULES_SQL);
	}
	
	public String getPkColsSql() {
		return (String) sql.get(GET_PK_COLS_SQL);
	}
		
	public String getUpdateBatchInfoSql() {
		return (String)sql.get(UPDATE_BATCH_INFO_SQL);
	}
	public String getUpdateBatchDetailSql() {
		return (String) sql.get(UPDATE_BATCH_DETAIL_SQL);
	}	
	
	
	public String getInsertBatchDetailSql() {
		return (String) sql.get(INSERT_BATCH_DETAIL_SQL);
	}
	
	public String getLowStableXid() {
		return (String) sql.get(LOW_STABLE_XID_SQL);
	}

	public String getTableChangedIndSql() {
		return (String) sql.get(TABLE_CHANGED_IND_SQL);
	}

	public String getTableOrgChangedIndSql() {
		return (String) sql.get(TABLE_ORG_CHANGED_IND_SQL);
	}

	public String getColumnChangedIndSql() {
		return (String) sql.get(COLUMNS_CHANGED_IND_SQL);
	}

	public String getSelectDataCaptureSetInfoSQL() {
		StringBuffer sb = new StringBuffer();

		sb.append("select").append(EOL)
		.append("DC.DC_SKEY,").append(EOL)
				.append("DC.SRC_DB_NAME,").append(EOL)
				.append("DC.STG_FORMAT,").append(EOL)
				.append("DC.STG_LOCATION,").append(EOL)
				.append("DC.ENABLED_FLAG,").append(EOL)
				.append("H.DNS_IP,")
				.append(EOL).append("H.HOST_PORT,")
				.append(EOL).append("H.USERNAME,").append(EOL)
				.append("H.PASSWORD").append(EOL)
				.append("from").append(EOL)
				.append("NZR_DC_SET DC").append(EOL)
				.append("inner join").append(EOL)
				.append("NZR_HOST H").append(EOL)
				.append("on").append(EOL)
				.append("H.HOST_SKEY = DC.SRC_HOST_SKEY").append(EOL)
				.append("where").append(EOL)
				//.append("DC.REP_SET_NAME = ?");
				.append("DC.DC_SKEY = ?");

		return sb.toString();
	}
  
  public String getSelectTablesInDataCaptureSetSQL() {
	  StringBuffer sb = new StringBuffer(); 
	   
	   sb
	  .append("SELECT").append(EOL)
	  .append("DCD.TABLE_SKEY,").append(EOL)
	  .append("DCD.TABLE_NAME ").append(EOL)
	  .append("FROM").append(EOL)
	  .append("NZR_DC_DETAIL DCD").append(EOL)
	  .append("WHERE").append(EOL)
	  .append("DCD.DC_SKEY = ?").append(EOL)
	  .append("AND DCD.ENABLED_FLAG = 1").append(EOL);
	   return sb.toString();
  }
  
  public String getInsertBatchInfoSQL() {
	  StringBuffer sb = new StringBuffer(); 
	   
	   sb.append("insert into NZR_DC_BATCH").append(EOL)
		.append("(").append(EOL)
		.append("DC_BATCH_SKEY,").append(EOL)
	    .append("DC_SKEY,").append(EOL)
	  //  .append("LOW_STABLE_XID,").append(EOL)
	    .append("HIGH_STABLE_XID,").append(EOL)
	    .append("DC_BATCH_START_TS,").append(EOL)
	    .append("DC_BATCH_END_TS,").append(EOL)
	    .append("DC_BATCH_STATUS,").append(EOL)
	    .append("DC_BATCH_TYPE").append(EOL)
	    .append(" )").append(EOL)
	    .append(" values(").append(EOL)
	    .append("?").append(", ").append(EOL)
	   // .append(" 	NZR_DC_BATCH_SEQ.NEXTVAL as NZR_DC_BATCH_SKEY,").append(EOL)
	    .append("?,").append(EOL)
	    .append("?,").append(EOL)
	  //  .append("?,").append(EOL)
	    .append("sysdate,").append(EOL)
	    .append("null,").append(EOL)
	    .append("?,").append(EOL)
	     .append("?)").append(EOL);
	    //.append(" from").append(EOL)
	   // .append(" 	dual").append(EOL);
	 //  returning NZR_DC_BATCH_SKEY
	    return sb.toString();
  }
  
  public String getBatchSkeySQL() {
	  return "SELECT nzr_dc_batch_seq.NEXTVAL FROM dual";
  }
  
  public String getInsertTableInfoSQL() {
	  StringBuffer sb = new StringBuffer(); 
	   
	   sb.append("insert into NZR_WRK_TABLE").append(EOL)
	  .append("(").append(EOL)
	  .append("DC_SKEY,").append(EOL)
	  .append("DC_BATCH_SKEY,").append(EOL)
	  .append("TABLE_SKEY,").append(EOL)
	  .append("TABLE_NAME,").append(EOL)
	  .append("LAST_DDL_TS,").append(EOL)
	  .append("SRC_OBJECT_ID,").append(EOL)
	  .append("COLUMN_CNT,").append(EOL)
	  .append("TABLE_COMMENT").append(EOL)
	  .append(")").append(EOL)
	  .append(" values").append(EOL)
	  .append("(").append(EOL)
	  .append("?,").append(EOL)
	  .append("?,").append(EOL)
	  .append("?,").append(EOL)
	  .append("?,").append(EOL)
	  .append("to_timestamp(?,'YYYY-MM-DD HH24:MI:SS'),").append(EOL)
	  .append("?,").append(EOL)
	  .append("?,").append(EOL)
	  .append("?").append(EOL)
	  .append(")").append(EOL)
	  ;
      return sb.toString();
  }
  
  public String getInsertColumnInfoSQL() {
	  StringBuffer sb = new StringBuffer(); 
	   
	   sb.append("insert into	NZR_WRK_TABLE_ATTR").append(EOL)
	    .append("(").append(EOL)
	  	.append("  DC_SKEY,").append(EOL)
	  	.append("  DC_BATCH_SKEY,").append(EOL)
	  	.append("  TABLE_SKEY,").append(EOL)
	  	.append("  ATTRIBUTE_NAME,").append(EOL)
	  	.append("  ATTRIBUTE_POSITION,").append(EOL)
	  	.append("  DATATYPE,").append(EOL)
	  	.append("  NULLABLE,").append(EOL)
	  	.append("  DIST_POSITION,").append(EOL)
	  	.append("  ORG_POSITION,").append(EOL)
	  	.append("  DEFAULT_VALUE,").append(EOL)
	  	.append("  COLUMN_COMMENT,").append(EOL)	  	
	  	.append("  DATA_LENGTH,").append(EOL)
	  	.append("  DATA_PRECISION, ").append(EOL)
	  	.append("  DATA_SCALE").append(EOL)	  	
	    .append(")").append(EOL)
	    .append(" values").append(EOL)
	    .append("(").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("   ?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	  	.append("   ?").append(EOL)
	    .append(")").append(EOL)
	  ;
	   return sb.toString();
  }
  
  public String getInsertTableConstraintInfoSQL() {
	  StringBuffer sb = new StringBuffer(); 	   
	   sb
	   .append("insert into	NZR_WRK_CONSTRAINTS").append(EOL)
       .append("(").append(EOL)
  	   .append("DC_SKEY,").append(EOL)
  	   .append("DC_BATCH_SKEY,").append(EOL)
  	   .append("TABLE_SKEY,").append(EOL)
  	   .append("CONSTRAINT_NAME,").append(EOL)
  	   .append("CONSTRAINT_TYPE,").append(EOL)
  	   .append("PK_TABLE_NAME").append(EOL)
       .append(")").append(EOL)
       .append("values").append(EOL)
       .append("(").append(EOL)
  	   .append("?,").append(EOL)
  	   .append("?,").append(EOL)
  	   .append("?,").append(EOL)
  	   .append("?,").append(EOL)
  	   .append("?,").append(EOL)
  	   .append("?").append(EOL)
       .append(")").append(EOL);
	   
       return sb.toString();
  }
  
  public String getInsertConstraintColumnInfoSQL() {
	  StringBuffer sb = new StringBuffer(); 	   
	   sb
	   .append("insert into	NZR_WRK_CONSTRAINT_ATTR").append(EOL)
	   .append("(").append(EOL)
	   .append("DC_SKEY,").append(EOL)
	   .append("DC_BATCH_SKEY,").append(EOL)
	   .append("TABLE_SKEY,").append(EOL)
	   .append("CONSTRAINT_NAME,").append(EOL)
	   .append("ATTRIBUTE_NAME,").append(EOL)
	   .append("CONSTRAINT_ATTRIBUTE_POSITION, ").append(EOL)
	    .append("pk_attribute_name").append(EOL)
	   .append(")").append(EOL)
	   .append(" values").append(EOL)
	   .append(" (").append(EOL)
	   .append(" 	?,").append(EOL)
	   .append("	?,").append(EOL)
	   .append("	?,").append(EOL)
	   .append("	?,").append(EOL)
	   .append("	?,").append(EOL)
	    .append("	?,").append(EOL)
	   .append("	?").append(EOL)
	   .append(" )").append(EOL);
	   
	   return sb.toString();
  }
  
  public String getSelectObjectsWithDDLChangesSQL() {
	  StringBuffer sb = new StringBuffer(); 	   
	   sb
	    .append("select").append(EOL)
		.append("W.TABLE_SKEY,").append(EOL)
		.append("W.TABLE_NAME").append(EOL)
	    .append("from").append(EOL)
		.append("NZR_WRK_TABLE W").append(EOL)
	    .append("inner join").append(EOL)
	 	.append("NZR_DC_DETAIL D").append(EOL)
	    .append("on").append(EOL)
		.append("(").append(EOL)
		.append("D.DC_SKEY = W.DC_SKEY").append(EOL)
		.append("and D.TABLE_SKEY = W.TABLE_SKEY").append(EOL)
		.append("and D.TABLE_NAME = W.TABLE_NAME").append(EOL)
		.append("and D.LAST_DDL_TS <> W.LAST_DDL_TS").append(EOL)
		.append(")").append(EOL)
	    .append("where").append(EOL)
		.append("W.DC_SKEY = ?").append(EOL)
		.append("and W.DC_BATCH_SKEY = ?").append(EOL)
	;

	   return sb.toString();
  }
  
  public String getSelectObjectsWithTableAttrChangesSQL() {
	  StringBuffer sb = new StringBuffer(); 	   
	   sb
	    .append("select").append(EOL)
	    .append("W.TABLE_SKEY,").append(EOL)
	    .append("W.ATTRIBUTE_POSITION").append(EOL)
	    .append("from").append(EOL)
	    .append("NZR_WRK_TABLE_ATTR W").append(EOL)
	    .append("inner join").append(EOL)
	    .append("NZR_DC_DETAIL_ATTRIBUTES D").append(EOL)
	    .append("on").append(EOL)
	    .append("(").append(EOL)
	    .append(" D.TABLE_SKEY = W.TABLE_SKEY").append(EOL)
	    .append("and D.ATTRIBUTE_POSITION = W.ATTRIBUTE_POSITION").append(EOL)
	    .append("and").append(EOL)
	    .append(" (").append(EOL)
	    .append("  D.ATTRIBUTE_NAME != W.ATTRIBUTE_NAME").append(EOL)
	    .append("  or D.DATATYPE != W.DATATYPE").append(EOL)
	    .append("  or D.NULLABLE != W.NULLABLE").append(EOL)
	    .append(" )").append(EOL)
	    .append(")").append(EOL)
	    .append("where").append(EOL)
	    .append("W.DC_SKEY = ?").append(EOL)
	    .append("and W.DC_BATCH_SKEY = ?").append(EOL)
	;
	   return sb.toString();
  }
  
  
  public String getSelectObjectsWithConstraintChangesSQL() {
	  StringBuffer sb = new StringBuffer(); 	   
	   sb
	    .append("select").append(EOL)
	    .append("W.TABLE_SKEY,").append(EOL)
	    .append("W.CONSTRAINT_NAME,").append(EOL)
	    .append("D.TABLE_SKEY,").append(EOL)
	    .append("D.CONSTRAINT_NAME,").append(EOL)
	    .append("D.CONSTRAINT_SKEY,").append(EOL)
	    .append("case").append(EOL)
	    .append(" when W.CONSTRAINT_NAME is null then 'DELETE'").append(EOL)
	    .append(" when D.CONSTRAINT_NAME is null then 'INSERT'").append(EOL)
	    .append(" else 'UPDATE'").append(EOL)
	    .append("end as STATUS").append(EOL)
	    .append("from").append(EOL)
	    .append("NZR_WRK_CONSTRAINTS W").append(EOL)
	    .append("full outer join").append(EOL)
	    .append(" NZR_DC_CONSTRAINTS D").append(EOL)
	    .append("on").append(EOL)
	    .append(" (").append(EOL)
	    .append("   D.TABLE_SKEY = W.TABLE_SKEY").append(EOL)
	    .append("  and D.CONSTRAINT_NAME = W.CONSTRAINT_NAME").append(EOL)
	    .append("  and").append(EOL)
	    .append("   (").append(EOL)
	    .append("     D.CONSTRAINT_TYPE != W.CONSTRAINT_TYPE").append(EOL)
	    .append("     or ").append(EOL)
	    .append("    (").append(EOL)
	    .append("     D.PK_TABLE_NAME != W.PK_TABLE_NAME").append(EOL)
	    .append("    and ").append(EOL)
	    .append("   (").append(EOL)
	    .append("     D.PK_TABLE_NAME is not null").append(EOL)
	    .append("     or W.PK_TABLE_NAME is not null").append(EOL)
	    .append("   )").append(EOL)
	    .append(" )").append(EOL)
	    .append("  )").append(EOL)
	    .append("  )").append(EOL)
	    .append(" where").append(EOL)
	    .append(" W.DC_SKEY = ?").append(EOL)
	    .append(" and W.DC_BATCH_SKEY = ?").append(EOL)
	  ;

	   return sb.toString();
  }
  
  public String getSelectObjectsWithConstraintAttributeChangesSQL() {
	  StringBuffer sb = new StringBuffer(); 	   
	   sb
	    .append("select").append(EOL)
	    .append(" W.TABLE_SKEY,").append(EOL)
	    .append(" W.CONSTRAINT_NAME,").append(EOL)
	    .append(" W.CONSTRAINT_ATTRIBUTE_POSITION,").append(EOL)
	    .append(" W.ATTRIBUTE_NAME,").append(EOL)
	    .append(" D.TABLE_SKEY,").append(EOL)
	    .append(" D.CONSTRAINT_NAME,").append(EOL)
	    .append(" D.CONSTRAINT_SKEY,").append(EOL)
	    .append(" D.CONSTRAINT_ATTRIBUTE_POSITION,").append(EOL)
	    .append(" D.ATTRIBUTE_NAME,").append(EOL)
	    .append(" case").append(EOL)
	    .append("  when W.ATTRIBUTE_NAME is null then 'DELETE'").append(EOL)
	    .append("  when D.ATTRIBUTE_NAME is null then 'INSERT'").append(EOL)
	    .append("  else 'UPDATE'").append(EOL)
	    .append(" end as STATUS").append(EOL)
	    .append("from").append(EOL)
	    .append("  NZR_WRK_CONSTRAINT_ATTR W").append(EOL)
	    .append("full outer join").append(EOL)
	    .append("  (").append(EOL)
	    .append("   select").append(EOL)
	    .append("    C.TABLE_SKEY,").append(EOL)
	    .append("    C.CONSTRAINT_SKEY,").append(EOL)
	    .append("    C.CONSTRAINT_NAME,").append(EOL)
	    .append("    CA.CONSTRAINT_ATTRIBUTE_POSITION,").append(EOL)
	    .append("    CA.ATTRIBUTE_NAME").append(EOL)
	    .append("    from").append(EOL)
	    .append("       NZR_DC_CONSTRAINTS C").append(EOL)
	    .append("    inner join").append(EOL)
	    .append("       NZR_DC_CONSTRAINT_ATTRIBUTE CA").append(EOL)
	    .append("    on").append(EOL)
	    .append("      (").append(EOL)
	    .append("       CA.TABLE_SKEY = C.TABLE_SKEY ").append(EOL)
	    .append("       and CA.CONSTRAINT_SKEY = C.CONSTRAINT_SKEY").append(EOL)
	    .append("      )").append(EOL)
	    .append("   ) D").append(EOL)
	    .append("on").append(EOL)
	    .append(" (").append(EOL)
	    .append("   D.TABLE_SKEY = W.TABLE_SKEY").append(EOL)
	    .append("   and D.CONSTRAINT_NAME = W.CONSTRAINT_NAME").append(EOL)
	    .append("   and D.CONSTRAINT_ATTRIBUTE_POSITION = W.CONSTRAINT_ATTRIBUTE_POSITION").append(EOL)
	    .append("   and D.ATTRIBUTE_NAME != W.ATTRIBUTE_NAME").append(EOL)
	    .append(" )").append(EOL)
	    .append("where").append(EOL)
	    .append("W.DC_SKEY = ?").append(EOL)
	    .append("and W.DC_BATCH_SKEY = ?").append(EOL)
	;
	   return sb.toString();
  }
  
 

public Map<Object, Object> getSql() {
	return sql;
}

public void setSql(Map<Object, Object> sql) {
	this.sql = sql;
}

public static void main(String a[]) {
	  //System.out.print(new  DataCaptureRepoSQLBuilderImpl().getInsertTableConstraintInfoSQL());
	   System.out.print(new  DataCaptureRepoSqlBuilder().sql);
   }
}