package mycompany.nzr.dc.ddlfactory;

import java.util.Arrays;
import java.util.List;

import mycompany.nzr.dc.dto.RenameColumnDTO;

public class RenameColumnFactory {
	private static String EOL = System.getProperty("line.separator");
	/**
	 * alter table ORDERS
  	   rename column LAST_UPDATE_DT to LAST_UPDATE_DATE
	 */
	
	public String getRenameColumnDDL(List<RenameColumnDTO> dtos) {
		
		StringBuffer bfr = new StringBuffer();
		
		for (RenameColumnDTO dto : dtos) {
			bfr.append("ALTER TABLE ").append(dto.getTableName())
			.append(" RENAME COLUMN ").append(dto.getOldName()).append(" TO ").append(dto.getNewName());
			bfr.append(EOL);
		}
	
		return bfr.toString();
	}

}
