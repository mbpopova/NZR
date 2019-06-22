package mycompany.nzr.dc.test;

import mycompany.nzr.common.BatchType;
import mycompany.nzr.dc.DataCaptureManager;
import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataCaptureManagerTest extends BaseTest {

	
	@Autowired
	private DataCaptureManager dcman;
	
	@Autowired
	private DataCaptureRepoDAO dao;
	
	@Autowired
	private DataCaptureRepoSqlBuilder sqlBuilder;
	
	@Test
	public void testStartIncrementalBatch() throws Throwable {
		dcman.startBatch(785, BatchType.I);
	}
		
	@Test
	public void testStartFullBatch() throws Throwable {
		dcman.startBatch(785, BatchType.F);
	}
	
	@Test
	public void testGetPrevFullBatchCount() {
		dao.getPreviousFullBatchCount(sqlBuilder.getCountPrevFullBatch_Sql(), 11111, 741, 742);
	}
	
	
}
