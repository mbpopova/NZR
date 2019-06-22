package mycompany.nzr.dc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.dto.OnDemandScheduleDTO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class OnDemandSchedulingJob implements Job {

	private static List<Long> jobsToBeRun = new ArrayList<Long>();
	Logger logger = Logger.getLogger(this.getClass());
	
	private void doSchedule(OnDemandScheduleDTO sc) {
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("OnDemandJobTrigger" + sc.getSchedulerSkey())
				.startAt(sc.getStartTime()).build();
		JobDetail job = JobBuilder.newJob(DataCaptureJob.class)
				.withIdentity(sc.getScheduleName() + "-"+ sc.getSchedulerSkey(), "OnDemandJobGroup").build();

		JobDataMap jobParams = trigger.getJobDataMap();
		jobParams.put(RunMe.DATASET_SKEY_PARAM_NAME, sc.getDcSkey());
		jobParams.put(RunMe.BATCH_TYPE_PARAM_NAME, sc.getBatchType());
		
		try {
			RunMe.masterScheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Looking for schedules...");	
		List<OnDemandScheduleDTO> schedules = getSchedules();
		boolean run = false;
//	logger.info("Got schedules: "  + schedules.toString());	
		synchronized (this) {
			for (OnDemandScheduleDTO sc : schedules) {
				if (sc.getStartTime().before(new Date())) {
					//jobsToBeRun.remove(sc.getSchedulerSkey());
					continue;
				} else {
					
					if (!jobsToBeRun.contains(sc.getSchedulerSkey())) {
						doSchedule(sc);
						run = true;
						logger.info(sc.getScheduleName() + " has been scheduled");
						jobsToBeRun.add(sc.getSchedulerSkey());
					}
				}
			}
		}
		if (run == false) {
			logger.info("Nothing to schedule.");
		}
	}

	private List<OnDemandScheduleDTO> getSchedules() {
		String sql = ((DataCaptureRepoSqlBuilder) DataCaptureContext.springctx
				.getBean("repoCommonSqlBuilder")).getOnDemandSchedules_Sql();
		return ((DataCaptureRepoDAO) DataCaptureContext.springctx
				.getBean("dcRepoDAO")).getOnDemandSchedules(sql);
	}

}
