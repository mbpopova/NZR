package mycompany.nzr.dc.test;

import static org.junit.Assert.fail;
import mycompany.nzr.common.ConfiguredSystem;
import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.dc.TransferManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransferManagerTest extends BaseTest{

	@Autowired
	private TransferManager manager;
	
	
	/*
	 * LOCAL means local to Netezza = VM (Unix).
	 * REMOTE means NZR server
	 */
	@Test
	public void testTransferTextRemote() throws Exception {
		manager.transferInserts(1, 217399, "ORDERS", FileFormat.T,
				FileLocation.REMOTE, 0, 10000000, 1, 1, new ConfiguredSystem());
	}

	@Test
	public void testTransferInternalRemote() throws Exception {
		manager.transferInserts(1, 217399, "ORDERS", FileFormat.I,
				FileLocation.REMOTE, 0, 10000000, 1, 1, new ConfiguredSystem());
	}

	@Test
	public void testTransferTextLocal() throws Exception {
		Exception ex = null;
		try {
			manager.transferInserts(1, 217399, "ORDERS", FileFormat.T,
					FileLocation.LOCAL, 0, 10000000, 1, 1, new ConfiguredSystem());
		} catch (Exception e) {
			ex = e;
		}

		if (ex == null) {
			fail("Should have given error that root does not corespond to file target location");
		}
	}

	@Test
	public void testTransferInternalLocal() throws Exception {
		Exception ex = null;
		try {
			manager.transferInserts(1, 217399, "ORDERS", FileFormat.T,
					FileLocation.LOCAL, 0, 10000000, 1, 1, new ConfiguredSystem());
		} catch (Exception e) {
			ex = e;
		}

		if (ex == null) {
			fail("Should have given error that root does not corespond to file target location");
		}
	}
	
}
