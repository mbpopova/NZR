package mycompany.nzr.common;

public enum Output {
	INSERTS("_exttbl_inserts.csv", "INSERTS"), 
	DELETES("_exttbl_deletes.csv", "DELETES"), 
	DDL_TABLE("create-table.sql", "DDL"), 
	DDL_PK("pk.sql", "DDL"), 
	DDL_FK("fk.sql", "DDL"), 
	DDL_MISC("misc.sql", "DDL");
	
	private String fileName;
    private String folderName;
    
	private Output(String fileName, String folderName) {
		this.fileName = fileName;
		this.folderName = folderName;
	}

	public String getFileName() {
		return fileName;
	}
	
	public String getFolderName() {
		return folderName;
	}
}
