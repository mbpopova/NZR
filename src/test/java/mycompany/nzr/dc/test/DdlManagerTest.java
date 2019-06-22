package mycompany.nzr.dc.test;

import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.dc.DdlManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DdlManagerTest extends BaseTest {

	@Autowired
	private DdlManager ddlManager;
	
	
	/*
	 * To test - manipulate the following 2 table column values:
nzr_wrk_table;
nzr_wrk_table_attr;
	 */
	@Test
	public void testCreateDdl() throws Exception {
      ddlManager.createDiffDdl( 242, 243, 189, 1, "CUSTOMER", FileLocation.REMOTE, FileFormat.T);
	}
}
