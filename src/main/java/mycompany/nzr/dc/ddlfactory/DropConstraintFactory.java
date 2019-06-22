package mycompany.nzr.dc.ddlfactory;

import java.util.Arrays;
import java.util.List;

import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;

public class DropConstraintFactory {

	/**
	 * alter table ORDERS drop constraint ORDERS_FK1 restrict;
	 */
	private static String EOL = System.getProperty("line.separator");

	public String getDropConstraintDDL(
			List<CreateConstraintColumnDTO> allFkcColumns) {

		StringBuffer bfr = new StringBuffer();
		for (CreateConstraintColumnDTO fkc : allFkcColumns) {
			bfr.append("ALTER TABLE ").append(fkc.getTableName())
					.append(" DROP CONSTRAINT ")
					.append(fkc.getConstraintName()).append(" RESTRICT")
					.append(EOL);
		}

		return bfr.toString();
	}

	public static void main(String[] args) {
		CreateConstraintColumnDTO fkc1 = new CreateConstraintColumnDTO();
		fkc1.setTableName("TestTableName1");
		fkc1.setConstraintName("TestConstraintName1");

		CreateConstraintColumnDTO fkc2 = new CreateConstraintColumnDTO();
		fkc2.setTableName("TestTableName2");
		fkc2.setConstraintName("TestConstraintName2");

		String ddl = new DropConstraintFactory().getDropConstraintDDL(Arrays
				.asList(new CreateConstraintColumnDTO[] { fkc1, fkc2 }));

		System.out.print(ddl);

	}

}
