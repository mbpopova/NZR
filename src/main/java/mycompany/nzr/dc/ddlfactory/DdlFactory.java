package mycompany.nzr.dc.ddlfactory;

import java.util.List;
import java.util.Map;

import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
import mycompany.nzr.dc.dto.CreateTableDTO;
import mycompany.nzr.dc.dto.ExtendVarcharColumnDTO;
import mycompany.nzr.dc.dto.RenameColumnDTO;
import mycompany.nzr.dc.dto.TableChangeIndicatorsDTO;
import mycompany.nzr.dc.dto.UpdateColumnCommentDTO;
import mycompany.nzr.dc.dto.UpdateDefaultValueDTO;
import mycompany.nzr.dc.dto.UpdateOrganizeOnDTO;
import mycompany.nzr.dc.dto.UpdateTableCommentDTO;

import org.springframework.beans.factory.annotation.Autowired;

public class DdlFactory {

	@Autowired
	private CreateConstraintFactory createConstraintFactory;
	@Autowired
	private CreateTableFactory createTblFactory;
	@Autowired
	private DropConstraintFactory dropConstraintFactory;
	@Autowired
	private DropRenameTableFactory dropRenameTablefactory;
	@Autowired
	private ExtendVarcharColumnFactory extendVarcharColumnFactory;
	@Autowired
	private RenameColumnFactory renameColumnFactory;
	@Autowired
	private UpdateFactory updateFactory;
	@Autowired
	private UpdateOrganizeOnFactory updateOrganizeOnFactory;

	
	public List<String> getCreatePKConstraintDDL(
			List<CreateConstraintColumnDTO> allPkColumns) {
		return createConstraintFactory.getCreatePKConstraintDDL(allPkColumns);
	}

	public List<String> getCreateFKConstraintDDL(
			List<CreateConstraintColumnDTO> allPkColumns) {
		return createConstraintFactory.getCreateFKConstraintDDL(allPkColumns);
	}

	public List<String> getCreateUniqueConstraintDDL(
			List<CreateConstraintColumnDTO> allPkColumns) {
		return createConstraintFactory
				.getCreateUniqueConstraintDDL(allPkColumns);
	}

	public String getCreateTableDDL(List<CreateTableDTO> dtos, String tableName) {
		return createTblFactory.getCreateTableDDL(dtos, tableName);
	}

	public String getCreateTempTableDDL(List<CreateTableDTO> dtos, String tableName) {
		return createTblFactory.getCreateTableDDL(dtos, tableName);
	}
	
	public String getDropConstraintDDL(
			List<CreateConstraintColumnDTO> allFkcColumns) {
		return dropConstraintFactory.getDropConstraintDDL(allFkcColumns);
	}

	public String getDropTableDDL(List<String> tableNames) {
		return dropRenameTablefactory.getDropTableDDL(tableNames);
	}

	public String getRenameTableDDL(Map<String, String> oldNewTablePairs) {
		return dropRenameTablefactory.getRenameTableDDL(oldNewTablePairs);
	}

	public String getExtendVarcharColumnDDL(List<ExtendVarcharColumnDTO> dtos) {
		return this.extendVarcharColumnFactory.getExtendVarcharColumnDDL(dtos);
	}

	public String getRenameColumnDDL(List<RenameColumnDTO> dtos) {
		return this.renameColumnFactory.getRenameColumnDDL(dtos);
	}

	public String getUpdateOrganizeOn(List<UpdateOrganizeOnDTO> dtos) {
		return this.updateOrganizeOnFactory.getUpdateOrganizeOn(dtos);
	}

	public String getUpdateDefaultValueDDL(List<UpdateDefaultValueDTO> dtos) {
		return this.updateFactory.getUpdateDefaultValueDDL(dtos);
	}

	public String getUpdateTableCommentDDL(List<UpdateTableCommentDTO> dtos) {
		return this.updateFactory.getUpdateTableCommentDDL(dtos);
	}

	public String getUpdateColumnCommentDDL(List<UpdateColumnCommentDTO> dtos) {
	 return this.updateFactory.getUpdateColumnCommentDDL(dtos);	
	}
}
