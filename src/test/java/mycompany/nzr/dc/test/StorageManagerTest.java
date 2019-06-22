package mycompany.nzr.dc.test;

import mycompany.nzr.common.FileLocation;
import mycompany.nzr.common.Output;
import mycompany.nzr.sm.StorageManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageManagerTest extends BaseTest {

@Autowired
private StorageManager storageManager;

@Test
public void testCreateDirectory() throws Exception {
	String path = storageManager.getOrCreateDirectory(1, "Customers", Output.DDL_MISC, FileLocation.REMOTE);
	System.out.println(path);
	path = storageManager.getOrCreateDirectory(1, "Orders", Output.DDL_MISC, FileLocation.REMOTE);
	System.out.println(path);
}

}

