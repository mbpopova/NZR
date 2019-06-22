package mycompany.nzr.dc.ddlfactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DropRenameTableFactory {
	private static String EOL = System.getProperty("line.separator");
	/**
	 * drop table ORDERS;
    */
	public String getDropTableDDL(List<String> tableNames) {
		StringBuffer bfr = new StringBuffer();
		for (String tableName : tableNames) {
			bfr.append("DROP TABLE ").append(tableName).append(EOL);
		}
		return bfr.toString();
	}
	
	
	/**
	 * alter table ORDERS_OLD rename to ORDERS_NEW;
	 *
	 */
	
	public String getRenameTableDDL (Map<String, String> oldNewTablePairs) {
		StringBuffer bfr = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = oldNewTablePairs.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry<String, String> mapEntry = iter.next();
			bfr
			.append("ALTER TABLE ")
			.append(mapEntry.getKey())
			.append(" RENAME TO ").append(mapEntry.getValue())
			.append(EOL);
		}
		return bfr.toString();
	}
	

	public static void main(String[] args) {
		List<String> tableNames = Arrays.asList(new String[]{"Table1", "Table2"});
		DropRenameTableFactory factory = new DropRenameTableFactory();
		System.out.println(factory.getDropTableDDL(tableNames));
		
		Map<String, String> map  = new HashMap<String, String> ();
		map.put("OldTable1", "NewTable1");
		map.put("OldTable2", "NewTable2");
		System.out.println(factory.getRenameTableDDL(map));
		
		
	}

}
