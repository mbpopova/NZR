package mycompany.nzr.dc.dto;

public class ExtendVarcharColumnDTO {

	private String columnName;
	private Integer oldLength;
	private Integer newLength;
	private String tableName;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getOldLength() {
		return oldLength;
	}
	public void setOldLength(Integer oldLength) {
		this.oldLength = oldLength;
	}
	public Integer getNewLength() {
		return newLength;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public void setNewLength(Integer newlength) {
		this.newLength = newlength;
	}
	
}
