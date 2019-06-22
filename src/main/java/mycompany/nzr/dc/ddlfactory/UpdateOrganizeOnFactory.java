package mycompany.nzr.dc.ddlfactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import mycompany.nzr.dc.dto.TableChangeIndicatorsDTO;
import mycompany.nzr.dc.dto.UpdateOrganizeOnDTO;

public class UpdateOrganizeOnFactory {
/**
 *  alter table ORDERS
 * 	organize on (TABLE_SKEY,ORDER_NUMBER);

    alter table ORDERS
	organize on none;
 */
	private static String EOL = System.getProperty("line.separator");

	
	public String getUpdateOrganizeOn(List<UpdateOrganizeOnDTO> dtos) {
		StringBuffer bfr = new StringBuffer();
		Map<String, Set<UpdateOrganizeOnDTO>> groupedDtos = group(dtos);

		Iterator<Map.Entry<String, Set<UpdateOrganizeOnDTO>>> iter = groupedDtos
				.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry<String, Set<UpdateOrganizeOnDTO>> mapEntry = iter.next();
			String tblName = mapEntry.getKey();
			bfr.append("ALTER TABLE ").append(tblName);
			
			Set<UpdateOrganizeOnDTO> set = mapEntry.getValue();
			
			if (set.size() == 0) {
				bfr.append(" ORGANIZE ON NONE;").append(EOL);
				continue;
			} else {
				bfr.append(" ORGANIZE ON ").append("(");
			}
			
			Iterator<UpdateOrganizeOnDTO> setIter = set.iterator();
			while (setIter.hasNext()) {
				UpdateOrganizeOnDTO dto = (UpdateOrganizeOnDTO) setIter.next();
				bfr.append(dto.getAttributeName()).append(",");
			}
			bfr.deleteCharAt(bfr.length() - 1);
			bfr.append(");");
			bfr.append(EOL);
		}
		return bfr.toString();
	}
	
	private Map<String, Set<UpdateOrganizeOnDTO>> group(List<UpdateOrganizeOnDTO> dtos)
	{
		Map <String, Set<UpdateOrganizeOnDTO>> map = new TreeMap<String, Set<UpdateOrganizeOnDTO>>();

		for (UpdateOrganizeOnDTO dto : dtos) {
			
			Set<UpdateOrganizeOnDTO> set = map.get(dto.getTableName());
			
			if (set == null)  {
				
				set = new TreeSet<UpdateOrganizeOnDTO>(new Comparator<UpdateOrganizeOnDTO>() {

					@Override
					public int compare(UpdateOrganizeOnDTO dto1, UpdateOrganizeOnDTO dto2) {
						if (dto1.getOrgPosition() < dto2.getOrgPosition()) return -1;
						else if (dto1.getOrgPosition() == dto2.getOrgPosition()) return 0;
						else return 1;
					}
					
				});
				map.put(dto.getTableName(), set);
			}
			if (dto.getAttributeName() != null) {
				set.add(dto);			
			}			
		}
		return map;
	}
	public static void main(String[] args) {
		UpdateOrganizeOnDTO dto11 = new UpdateOrganizeOnDTO();
		dto11.setAttributeName("Col11");
		dto11.setOrgPosition(1);
		dto11.setTableName("Tbl1");
		
		UpdateOrganizeOnDTO dto12 = new UpdateOrganizeOnDTO();
		dto12.setAttributeName("Col12");
		dto12.setOrgPosition(2);
		dto12.setTableName("Tbl1");
		
		UpdateOrganizeOnDTO dto21 = new UpdateOrganizeOnDTO();
	//	dto21.setAttributeName("Col21");
		dto21.setOrgPosition(1);
		dto21.setTableName("Tbl2");
		
		System.out.println(new UpdateOrganizeOnFactory().getUpdateOrganizeOn(Arrays.asList(new UpdateOrganizeOnDTO[]{dto11, dto12, dto21})));
	}

}
