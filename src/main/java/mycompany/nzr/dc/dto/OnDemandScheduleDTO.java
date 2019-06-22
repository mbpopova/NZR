package mycompany.nzr.dc.dto;

import java.util.Date;

public class OnDemandScheduleDTO {
	private Long schedulerSkey;
	private Long dcSkey;
	private String scheduleName;
	private Date startTime;
	private String batchType;

	public Long getSchedulerSkey() {
		return schedulerSkey;
	}

	public void setSchedulerSkey(Long schedulerSkey) {
		this.schedulerSkey = schedulerSkey;
	}

	public Long getDcSkey() {
		return dcSkey;
	}

	public void setDcSkey(Long dcSkey) {
		this.dcSkey = dcSkey;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return this.scheduleName;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

}
