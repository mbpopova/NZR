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
}
