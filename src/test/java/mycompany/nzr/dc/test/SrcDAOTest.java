package mycompany.nzr.dc.test;

import java.util.List;

import mycompany.nzr.dc.dao.DataCaptureSrcDAO;
import mycompany.nzr.dc.dto.SrcColumnDTO;
import mycompany.nzr.dc.dto.SrcConstraintAttributeDTO;
import mycompany.nzr.dc.dto.SrcTableConstraintDTO;
import mycompany.nzr.dc.dto.SrcTableDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureSrcSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SrcDAOTest extends BaseTest {
	String[] tables = new String[2];
	
	{
		tables[0] = "CUSTOMER";
		tables[1] ="ADDRESS";
	}
	
	@Autowired
	private DataCaptureSrcSqlBuilder sqlBuilder;
	
	@Autowired
	private DataCaptureSrcDAO dao;
	
	
	@Test
	public void testFetchTableConfig() {
		List<SrcTableDTO> srcTables = dao.fetchTableInfo(sqlBuilder.getSelectTableInfoSQL(2), tables, 204);
		System.out.println(srcTables.get(0));
	}
	
	@Test
	public void testFetchStableXid() {
		long xid = dao.fetchStableXid(sqlBuilder.getSelectStableXidSQL(), 204);
		System.out.println(xid);
	}
	
	@Test
	public void testFetchColumnInfo() {
		List<SrcColumnDTO> columns = dao.fetchColumnInfo(sqlBuilder.getSelectColumnInfoSQL(2), tables, 204);
		System.out.println(columns.get(0));
	}
	
	@Test
	public void testFetchTableConstraintInfo() {
		List<SrcTableConstraintDTO> columns = dao.fetchTableConstraintInfo(sqlBuilder.getSelectTableConstraintInfoSQL(2), tables, 204);
		System.out.println(columns.get(0));
	}
	
	@Test
	public void testFetchConstraintColumnInfo() {
		List<SrcConstraintAttributeDTO> columns = dao.fetchConstraintColsInfo(sqlBuilder.getSelectConstraintColsInfoSQL(2), tables, 204);
		System.out.println(columns.get(0));
	}
}
