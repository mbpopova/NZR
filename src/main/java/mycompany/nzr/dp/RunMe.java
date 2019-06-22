package mycompany.nzr.dp;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class RunMe {

	public static Scheduler scheduler;

	private static String MASTER_JOB = "MasterJob";
	public static JobKey masterJobKey = new JobKey(MASTER_JOB);
	
	public static void main(String[] args) throws Throwable {

		Class.forName("mycompany.nzr.dp.DataPropagationContext");
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(MASTER_JOB)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(60).repeatForever())
				.build();

		JobDetail job = JobBuilder.newJob(PropagationJob.class)
				.withIdentity(masterJobKey).build();

		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
		System.out.println("Master Job has been scheduled");
	}

}
