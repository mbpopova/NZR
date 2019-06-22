package mycompany.nzr.dc;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class RunMe {

	public static Scheduler masterScheduler;

	private static String ON_DEMAND_JOB_TRIGGER_ID = "OnDemandJobTriggerId";
	private static String REPEATING_JOB_TRIGGER_ID = "RepeatingJobTriggerId";

	public static JobKey ON_DEMAND_CAPTURES_SCHEDULER_JOBKEY = new JobKey(
			ON_DEMAND_JOB_TRIGGER_ID);
	public static JobKey INCREMENTAL_CAPTURES_SCHEDULER_JOBKEY = new JobKey(
			REPEATING_JOB_TRIGGER_ID);

	public static int INCREMENTAL_CAPTURE_INTERVAL = 10; // in seconds
	public static int ON_DEMAND_CAPTURE_INTERVAL = 10; // in seconds

	public static String DATASET_SKEY_PARAM_NAME = "dcSetSkey";
	public static String BATCH_TYPE_PARAM_NAME = "batchType";

	public static void main(String[] args) throws Throwable {
		Class.forName("mycompany.nzr.dc.DataCaptureContext");
		masterScheduler = new StdSchedulerFactory().getScheduler();
		masterScheduler.start();

		startIncrementalScheduler();
		startOnDemandScheduler();
	}

	private static void startIncrementalScheduler() throws SchedulerException {
		Trigger repeatingJobSchedulerTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity(REPEATING_JOB_TRIGGER_ID)
				.withSchedule(
						SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(
										INCREMENTAL_CAPTURE_INTERVAL)
								.repeatForever()).build();
		JobDetail repeatingDataCaptureJobScheduler = JobBuilder
				.newJob(SchedulingJob.class)
				.withIdentity(INCREMENTAL_CAPTURES_SCHEDULER_JOBKEY).build();

		masterScheduler.scheduleJob(repeatingDataCaptureJobScheduler,
				repeatingJobSchedulerTrigger);
	}

	private static void startOnDemandScheduler() throws SchedulerException {
		Trigger onDemandJobSchedulerTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity(ON_DEMAND_JOB_TRIGGER_ID)
				.withSchedule(
						SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(
										ON_DEMAND_CAPTURE_INTERVAL)
								.repeatForever()).build();
		JobDetail onDemandDataCaptureJobScheduler = JobBuilder
				.newJob(OnDemandSchedulingJob.class)
				.withIdentity(ON_DEMAND_CAPTURES_SCHEDULER_JOBKEY).build();

		masterScheduler.scheduleJob(onDemandDataCaptureJobScheduler,
				onDemandJobSchedulerTrigger);
	}

}
