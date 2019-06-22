package mycompany.nzr.dc.dto;

public class SrcColumnDTO implements SrcObjectDTO {
	//c.OBJID,
    //TABLE_NAME,
    //COLUMN_NAME,
    //DATATYPE,
    // NULLABLE,
    //DEFAULT_VALUE,
    //COLUMN_COMMENT,
    //DIST_POSITION,
    //COLUMN_POSITION,
    //ORG_POSITION
	
	
	private int dataLength = 10;
	private int dataPrecision = 1;
	private int dataScale = 1;
	
	
	private Long objId;
	private String tableName;
	private String columnName;
	private String dataType;
	private Integer nullable;
	private String defaultValue;
	private String columnComment;
	private Integer distPosition;
	private Integer columnPosition;
	private Integer orgPosition;
	private long tableSkey; // repo's attribute only

	public Long getObjId() {
		return objId;
	}

	public void setObjId(Long objId) {
		this.objId = objId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getNullable() {
		return nullable;
	}

	public void setNullable(Integer nullable) {
		this.nullable = nullable;
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

	public Integer getDistPosition() {
		return distPosition;
	}

	public void setDistPosition(Integer distPosition) {
		this.distPosition = distPosition;
	}

	public Integer getColumnPosition() {
		return columnPosition;
	}

	public void setColumnPosition(Integer columnPosition) {
		this.columnPosition = columnPosition;
	}

	public Integer getOrgPosition() {
		return orgPosition;
	}

	public void setOrgPosition(Integer orgPosition) {
		this.orgPosition = orgPosition;
	}

	public long getTableSkey() {
		return tableSkey;
	}

	public void setTableSkey(long tableSkey) {
		this.tableSkey = tableSkey;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public int getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision(int dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public int getDataScale() {
		return dataScale;
	}

	public void setDataScale(int dataScale) {
		this.dataScale = dataScale;
	}
	
	

}
