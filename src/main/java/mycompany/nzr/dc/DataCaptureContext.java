package mycompany.nzr.dc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataCaptureContext {

	  public static ApplicationContext springctx;
		
		static {
			springctx = new ClassPathXmlApplicationContext(
					"springctx-allmodules-dc.xml");
			
		}
}
