package mycompany.nzr.dc.ddlfactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
/**
 * alter table 
	ORDERS
    add constraint ORDER_FK1 foreign key (CUSTOMER_SKEY)
	references CUSTOMER (CUSTOMER_SKEY)
	match FULL
	on update CASCADE
	on delete SET NULL
    
    match FULL
	on update CASCADE
	on delete SET NULL
 */

public class CreateConstraintFactory {
	private static String EOL = System.getProperty("line.separator");
	
	/**
	 *  alter table 
	 * 	ORDERS
        add constraint ORDERS_PK primary key (ORDER_SKEY)
	 */
	public List<String> getCreatePKConstraintDDL(List<CreateConstraintColumnDTO> allPkColumns) {
		return getCreatePKOrUniqueConstraintDDL(allPkColumns, true);
	}
	
	public List<String> getCreateUniqueConstraintDDL(List<CreateConstraintColumnDTO> allPkColumns) {
		return getCreatePKOrUniqueConstraintDDL(allPkColumns, false);
	}
	
	private List<String> getCreatePKOrUniqueConstraintDDL(List<CreateConstraintColumnDTO> allPkColumns, boolean isPk) {
		List<String> ddls = new ArrayList<String>(); 		
		Set<String> pkUniqueNames = getUniqueConstraintNames(allPkColumns);

		for (String pkName : pkUniqueNames) {
			StringBuffer bfr = new StringBuffer();
			List<CreateConstraintColumnDTO> pkColumnGroup = getConstraintColumnGroup(
					pkName, allPkColumns);
			CreateConstraintColumnDTO firstPkColumn = pkColumnGroup.get(0);
			bfr.append("ALTER TABLE ").append(EOL)
					.append(firstPkColumn.getTableName()).append(EOL)
					.append(" ADD CONSTRAINT ")
					.append(firstPkColumn.getConstraintName());
			
			if (isPk)
				bfr.append(" PRIMARY KEY (");
			else
				bfr.append(" UNIQUE (");

			for (CreateConstraintColumnDTO pkColumn : pkColumnGroup) {
				bfr.append(pkColumn.getAttributeName());
				bfr.append(",");
			}
			bfr.deleteCharAt(bfr.length() - 1);
			
			bfr.append(");");
			bfr.append(EOL).append(EOL);
			ddls.add(bfr.toString());
		}
		return ddls;
	}
	
	public List<String> getCreateFKConstraintDDL(List<CreateConstraintColumnDTO> allFkColumns) {
		
		List<String> ddls = new ArrayList<String>();
		Set<String> fkUniqueNames = getUniqueConstraintNames(allFkColumns);

		for (String fkName : fkUniqueNames) {
			StringBuffer bfr = new StringBuffer();
			List<CreateConstraintColumnDTO> fkColumnGroup = getConstraintColumnGroup(
					fkName, allFkColumns);
			CreateConstraintColumnDTO firstFkColumn = fkColumnGroup.get(0);
			bfr.append("ALTER TABLE ").append(EOL)
					.append(firstFkColumn.getTableName()).append(EOL)
					.append(" ADD CONSTRAINT ")
					.append(firstFkColumn.getConstraintName())
					.append(" FOREIGN KEY (");

			for (CreateConstraintColumnDTO fkColumn : fkColumnGroup) {
				bfr.append(fkColumn.getAttributeName());
				bfr.append(",");
			}
			bfr.deleteCharAt(bfr.length() - 1);
			bfr.append(")").append(" REFERENCES ")
					.append(firstFkColumn.getPkTableName()).append("(");
			for (CreateConstraintColumnDTO fkColumn : fkColumnGroup) {
				bfr.append(fkColumn.getPkAttributeName());
				bfr.append(",");
			}
			bfr.deleteCharAt(bfr.length() - 1);
			bfr.append(")");
			/**
			 *  match FULL
	            on update CASCADE
	            on delete SET NULL
			 */
			
			if (!"NO ACTION".equals(firstFkColumn.getMatchType())) {
				bfr.append(EOL).append("MATCH FULL ");
			}
			if (!"NO ACTION".equalsIgnoreCase(firstFkColumn.getDeleteType())) {
				bfr.append(EOL).append("ON DELETE SET NULL ");
			}
			if ("NO ACTION".equalsIgnoreCase(firstFkColumn.getUpdateType())) {
				bfr.append(EOL).append("ON UPDATE CASCADE");
			}
			
			//bfr.append(EOL);
			bfr.append(";");
			bfr.append(EOL);
			ddls.add(bfr.toString());
	    }
		
		return ddls;
	}
	
