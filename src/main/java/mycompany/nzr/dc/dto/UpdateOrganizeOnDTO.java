package mycompany.nzr.dc.dto;

public class UpdateOrganizeOnDTO {

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getOrgPosition() {
		return orgPosition;
	}
	public void setOrgPosition(Integer orgPosition) {
		this.orgPosition = orgPosition;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	private String tableName;
	private Integer orgPosition;
	private String attributeName;

	
}
