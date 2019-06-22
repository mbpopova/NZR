package mycompany.nzr.dc.ddlfactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import mycompany.nzr.dc.dto.CreateTableDTO;

public class CreateTableFactory {
	private static String EOL = System.getProperty("line.separator");
	private static int PADDED_LENGTH1 = 20;
	private static int PADDED_LENGTH2 = 40;
	
	private void pad (StringBuffer bfr, String column, int paddedLength, int actualLength) {
		
		for (int i = 0; i < (paddedLength - actualLength); i++) {
			bfr.append(" ");
		}
	}
	
	private Map<Integer, CreateTableDTO> getColumnsSortedByPosition(
			List<CreateTableDTO> columns) {
		Map<Integer, CreateTableDTO> mapSortedByColPosition = new TreeMap<Integer, CreateTableDTO>();
		Iterator<CreateTableDTO> i = columns.iterator();
		int pos = 0;
		while (i.hasNext()) {
			CreateTableDTO column = (CreateTableDTO) i.next();
			if (column.getColumnPosition() != null)
				mapSortedByColPosition.put(column.getColumnPosition(), column);
			else
				mapSortedByColPosition.put(++pos, column);
		}
		return mapSortedByColPosition;
	}
	
	private Map<Integer, CreateTableDTO> getColumnsSortedByOrgPosition(List<CreateTableDTO> columns) {
		Map<Integer, CreateTableDTO> mapSortedByOrgPosition = new TreeMap<Integer, CreateTableDTO>();
		Iterator<CreateTableDTO> i = columns.iterator();
		while (i.hasNext()) {
			CreateTableDTO column = (CreateTableDTO)i.next();
			mapSortedByOrgPosition.put(column.getOrgPosition(), column);
		}
		return mapSortedByOrgPosition;
	}
	
	private Map<Integer, CreateTableDTO> getColumnsSortedByDistPosition(List<CreateTableDTO> columns) {
		Map<Integer, CreateTableDTO> mapSortedByDistPosition = new TreeMap<Integer, CreateTableDTO>();
		Iterator<CreateTableDTO> i = columns.iterator();
		while (i.hasNext()) {
			CreateTableDTO column = (CreateTableDTO)i.next();
			mapSortedByDistPosition.put(column.getDistPosition(), column);
		}
		return mapSortedByDistPosition;
	}
	
	public String createTempTableDDL(List<CreateTableDTO> dtos, String tableName) {
		return doCreateTableDDL(dtos, tableName, true);
	}
    public String getCreateTableDDL(List<CreateTableDTO> dtos, String tableName) {  // Each dto represents a col in the new tbl
    	return doCreateTableDDL(dtos, tableName, false);
    }
    
    private String doCreateTableDDL(List<CreateTableDTO> dtos, String tableName, boolean tempTable) {
    	if (dtos.isEmpty()) {
    		return "" ;
    	}
		StringBuffer bfr = new StringBuffer();

		if (tempTable)
			bfr.append("CREATE TEMP TABLE ");
		else
		    bfr.append("CREATE TABLE ");
		
		bfr.append(tableName).append(EOL).append("(");

		Map<Integer, CreateTableDTO> mapSortedByColPosition = getColumnsSortedByPosition(dtos);
		Iterator<Entry<Integer, CreateTableDTO>> i = mapSortedByColPosition
				.entrySet().iterator();

		List<CreateTableDTO> organizeColumns = new ArrayList<CreateTableDTO>();
		List<CreateTableDTO> distColumns = new ArrayList<CreateTableDTO>();

		while (i.hasNext()) {
			Map.Entry<Integer, CreateTableDTO> mapEntry = (Map.Entry<Integer, CreateTableDTO>) i
					.next();
			CreateTableDTO c = mapEntry.getValue();

			if (c.getOrgPosition() != null) {
				organizeColumns.add(c);
			}

			if (c.getDistPosition() != null) {
				distColumns.add(c);
			}

			bfr.append(EOL).append("  ").append(c.getAttributeName());
			pad(bfr, c.getAttributeName(), PADDED_LENGTH1, c.getAttributeName()
					.length());
			bfr.append(c.getDataType());

			if (c.getNullable() != null && !c.getNullable()) {
				pad(bfr, c.getAttributeName(), PADDED_LENGTH2, PADDED_LENGTH1
						+ c.getDataType().length());
				bfr.append("NOT NULL");
			}
			bfr.append(",");
		}
		bfr.deleteCharAt(bfr.length() - 1);
		bfr.append(EOL).append(")").append(EOL);

		if (!distColumns.isEmpty()) {
			printOrganizeOrDistStatements(getColumnsSortedByDistPosition(distColumns), false, bfr);
		} else if (!tempTable){
			bfr.append(EOL).append("DISTRIBUTE ON RANDOM ");
		}
		bfr.append(EOL);
		if (!organizeColumns.isEmpty()) {
			printOrganizeOrDistStatements(getColumnsSortedByOrgPosition(organizeColumns), true, bfr);
		}		
		bfr.append(";");
		return bfr.toString();
    			
    }
    
    private void printOrganizeOrDistStatements(Map<Integer, CreateTableDTO> mapSortedByPosition, boolean isOrganize, StringBuffer bfr) {
    	Iterator<Entry<Integer, CreateTableDTO>> iter = mapSortedByPosition
				.entrySet().iterator();   	
    	if (isOrganize) {
			bfr.append("ORGANIZE ON (");
		} else {
			bfr.append("DISTRIBUTE ON (");
		}
		while (iter.hasNext()) {
			Map.Entry<Integer, CreateTableDTO> mapEntry = (Map.Entry<Integer, CreateTableDTO>) iter
					.next();
			/*if (isOrganize) {
				bfr.append("ORGANIZE ON (");
			} else {
				bfr.append("DISTRIBUTE ON (");
			}*/
			
			bfr
			  .append (((CreateTableDTO)mapEntry.getValue()).getAttributeName())
			  .append(",");
		}		
		bfr.deleteCharAt(bfr.length() - 1);
		bfr.append(")").append(EOL);
    }
    
    public static void main(String a[]) {
    	CreateTableDTO c1 = new CreateTableDTO();
    	c1.setAttributeName("PKCol1");
    	c1.setDataType("VARCHAR");
    	c1.setNullable(true);
    	////c1.setColumnPosition(2);
    	//c1.setOrgPosition(2);
    	//c1.setDistPosition(1);
    	
    	CreateTableDTO c2 = new CreateTableDTO();
    	c2.setAttributeName("PKCol2");
    	c2.setDataType("INTEGER");
    	//c2.setNullable(false);
    	///c2.setOrgPosition(1);
    	//c2.setColumnPosition(1);
    	//c2.setDistPosition(5);
    	
    	CreateTableFactory obj = new CreateTableFactory();
    	
    	System.out.print(obj.createTempTableDDL(Arrays.asList(new CreateTableDTO[]{c1, c2}), "MyTableName"));
    }
}