	private Set<String> getUniqueConstraintNames(List<CreateConstraintColumnDTO> constraintColumns)
	{
		Set<String> constraintUniqueNames = new HashSet<String> ();
		for (CreateConstraintColumnDTO fkc : constraintColumns) {
			constraintUniqueNames.add(fkc.getConstraintName());
		}
		return constraintUniqueNames;
	}
	
	private List<CreateConstraintColumnDTO> getConstraintColumnGroup(String constraintName, List<CreateConstraintColumnDTO> fkConstraints) {
		Set<CreateConstraintColumnDTO> fkcSet= new TreeSet<CreateConstraintColumnDTO>(new Comparator<CreateConstraintColumnDTO>(){

		   @Override
			public int compare(CreateConstraintColumnDTO o1, CreateConstraintColumnDTO o2) {
				if (o1.getConstraintAttributePosition() == o2.getConstraintAttributePosition()) return 0;
				else if (o1.getConstraintAttributePosition() < o2.getConstraintAttributePosition()) return -1;
				else return 1;
			}
			
		});
		
		for (CreateConstraintColumnDTO fkc : fkConstraints) {
			if (fkc.getConstraintName().equals(constraintName)) {
				fkcSet.add(fkc);
			}
		}
		return new ArrayList<CreateConstraintColumnDTO>(fkcSet);
	}
	public static void main(String argd[]) {
		List<CreateConstraintColumnDTO> list = new ArrayList<CreateConstraintColumnDTO>();
		CreateConstraintColumnDTO fkc11 = new CreateConstraintColumnDTO();
		fkc11.setAttributeName("ChildTblColName11");
		fkc11.setConstraintAttributePosition(1);
		fkc11.setConstraintName("FK_Test_Constraint1");
		fkc11.setDeleteType("Some Type");
		
		fkc11.setPkAttributeName("ParentTblColName11");
		fkc11.setPkTableName("ParentTable1");
		fkc11.setTableName("ChildTable1");
		list.add(fkc11);
		
		
		CreateConstraintColumnDTO fkc12 = new CreateConstraintColumnDTO();
		fkc12.setAttributeName("ChildTblColName12");
		fkc12.setConstraintAttributePosition(2);
		fkc12.setConstraintName("FK_Test_Constraint1");
		fkc12.setDeleteType("Some Type");		
		fkc12.setPkAttributeName("ParentTblColName12");
		fkc12.setPkTableName("ParentTabl1");
		fkc12.setTableName("ChildTable1");
		list.add(fkc12);
		
		
		CreateConstraintColumnDTO fkc2 = new CreateConstraintColumnDTO();
		fkc2.setAttributeName("ChildTblColName21");
		fkc2.setConstraintAttributePosition(1);
		fkc2.setConstraintName("FK_Test_Constraint2");
		fkc2.setDeleteType("Some Type");
		
		fkc2.setPkAttributeName("ParentTblColName21");
		fkc2.setPkTableName("ParentTable2");
		fkc2.setTableName("ChildTable2");		
		list.add(fkc2);
		
		//List<String> ddls1 = new CreateConstraintFactory().getCreateFKConstraintDDL(list);		
		//System.out.print(ddl);
		
		List<String> ddl = new CreateConstraintFactory().getCreatePKConstraintDDL(list);
		System.out.print(ddl);
	}
	
}
