package mycompany.nzr.dp.dto;

public class TempTableCol {

	private String tableSkey;
	private String attributeName;
	private String dataType;
	private int dataLength;
	private int dataPrecision;
	private int dataScale;

	public String getTableSkey() {
		return tableSkey;
	}

	public void setTableSkey(String tableSkey) {
		this.tableSkey = tableSkey;
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
