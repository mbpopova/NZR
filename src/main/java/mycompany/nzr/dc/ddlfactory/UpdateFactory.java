package mycompany.nzr.dc.ddlfactory;

import java.util.Arrays;
import java.util.List;

import mycompany.nzr.dc.dto.ExtendVarcharColumnDTO;
import mycompany.nzr.dc.dto.UpdateColumnCommentDTO;
import mycompany.nzr.dc.dto.UpdateDefaultValueDTO;
import mycompany.nzr.dc.dto.UpdateTableCommentDTO;

public class UpdateFactory {

	/**
	 *comment on column ORDERS.ORDER_NUMBER is 'Test comment';
	 **/
	private static String EOL = System.getProperty("line.separator");
	
	public String getUpdateColumnCommentDDL(List<UpdateColumnCommentDTO> dtos) {
		StringBuffer bfr = new StringBuffer();
		
		for (UpdateColumnCommentDTO dto : dtos) {
						 
			bfr.append("COMMENT ON COLUMN ")
			.append(dto.getTableName()).append(".").append(dto.getAttributeName())
			.append(" IS ");
			
			if (dto.getColumnComment() != null) {
				bfr.append('\'');
				bfr.append(dto.getColumnComment());
				bfr.append('\'');
			}
			
			else {
				bfr.append(dto.getColumnComment());
			}
			
			bfr.append(EOL);
		}
		return bfr.toString();
	}
	
	/**
	 * alter table ORDERS
	   alter column ORDER_NUMBER set default 'test comment'
    
       alter table ORDERS
	   alter column ORDER_NUMBER drop default
 	*/
	
	public String getUpdateDefaultValueDDL(List<UpdateDefaultValueDTO> dtos) {
		StringBuffer bfr = new StringBuffer();
		
		for (UpdateDefaultValueDTO dto : dtos) {
			bfr.append("ALTER TABLE ").append(dto.getTableName()).append(" ALTER COLUMN ").append(dto.getAttributeName());
			
			if (dto.getDefaultValue() != null) {
				bfr.append(" SET DEFAULT ").append(dto.getDefaultValue());
			} else {
				bfr.append(" DROP DEFAULT");
			}			
			bfr.append(EOL);
		}
		return bfr.toString();
	}
	/**
	 * comment on table ORDERS is 'Test comment';
	 * 
	 */
	public String getUpdateTableCommentDDL(List<UpdateTableCommentDTO> dtos) {
		StringBuffer bfr = new StringBuffer();
		
		for (UpdateTableCommentDTO dto : dtos) {
			bfr.append("COMMENT ON TABLE ").append(dto.getTableName()).append(" IS ");
			if (dto.getTableComment() != null) {
				bfr.append('\'');
				bfr.append(dto.getTableComment());
				bfr.append('\'');
			}
			
			else {
				bfr.append(dto.getTableComment());
			}
			
			bfr.append(EOL);
		}
		return bfr.toString();
	}
	
	/**
	 * alter table ORDERS
	   modify column (ORDER_NUMBER character varying(11))
	 */
	
	public String getExtendVarcharColumn(List<ExtendVarcharColumnDTO> dtos) {
		StringBuffer bfr = new StringBuffer();
		
		return bfr.toString();
	}
	
	public static void main(String[] args) {
		UpdateColumnCommentDTO dto = new UpdateColumnCommentDTO();
		dto.setAttributeName("col");
		dto.setTableName("tbl");
		dto.setColumnComment("sample comment");
		System.out.print(new UpdateFactory().getUpdateColumnCommentDDL(Arrays.asList(new UpdateColumnCommentDTO[]{dto})));
		
		UpdateDefaultValueDTO dto2 = new UpdateDefaultValueDTO();
		dto2.setAttributeName("Col");
		dto2.setTableName("Tbl");
		//dto2.setDefault_value("NewDefault");
		System.out.print(new UpdateFactory().getUpdateDefaultValueDDL(Arrays.asList(new UpdateDefaultValueDTO[]{dto2})));
		
		UpdateTableCommentDTO dto3 = new UpdateTableCommentDTO();
		dto3.setTableComment("Comment");
		dto3.setTableName("Tbl");
		System.out.print(new UpdateFactory().getUpdateTableCommentDDL(Arrays.asList(new UpdateTableCommentDTO[]{dto3})));

	}

}
