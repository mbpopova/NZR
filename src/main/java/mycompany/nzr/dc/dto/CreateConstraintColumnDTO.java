package mycompany.nzr.dc.dto;

/**
 * Contains fields enough to add a Foreign Key constraint but also used to hold data for Primary Key and Unique constraint ddl. 
 * @author Margarita
 *
 */
public class CreateConstraintColumnDTO {  
	private String tableName;
	private String constraintName;
	private Integer constraintAttributePosition;
	private String attributeName;
	private String pkTableName;
	private String pkAttributeName;
	private String matchType;
	private String updateType;
	private String deleteType;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public Integer getConstraintAttributePosition() {
		return constraintAttributePosition;
	}

	public void setConstraintAttributePosition(Integer constraintAttributePosition) {
		this.constraintAttributePosition = constraintAttributePosition;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String getPkAttributeName() {
		return pkAttributeName;
	}

	public void setPkAttributeName(String pkAttributeName) {
		this.pkAttributeName = pkAttributeName;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

}