package mycompany.nzr.dc.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import mycompany.nzr.dc.dao.DataCaptureDdlSourceDAO;
import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
import mycompany.nzr.dc.dto.CreateTableDTO;
import mycompany.nzr.dc.dto.ExtendVarcharColumnDTO;
import mycompany.nzr.dc.dto.UpdateColumnCommentDTO;
import mycompany.nzr.dc.dto.UpdateDefaultValueDTO;
import mycompany.nzr.dc.dto.UpdateOrganizeOnDTO;
import mycompany.nzr.dc.dto.UpdateTableCommentDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureDdlSourceSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DdlSourceDAOTest extends BaseTest {

	@Autowired
	private DataCaptureDdlSourceDAO dao;
	
	
	@Autowired
	private DataCaptureDdlSourceSqlBuilder sqlBuilder;
	
	@Test
	public void testGetCreateUniqueConstraintDdlData() {
		String sql = sqlBuilder.getCreateFKConstraintSQL();

		System.out.print(sql);
		List<CreateConstraintColumnDTO> dtos = dao.getConstraintData(sql, 100,
				"ORDER_AK1", 241);

		if (dtos != null && !dtos.isEmpty()) {
			CreateConstraintColumnDTO theDto = dtos.get(0);
			assertNotNull(theDto.getTableName());
			assertNotNull(theDto.getConstraintName());
			assertNotNull(theDto.getAttributeName());
			assertNotNull(theDto.getConstraintAttributePosition());
		}

	}

	@Test
	public void testGetCreatePKConstraintDdlData() {
		String sql = sqlBuilder.getCreatePKConstraintSQL();

		System.out.print(sql);
		List<CreateConstraintColumnDTO> dtos = dao.getConstraintData(sql, 100,
				"ORDER_PK", 241);

		if (dtos != null && !dtos.isEmpty()) {
			CreateConstraintColumnDTO theDto = dtos.get(0);
			assertNotNull(theDto.getTableName());
			assertNotNull(theDto.getConstraintName());
			assertNotNull(theDto.getAttributeName());
			assertNotNull(theDto.getConstraintAttributePosition());
		}

	}

	@Test
	public void testGetCreateFKConstraintDdlData() {
		String sql = sqlBuilder.getCreateFKConstraintSQL();
		// System.out.print(sql);
		List<CreateConstraintColumnDTO> dtos = dao.getConstraintData(sql, 100,
				"ORDER_FK1", 241);

		if (dtos != null && !dtos.isEmpty()) {
			CreateConstraintColumnDTO theDto = dtos.get(0);
			assertNotNull(theDto.getTableName());
			assertNotNull(theDto.getConstraintName());
			assertNotNull(theDto.getAttributeName());
			assertNotNull(theDto.getConstraintAttributePosition());
			assertNotNull(theDto.getDeleteType());
			assertNotNull(theDto.getMatchType());
			assertNotNull(theDto.getPkTableName());
			assertNotNull(theDto.getPkAttributeName());
			assertNotNull(theDto.getUpdateType());
		}

	}

	@Test
	public void testGetExtendVarcharColumn() {
		String sql = sqlBuilder.getExtendVarcharColumnSQL();

		List<ExtendVarcharColumnDTO> dtos = dao.getExtendVarcharColumnDdlData(
				sql, 100, 1000, "ORDER_NUMBER");
		if (dtos != null && !dtos.isEmpty()) {
			ExtendVarcharColumnDTO theDto = dtos.get(0);
			assertNotNull(theDto.getTableName());
			assertNotNull(theDto.getColumnName());
			assertNotNull(theDto.getNewLength());
		}
	}

	@Test
	public void testGetTableDdlData() {
		String sql = sqlBuilder.getTableSQL();
		List<CreateTableDTO> dtos = dao.getTableDdlData(sql, 4);
		if (dtos != null && !dtos.isEmpty()) {
			CreateTableDTO theDto = dtos.get(0);
			System.out.println(theDto.getAttributeName());
			assertNotNull(theDto.getAttributeName());
			// assertNotNull(theDto.getColumnPosition());
			assertNotNull(theDto.getDataLength());
			assertNotNull(theDto.getDataPrecision());
			assertNotNull(theDto.getDataScale());
			assertNotNull(theDto.getDataType());
			assertNotNull(theDto.getNullable());
			assertNotNull(theDto.getTableName());
		}
	}

	@Test
	public void testGetUpdateColumnCommentDdlData() {
		String sql = sqlBuilder.getUpdateColumnCommentSQL();
		List<UpdateColumnCommentDTO> dtos = dao.getUpdateColumnCommentDdlData(
				sql, 100, "ORDERS", "LAST_UPDATE_DT");
		if (dtos != null && !dtos.isEmpty()) {
			UpdateColumnCommentDTO theDto = dtos.get(0);
			assertNotNull(theDto.getAttributeName());
			// assertNotNull(theDto.getColumnComment());
			assertNotNull(theDto.getTableName());
		}
	}

	@Test
	public void testGetUpdateDefaultValueDdlData() {
		String sql = sqlBuilder.getUpdateDefaultValueSQL();
		List<UpdateDefaultValueDTO> dtos = dao.getUpdateDefaultValueDdlData(
				sql, 100, "ORDERS", "LAST_UPDATE_DT");
		if (dtos != null && !dtos.isEmpty()) {
			UpdateDefaultValueDTO theDto = dtos.get(0);
			assertNotNull(theDto.getAttributeName());
			// assertNotNull(theDto.getColumnComment());
			assertNotNull(theDto.getTableName());
			assertNotNull(theDto.getTableName());
		}
	}

	@Test
	public void testGetUpdateOrganizeOnDdlData() {
		String sql = sqlBuilder.getUpdateOrganizeOnSQL();
		List<UpdateOrganizeOnDTO> dtos = dao.getUpdateOrganizeOnDdlData(sql,
				100, 100, "ORDERS");

		if (dtos != null && dtos.size() > 0) {
			UpdateOrganizeOnDTO dto = dtos.get(0);
			assertNotNull(dto.getTableName());
			assertNotNull(dto.getAttributeName());
			assertNotNull(dto.getOrgPosition());
		}
	}

	@Test
	public void testGetUpdateTableCommentDdlData() {
		String sql = sqlBuilder.getUpdateTableCommentSQL();
		List<UpdateTableCommentDTO> dtos = dao.getUpdateTableCommentDdlData(
				sql, 100, "ORDERS");
		if (dtos != null && dtos.size() > 0) {
			UpdateTableCommentDTO dto = dtos.get(0);
			// assertNotNull(dto.getTableComment());
			assertNotNull(dto.getTableName());
		}
	}
}
