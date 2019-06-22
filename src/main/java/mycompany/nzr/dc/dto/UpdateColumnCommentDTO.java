package mycompany.nzr.dc.dto;

public class UpdateColumnCommentDTO {

	 private String tableName;
	 private String  attributeName;
	 private String  columnComment;
	 
	 

	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public String getAttributeName() {
		return attributeName;
	}



	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}



	public String getColumnComment() {
		return columnComment;
	}



	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}



}
