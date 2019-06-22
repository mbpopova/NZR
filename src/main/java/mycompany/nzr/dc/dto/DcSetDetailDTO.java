package mycompany.nzr.dc.dto;

public class DcSetDetailDTO {
	// TABLE_SKEY=1, TABLE_NAME=CUSTOMER, SRC_OBJECT_ID=217330, DC_TYPE=P,
	// STG_FILE_FORMAT=F, LAST_DDL_TS=2012-12-21 19:02:11.0, WHERE_CLAUSE=null

	private long tableSkey;
	private String tableName;
	private long srcObjectId;
	private String dcType;
	private String stgFileFormat;
	private String lastDdlTs;
	private String whereClause;

	
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

	public long getSrcObjectId() {
		return srcObjectId;
	}

	public void setSrcObjectId(long srcObjectId) {
		this.srcObjectId = srcObjectId;
	}

	public String getDcType() {
		return dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}

	public String getStgFileFormat() {
		return stgFileFormat;
	}

	public void setStgFileFormat(String stgFileFormat) {
		this.stgFileFormat = stgFileFormat;
	}

	public String getLastDdlTs() {
		return lastDdlTs;
	}

	public void setLastDdlTs(String lastDdlTs) {
		this.lastDdlTs = lastDdlTs;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	@Override
	public String toString() {
		return this.tableName;
	}

}
