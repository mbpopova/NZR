package mycompany.nzr.dc.dto;

public class ColumnChangeIndicatorsDTO {

	private long objectId;
	private String tableName;
	private long tableSkey;
	private String columnName;

	private int columnRenamedInd;
	private int columnDefaultChangedInd;
	private int columnCommentChangedInd;
	private int columnLengthChangedInd;

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnRenamedInd() {
		return columnRenamedInd;
	}

	public void setColumnRenamedInd(int columnRenamedInd) {
		this.columnRenamedInd = columnRenamedInd;
	}

	public int getColumnDefaultChangedInd() {
		return columnDefaultChangedInd;
	}

	public void setColumnDefaultChangedInd(int columnDefaultChangedInd) {
		this.columnDefaultChangedInd = columnDefaultChangedInd;
	}

	public int getColumnCommentChangedInd() {
		return columnCommentChangedInd;
	}

	public void setColumnCommentChangedInd(int columnCommentChangedInd) {
		this.columnCommentChangedInd = columnCommentChangedInd;
	}

	public int getColumnLengthChangedInd() {
		return columnLengthChangedInd;
	}

	public void setColumnLengthChangedInd(int columnLengthChangedInd) {
		this.columnLengthChangedInd = columnLengthChangedInd;
	}

	
}
