package mycompany.nzr.dc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.dto.ScheduleDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;

public class SchedulingJob implements Job {

	
	Logger logger = Logger.getLogger(this.getClass());
  
//	private static List<Long> startedScheduleIds = new ArrayList<Long>();
	// Cron Expression : second minute hour day-of-month month day-of-week year. Example:
	// cc.cronSchedule("0 0 1-5 ? * 1-5 *"); // week day 1PM - 5PM every hour.
	public static void main(String a[]) {
		new SchedulingJob().getSchedules();
	}
	private List<ScheduleDTO> getSchedules() {
		String sql = ((DataCaptureRepoSqlBuilder) DataCaptureContext.springctx
				.getBean("repoCommonSqlBuilder")).getSelectAllSchedulesSql();
		List<ScheduleDTO> l = ((DataCaptureRepoDAO) DataCaptureContext.springctx.getBean("dcRepoDAO"))
				.getSchedules(sql);
		logger.info("Got schedules: " + l);
		return l;
	}

	private List<JobKey> getJobKeys(List<ScheduleDTO> curSchedules) {
		List<JobKey> list = new ArrayList<JobKey>();
		for (ScheduleDTO s : curSchedules) {
			list.add(JobKey.jobKey(String.valueOf(s.getSchedulerSkey())));
		}
		
		return list;
	}

	private void removeOldJobs(List<ScheduleDTO> currentSchedules)
			throws Exception {
		List<JobKey> curJobs = getJobKeys(currentSchedules);
		Set<JobKey> onDemandJobs = RunMe.masterScheduler
				.getJobKeys(GroupMatcher.jobGroupEquals("OnDemandJobGroup"));
		Set<JobKey> scheduledJobs = RunMe.masterScheduler
				.getJobKeys(GroupMatcher.anyJobGroup());

		// Calculate difference. Remaining jobs - to be removed
		scheduledJobs.removeAll(curJobs);
		scheduledJobs.removeAll(onDemandJobs);
		scheduledJobs.remove(RunMe.INCREMENTAL_CAPTURES_SCHEDULER_JOBKEY);
		scheduledJobs.remove(RunMe.ON_DEMAND_CAPTURES_SCHEDULER_JOBKEY);

		for (JobKey jobKey : scheduledJobs) {
			RunMe.masterScheduler.deleteJob(jobKey);
			logger.info("Removed job with job key=" + jobKey);
			System.out.println("Schedule with SKEY=" + jobKey.getName()
					+ " has been deleted");
		}
	}
	
		/*for (Long startedScheduleId : startedScheduleIds) {
			if (!curJobs.contains(startedScheduleId))
				Runner.scheduler.deleteJob(JobKey.jobKey(String
						.valueOf(startedScheduleId)));
		}*/
		
	

	private Integer[] getDaysOfWeekArray(ScheduleDTO dto) {
		List<Integer> list = new ArrayList<Integer>();
		if (dto.getMondayFlag() == 1) {
			list.add(Calendar.MONDAY);
		}
		if (dto.getTuesdayFlag() == 1) {
			list.add(Calendar.TUESDAY);
		}
		if (dto.getWednesdayFlag() == 1) {
			list.add(Calendar.WEDNESDAY);
		}
		if (dto.getThursdayFlag() == 1) {
			list.add(Calendar.THURSDAY);
		}
		if (dto.getFridayFlag() == 1) {
			list.add(Calendar.FRIDAY);
		}
		if (dto.getSaturdayFlag() == 1) {
			list.add(Calendar.SATURDAY);
		}
		if (dto.getSundayFlag() == 1) {
			list.add(Calendar.SUNDAY);
		}
		Integer[] arr_ = new Integer[list.size()];
		list.toArray(arr_);
		return arr_;
	}
	
	private void startNewJobs(List<ScheduleDTO> currentSchedules)
			throws SchedulerException {
		List<JobKey> curJobs = getJobKeys(currentSchedules);
		Set<JobKey> scheduledJobs = RunMe.masterScheduler.getJobKeys(GroupMatcher
				.anyJobGroup());
		// Calculate difference. Remaining jobs - to be started
		curJobs.removeAll(scheduledJobs);
		DataCaptureManager dcm = DataCaptureContext.springctx
				.getBean(DataCaptureManager.class);
		dcm.init();

		for (ScheduleDTO schedule : currentSchedules) {
			JobKey jk = JobKey.jobKey(String.valueOf(schedule
					.getSchedulerSkey()));
			if (curJobs.contains(jk)) {
				JobDetail job = JobBuilder.newJob(DataCaptureJob.class)
						.withIdentity(jk).build();

				int startMin = schedule.getStartMinute();
				int startHour = schedule.getStartHour();
				Integer [] daysOfWeekArr = getDaysOfWeekArray(schedule);
				CronScheduleBuilder csb = CronScheduleBuilder
						.atHourAndMinuteOnGivenDaysOfWeek(startHour, startMin,
								daysOfWeekArr); 
								
				Trigger trigger = TriggerBuilder.newTrigger()
						.withIdentity("Trigger_" + schedule.getSchedulerSkey())
						.withSchedule(csb).build();

				JobDataMap jobParams = trigger.getJobDataMap();
				jobParams.put(RunMe.DATASET_SKEY_PARAM_NAME, schedule.getDcSkey());
				jobParams.put(RunMe.BATCH_TYPE_PARAM_NAME, schedule.getBatchType());
				RunMe.masterScheduler.scheduleJob(job, trigger);
				logger.info("Data Capture Job has been scheduled (schedule: "
								+ schedule.getSchedulerSkey()
								+ ") for DC Set: "
								+ schedule.getDcSkey()
								+ " for Hour: "
								+ schedule.getStartHour()
								+ " Minute: "
								+ schedule.getStartMinute()
								+ " On the following Days of Week: "  + getDaysOfWeekAsString(daysOfWeekArr));
			}
			
		}
	}

	private String getDaysOfWeekAsString(Integer[] daysOfWeekArr) {
		StringBuffer bfr = new StringBuffer();
		for (int i = 0; i < daysOfWeekArr.length; i++) {
			bfr.append(getDayOfWeekName(daysOfWeekArr[i]));
			bfr.append(" ");
		}
		return bfr.toString();
	}
	
	public String getDayOfWeekName(int day) {
		switch (day) {
		case 1:
			return "Sun";
		case 2:
			return "Mon";
		case 3:
			return "Tue";
		case 4:
			return "Wed";
		case 5:
			return "Thu";
		case 6:
			return "Fri";
		case 7:
			return "Sat";
		default:
			return "";
		}
	}
	
	@Override
	public void execute(JobExecutionContext jobctx)
			throws JobExecutionException {
		
		logger.info("Looking for Incremental schedules");
		try {
			List<ScheduleDTO> schedules = getSchedules();
			removeOldJobs(schedules);
			startNewJobs(schedules);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JobExecutionException();
		}
		logger.info("Done processing Incremental schedules");
	}
}
