package mycompany.nzr.dp;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PropagationJob implements Job {

	@Override
	public void execute(JobExecutionContext jobctx)
			throws JobExecutionException {
		try {
			System.out.println("Starting Propagation Job");
			PropagationManager pm = DataPropagationContext.springctx
					.getBean(PropagationManager.class);
			pm.propagate();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new JobExecutionException();
		}
	}
}
