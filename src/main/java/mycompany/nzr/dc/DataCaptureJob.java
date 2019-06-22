package mycompany.nzr.dc;

import mycompany.nzr.common.BatchType;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DataCaptureJob implements Job {

	private DataCaptureManager dcm ;
	
	@Override
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		
		try {
			JobDataMap jobParams = jobCtx.getTrigger().getJobDataMap();
			
			long dcSkey = jobParams.getLong(RunMe.DATASET_SKEY_PARAM_NAME);
			String batchType = jobParams.getString(RunMe.BATCH_TYPE_PARAM_NAME);
			
			System.out.println("Starting DataCaptureManager Job" + dcSkey);
			dcm = DataCaptureContext.springctx.getBean(DataCaptureManager.class);
			dcm.startBatch(dcSkey,BatchType.valueOf(batchType));
			System.out.println("DataCapture Manager Job is finished");
			
		} catch (Throwable e) {
			e.printStackTrace();
			throw new JobExecutionException();
		}
	}
 }

