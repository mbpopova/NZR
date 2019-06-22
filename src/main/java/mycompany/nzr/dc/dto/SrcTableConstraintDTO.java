package mycompany.nzr.dc.dto;

public class SrcTableConstraintDTO implements SrcObjectDTO {
/*
 * RELATION,
   CONSTRAINTNAME,
   CONTYPE,
   PKRELATION
 */
	
	private String tableName;
	private String relation;  // table name
	private String constraintName;
	private String conType;
	private String pkRelation;  // "referred to" table name if foreign key constraint
	private long tableSkey;
	
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getConstraintName() {
		return constraintName;
	}
	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}
	public String getConType() {
		return conType;
	}
	public void setConType(String conType) {
		this.conType = conType;
	}
	public String getPkRelation() {
		return pkRelation;
	}
	public void setPkRelation(String pkRelation) {
		this.pkRelation = pkRelation;
	}
	public long getTableSkey() {
		return tableSkey;
	}
	public void setTableSkey(long tableSkey) {
		this.tableSkey = tableSkey;
	}
	public String getTableName() {
		return tableName;
	}
	
}
