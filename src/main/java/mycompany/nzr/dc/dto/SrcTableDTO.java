package mycompany.nzr.dc.dto;

public class SrcTableDTO implements SrcObjectDTO {

	// TABLENAME,
	// CREATEDATE,
	// OBJID,
	// RELNATTS,
	// DESCRIPTION

	private long tableSkey;  //repo's attribute only
	private String tableName;
	private String createDate;
	private Long objId;
	private Integer relnatts;  //col count
	private String description;
	public long getTableSkey() {
		return tableSkey;
	}
	public void setTableSkey(long tableSkey) {
		this.tableSkey = tableSkey;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getObjId() {
		return objId;
	}
	public void setObjId(Long objId) {
		this.objId = objId;
	}
	public Integer getRelnatts() {
		return relnatts;
	}
	public void setRelnatts(Integer relnatts) {
		this.relnatts = relnatts;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
