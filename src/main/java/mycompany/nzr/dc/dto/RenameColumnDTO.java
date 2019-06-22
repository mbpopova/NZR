package mycompany.nzr.dc.dto;

public class RenameColumnDTO {

	private String tableName;
	private String oldName;
	private String newName;
	
	
	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getOldName() {
		return oldName;
	}


	public void setOldName(String oldName) {
		this.oldName = oldName;
	}


	public String getNewName() {
		return newName;
	}


	public void setNewName(String newName) {
		this.newName = newName;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
