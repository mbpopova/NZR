package mycompany.nzr.dc.ddlfactory;

import java.util.Arrays;
import java.util.List;

import mycompany.nzr.dc.dto.ExtendVarcharColumnDTO;

public class ExtendVarcharColumnFactory {

	private static String EOL = System.getProperty("line.separator");
	
//	alter table ORDERS modify column (ORDER_NUMBER character varying(11))

	
	public String getExtendVarcharColumnDDL(List<ExtendVarcharColumnDTO> dtos) {
		StringBuffer bfr = new StringBuffer();

		for (ExtendVarcharColumnDTO dto : dtos) {

			bfr.append("ALTER TABLE ").append(dto.getTableName())
					.append(" MODIFY COLUMN(").append(dto.getColumnName())
					.append(" CHARACTER VARYING (").append(dto.getNewLength())
					.append("));").append(EOL);
		}
		return bfr.toString();
	}
	public static void main(String[] args) {
		ExtendVarcharColumnDTO dto1 = new ExtendVarcharColumnDTO();
		dto1.setColumnName("TestCol1");
		dto1.setNewLength(10);
		dto1.setTableName("TestTbl1");
		
		ExtendVarcharColumnDTO dto2 = new ExtendVarcharColumnDTO();
		dto2.setColumnName("TestCol2");
		dto2.setNewLength(20);
		dto2.setTableName("TestTbl2");

		System.out.print(new ExtendVarcharColumnFactory()
				.getExtendVarcharColumnDDL(Arrays
						.asList(new ExtendVarcharColumnDTO[] { dto1, dto2 })));

	}

}
