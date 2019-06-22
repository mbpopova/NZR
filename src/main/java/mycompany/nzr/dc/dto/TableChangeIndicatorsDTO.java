package mycompany.nzr.dc.dto;

public class TableChangeIndicatorsDTO {

	private Long tableSkey;
	private String tableName;
	private Long srcObjectId;
	
	private int tableRebuiltInd;
	private int tableRenamedInd;
	private int tableCommentChangedInd;
	private int tableOrgChangedInd;
	
	private int tableColumnsChangedInd;

	private int orgPosition;
	private String currAttrName;
	

	public int getOrgPosition() {
		return orgPosition;
	}

	public void setOrgPosition(int orgPosition) {
		this.orgPosition = orgPosition;
	}

	public String getCurrAttrName() {
		return currAttrName;
	}

	public void setCurrAttrName(String currAttrName) {
		this.currAttrName = currAttrName;
	}

	public Long getSrcObjectId() {
		return srcObjectId;
	}

	public void setSrcObjectId(Long srcObjectId) {
		this.srcObjectId = srcObjectId;
	}

	public Long getTableSkey() {
		return tableSkey;
	}

	public void setTableSkey(Long tableSkey) {
		this.tableSkey = tableSkey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getTableRebuiltInd() {
		return tableRebuiltInd;
	}

	public void setTableRebuiltInd(int tableRebuiltInd) {
		this.tableRebuiltInd = tableRebuiltInd;
	}

	public int getTableRenamedInd() {
		return tableRenamedInd;
	}

	public void setTableRenamedInd(int tableRenamedInd) {
		this.tableRenamedInd = tableRenamedInd;
	}

	public int getTableCommentChangedInd() {
		return tableCommentChangedInd;
	}

	public void setTableCommentChangedInd(int tableCommentChangedInd) {
		this.tableCommentChangedInd = tableCommentChangedInd;
	}

	
	public int getTableOrgChangedInd() {
		return tableOrgChangedInd;
	}

	public void setTableOrgChangedInd(int tableOrgChangedInd) {
		this.tableOrgChangedInd = tableOrgChangedInd;
	}

	public int getTableColumnsChangedInd() {
		return tableColumnsChangedInd;
	}

	public void setTableColumnsChangedInd(int tableColumnsChangedInd) {
		this.tableColumnsChangedInd = tableColumnsChangedInd;
	}

}
