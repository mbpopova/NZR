package mycompany.nzr.dc.test;

import mycompany.nzr.dc.sqlbuilder.DataCaptureDdlSourceSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DdlSourceSqlBuilderTest extends BaseTest {
	@Autowired
	private DataCaptureDdlSourceSqlBuilder ddlSourceSQLBuilder;

	@Test
	public void testGetCreateFConstraintSQL() {
		System.out.print(ddlSourceSQLBuilder.getCreateFKConstraintSQL());
	}

	@Test
	public void testGetExtendVarcharColumnSQL() {
		System.out.print(ddlSourceSQLBuilder.getExtendVarcharColumnSQL());
	}

	@Test
	public void testGetPrimaryKeyConstraintSQL() {
		System.out.print(ddlSourceSQLBuilder.getCreatePKConstraintSQL());
	}

	@Test
	public void testGetRenameColumnSQL() {
		System.out.print(ddlSourceSQLBuilder.getRenameColumnSQL());
	}

	@Test
	public void testGetTableSQL() {
		System.out.print(ddlSourceSQLBuilder.getTableSQL());
	}

	@Test
	public void testGetUniqueKeyConstraintSQL() {
		System.out.print(ddlSourceSQLBuilder.getCreateUniqueKeyConstraintSQL());
	}

	@Test
	public void testGetUpdateColumnCommentSQL() {
		System.out.print(ddlSourceSQLBuilder.getUpdateColumnCommentSQL());
	}

	@Test
	public void testGetUpdateDefaultValueSQL() {
		System.out.print(ddlSourceSQLBuilder.getUpdateDefaultValueSQL());
	}

	@Test
	public void testGetUpdateOrganizeOnSQL() {
		System.out.print(ddlSourceSQLBuilder.getUpdateOrganizeOnSQL());
	}

	@Test
	public void testGetUpdateTableCommentSQL() {
		System.out.print(ddlSourceSQLBuilder.getUpdateTableCommentSQL());
	}
}
