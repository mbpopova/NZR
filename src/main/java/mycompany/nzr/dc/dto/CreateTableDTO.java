package mycompany.nzr.dc.dto;

public class CreateTableDTO {
	private String tableName;
	private String attributeName;
	private String dataType;
	private Integer dataLength;
	private Integer dataPrecision;
	private Integer dataScale;
	private Boolean nullable;
	private Integer distPosition;
	private Integer orgPosition;
	private String defaultValue;
	private String columnComment;
	private Integer columnPosition;
	
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getDataLength() {
		return dataLength;
	}
	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}
	public Integer getDataPrecision() {
		return dataPrecision;
	}
	public void setDataPrecision(Integer dataPrecision) {
		this.dataPrecision = dataPrecision;
	}
	public Integer getDataScale() {
		return dataScale;
	}
	public void setDataScale(Integer dataScale) {
		this.dataScale = dataScale;
	}
	public Boolean getNullable() {
		return nullable;
	}
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}
	public Integer getDistPosition() {
		return distPosition;
	}
	public void setDistPosition(Integer distPosition) {
		this.distPosition = distPosition;
	}
	public Integer getOrgPosition() {
		return orgPosition;
	}
	public void setOrgPosition(Integer orgPosition) {
		this.orgPosition = orgPosition;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
	public Integer getColumnPosition() {
		return columnPosition;
	}
	public void setColumnPosition(Integer columnPosition) {
		this.columnPosition = columnPosition;
	}

	
	
}