package mycompany.nzr.dp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataPropagationContext {

	  public static ApplicationContext springctx;
		
		static {
			springctx = new ClassPathXmlApplicationContext(
					"springctx-allmodules-dp.xml");
			
		}
}
