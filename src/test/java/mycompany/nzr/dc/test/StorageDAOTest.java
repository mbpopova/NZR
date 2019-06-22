package mycompany.nzr.dc.test;

import java.util.Map;

import mycompany.nzr.sm.StorageMgmtRepoDAO;
import mycompany.nzr.sm.StorageMgmtSqlBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageDAOTest extends BaseTest {
	@Autowired
	private StorageMgmtSqlBuilder sqlGenerator;
	
	@Autowired
	private StorageMgmtRepoDAO storageDAO; 
	
	@Autowired
	private StorageMgmtRepoDAO jdbcStorageDAO;
	
	@Test
	public void testGetHostAndDatacaptureSet() throws Exception {
		Map<String, Object> folders = storageDAO.getHostAndDatacaptureSet(1);		
		System.out.print(folders);
	}
	
	@Test
	public void testSQLGenerator() {
		String sql = sqlGenerator.getHostAndDatacaptureSetSQL();
		System.out.print(sql);
	}
	
	@Test
	public void testJDBCStorageDAO() {
		Map<String,Object> result = jdbcStorageDAO.getHostAndDatacaptureSet(1);
		System.out.print(result);
	}
	
	}
