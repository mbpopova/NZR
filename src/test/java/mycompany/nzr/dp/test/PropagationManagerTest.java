package mycompany.nzr.dp.test;

import mycompany.nzr.dp.test.BaseTest;
import mycompany.nzr.dp.PropagationException;
import mycompany.nzr.dp.PropagationManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PropagationManagerTest extends BaseTest {
	
	@Autowired
	private PropagationManager pm;
	
	@Test
	public void testPropagate() throws PropagationException {
		pm.propagate();
	}

}
