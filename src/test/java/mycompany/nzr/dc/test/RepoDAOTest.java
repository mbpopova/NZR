package mycompany.nzr.dc.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mycompany.nzr.common.BatchStatus;
import mycompany.nzr.common.BatchType;
import mycompany.nzr.dc.DataCaptureContext;
import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.dto.ColumnChangeIndicatorsDTO;
import mycompany.nzr.dc.dto.DcSetDetailDTO;
import mycompany.nzr.dc.dto.OnDemandScheduleDTO;
import mycompany.nzr.dc.dto.SrcColumnDTO;
import mycompany.nzr.dc.dto.SrcConstraintAttributeDTO;
import mycompany.nzr.dc.dto.SrcTableConstraintDTO;
import mycompany.nzr.dc.dto.SrcTableDTO;
import mycompany.nzr.dc.dto.TableChangeIndicatorsDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureTransferSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureDdlSourceSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
public class RepoDAOTest extends BaseTest{

	@Autowired
	private DataCaptureRepoDAO repoDAO;
	
	@Autowired
	private DataCaptureRepoSqlBuilder repoSQLBuilder;
	@Autowired
	private DataCaptureTransferSqlBuilder datatransferSQLBuilder;
	@Autowired
	private DataCaptureDdlSourceSqlBuilder ddlSourceSQLBuilder;
	
	@Test
	public void testGetODSchedules() {
		String sql = repoSQLBuilder.getOnDemandSchedules_Sql();
		System.out.println(repoDAO.getOnDemandSchedules(sql));
	}
	
	@Test
	public void testSqlStmtPopulation() {
		Map<Object, Object> map1 = this.datatransferSQLBuilder.getSql();
		assertTrue(map1.size() > 0);
		
		Map<Object, Object> map2 = this.ddlSourceSQLBuilder.getSql();
		assertTrue(map2.size() > 0);
	}
	
	@Test
	public void testFetchDataCaptureSetConfig() {
		String sql  = repoSQLBuilder.getSelectDataCaptureSetInfoSQL();
		Map<String, Object> result = repoDAO.fetchDataCaptureSetConfig(sql, 1);
		
		System.out.print(result);
	}
	
	@Test
	public void testFetchDataCaptureSetTables() {
		String sql = repoSQLBuilder.getSelectTablesInDataCaptureSetSQL();
		List<DcSetDetailDTO> result = repoDAO.fetchDataCaptureSetDetails(sql, 1);
		System.out.print(result);
	}
	
	@Test
	public void testInsertNewBatchInfo() {
		String sql = this.repoSQLBuilder.getInsertBatchInfoSQL();
		repoDAO.insertNewBatchInfo(sql, 1, 1, 100l, BatchStatus.RUNNING.toString(), BatchType.F);
	}
	
	@Test
	public void testInsertTempTableData() {
		SrcTableDTO tbl = new SrcTableDTO();
		tbl.setTableSkey(1);
		tbl.setTableName("CUSTOMER");
		tbl.setObjId(123l);
		tbl.setCreateDate("2012-01-01 22:00:00");
		tbl.setRelnatts(2);
		tbl.setDescription("test descr");

		List<SrcTableDTO> list = new ArrayList<SrcTableDTO>();
		list.add(tbl);
		repoDAO.insertTableInfoWrk(
				repoSQLBuilder.getInsertTableInfoSQL(), 1, 1, list);

	}
	
	@Test
	public void testInsertColumnInfo() {
		SrcColumnDTO col = new SrcColumnDTO();
		col.setColumnComment("Test Comment");
		col.setColumnName("CITY");
		col.setColumnPosition(1);
		col.setDataType("CHARACTER VARYING(100)");
		col.setDefaultValue("");
		col.setTableName("ADDRESS");
		col.setObjId(217351l);
		col.setTableSkey(1);
		col.setNullable(0);
		
		SrcColumnDTO[] cols = new SrcColumnDTO[]{col};
		repoDAO.insertColumnInfoWrk(repoSQLBuilder.getInsertColumnInfoSQL(), 1, 1, Arrays.asList(cols));
		
	}
	
	@Test
	public void testInsertTableConstraintInfo() {
		SrcTableConstraintDTO c = new SrcTableConstraintDTO();
		c.setConstraintName("CUSTOMER_PK");
		c.setConType("p");
		c.setTableSkey(1);
		SrcTableConstraintDTO[] constraints = new SrcTableConstraintDTO[]{c};
		repoDAO.insertTableConstraintInfoWrk(repoSQLBuilder.getInsertTableConstraintInfoSQL(),1,1, Arrays.asList(constraints));
	}
	
	@Test
	public void testInsertConstraintColumnsInfo() {
		SrcConstraintAttributeDTO c = new SrcConstraintAttributeDTO();
		c.setAttName("CUSTOMER_SKEY");
		c.setConseq(1);
		c.setConstraintName("CUSTOMER_PK");
		c.setTableSkey(1);
		SrcConstraintAttributeDTO[] cols = new SrcConstraintAttributeDTO[]{c};
		repoDAO.insertConstraintColumnsInfoWrk(repoSQLBuilder.getInsertConstraintColumnInfoSQL(), 1, 1, Arrays.asList(cols));
	}

	@Test
	public void testGetTableChangeIndicators() {
		String sql = repoSQLBuilder.getTableChangedIndSql();
		TableChangeIndicatorsDTO list = repoDAO.getTableChangeIndicators(sql,
				242, 243, "CUSTOMER");

		assertTrue(list != null);
	}

	@Test
	public void testGetTableOrgChangeIndicators() {
		String sql = repoSQLBuilder.getTableOrgChangedIndSql();
		TableChangeIndicatorsDTO list = repoDAO.getTableChangeIndicators(sql,
				242, 243, "CUSTOMER");

		assertTrue(list != null);
	}

	@Test
	public void testGetColumnChangeIndicators() {
		String sql = repoSQLBuilder.getColumnChangedIndSql();
		List<ColumnChangeIndicatorsDTO> list = repoDAO
				.getColumnChangeIndicators(sql, 242, 243, "CUSTOMER");

		assertTrue(list != null);
	}

}
