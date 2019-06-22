package mycompany.nzr.dc.dto;

public class SrcConstraintAttributeDTO {
/*
 * RELATION,
CONSTRAINTNAME,
ATTNAME,
CONSEQ
 */
	private String tableName;
	private long tableSkey;
	private String relation; // TABLE_NAME
	private String constraintName;
	private String attName;   
	private Integer conseq;  // CONSTRAINT_ATTRIBUTE_POSITION
	private String pkAttName; 
	
	public String getPkAttName() {
		return pkAttName;
	}
	public void setPkAttName(String pkAttName) {
		this.pkAttName = pkAttName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getTableSkey() {
		return tableSkey;
	}
	public void setTableSkey(long tableSkey) {
		this.tableSkey = tableSkey;
	}
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
	public String getAttName() {
		return attName;
	}
	public void setAttName(String attName) {
		this.attName = attName;
	}
	public Integer getConseq() {
		return conseq;
	}
	public void setConseq(Integer conseq) {
		this.conseq = conseq;
	}
	
	
	
}
