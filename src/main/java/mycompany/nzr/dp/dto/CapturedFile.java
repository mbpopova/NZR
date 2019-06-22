package mycompany.nzr.dp.dto;



public class CapturedFile implements Comparable<CapturedFile> {
	
	private int dcBatchDtlSkey;
	private int dcBatchSkey;
	private int tableSkey;
	private String tableName;
	private int deleteCnt;
	private int insertCnt;
	private int ddlFlag;
	private String folderName;
	private String fileName;
	private String fileFormat;
	private String fileLocation;
	private long dpBatchDtlSkey;
	
	public long getDpBatchDtlSkey() {
		return dpBatchDtlSkey;
	}

	public void setDpBatchDtlSkey(long dpBatchDtlSkey) {
		this.dpBatchDtlSkey = dpBatchDtlSkey;
	}

	public int getDcBatchDtlSkey() {
		return dcBatchDtlSkey;
	}

	public void setDcBatchDtlSkey(int dcBatchDtlSkey) {
		this.dcBatchDtlSkey = dcBatchDtlSkey;
	}

	public int getDcBatchSkey() {
		return dcBatchSkey;
	}

	public void setDcBatchSkey(int dcBatchSkey) {
		this.dcBatchSkey = dcBatchSkey;
	}

	public int getTableSkey() {
		return tableSkey;
	}

	public void setTableSkey(int tableSkey) {
		this.tableSkey = tableSkey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getDeleteCnt() {
		return deleteCnt;
	}

	public void setDeleteCnt(int deleteCnt) {
		this.deleteCnt = deleteCnt;
	}

	public int getInsertCnt() {
		return insertCnt;
	}

	public void setInsertCnt(int insertCnt) {
		this.insertCnt = insertCnt;
	}

	public int getDdlFlag() {
		return ddlFlag;
	}

	public void setDdlFlag(int ddlFlag) {
		this.ddlFlag = ddlFlag;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}


	@Override
	public int compareTo(CapturedFile o) {
		return fileName.compareTo(o.getFileName());
	}
	
}
