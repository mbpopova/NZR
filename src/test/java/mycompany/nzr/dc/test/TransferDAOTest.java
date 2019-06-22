package mycompany.nzr.dc.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import mycompany.nzr.dc.dao.DataCaptureTransferDAO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureTransferSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransferDAOTest extends BaseTest {
	@Autowired
	private DataCaptureTransferDAO dao;

	@Autowired
	private DataCaptureTransferSqlBuilder sqlBuilder;

	@Test
	public void testCreateExternalTableRemoteTextFormat() {
		String sql = sqlBuilder.getDataCaptureRemoteTextInsertsSql("ORDERS");
		int threadCount = 4;
		
		for (int i = 1 ; i <= threadCount; i++) {
			dao.execDataCaptureInsertsSql(sql,
					"d:\\pss\\NZR\\junit-tests\\external-tables\\remoteTextExternalTbl_1.csv",
					"d:\\pss\\NZR\\junit-tests\\logs", threadCount, i, 0, 100000, 204);	
		}
		
		File file = new File("d:\\pss\\NZR\\junit-tests\\external-tables");
		String[] filenames = file.list();
		
		assertNotNull(filenames);
		assertTrue(filenames.length >= threadCount);
		
	}
	
	@Test
	public void testCreateExternalTableRemoteInternalFormat() {
		String sql = sqlBuilder.getDataCaptureRemoteInternalInsertsSql("ORDERS");
		dao.execDataCaptureInsertsSql(sql,
				"d:\\pss\\NZR\\junit-tests\\external-tables\\remoteTextExternalTbl_1.bkp",
				"d:\\pss\\NZR\\junit-tests\\logs", 4, 1, 0, 100000, 204);
	}
}
