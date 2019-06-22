package mycompany.nzr.dc.dto;

public class UpdateDefaultValueDTO {

	private String tableName;
	private String attributeName;
	private String defaultValue;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attribute_name) {
		this.attributeName = attribute_name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
