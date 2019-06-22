package mycompany.nzr.dc.dto;

public class ScheduleDTO {

	private Long schedulerSkey;
	private Long dcSkey;
	private String scheduleName;
	private int mondayFlag;
	private int tuesdayFlag;
	private int wednesdayFlag;
	private int thursdayFlag;
	private int fridayFlag;
	private int saturdayFlag;
	private int sundayFlag;
	private int enabledFlag;
	private String batchType;

	private int startHour;
	private int startMinute;

	// private List<Integer> daysOfWeek = new ArrayList<Integer>();

	@Override
	public String toString() {
		return this.scheduleName;
	}

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

	public int getMondayFlag() {
		return mondayFlag;
	}

	public void setMondayFlag(int mondayFlag) {
		this.mondayFlag = mondayFlag;
	}

	public int getTuesdayFlag() {
		return tuesdayFlag;
	}

	public void setTuesdayFlag(int tuesdayFlag) {
		this.tuesdayFlag = tuesdayFlag;
	}

	public int getWednesdayFlag() {
		return wednesdayFlag;
	}

	public void setWednesdayFlag(int wednesdayFlag) {
		this.wednesdayFlag = wednesdayFlag;
	}

	public int getThursdayFlag() {
		return thursdayFlag;
	}

	public void setThursdayFlag(int thursdayFlag) {
		this.thursdayFlag = thursdayFlag;
	}

	public int getFridayFlag() {
		return fridayFlag;
	}

	public void setFridayFlag(int fridayFlag) {
		this.fridayFlag = fridayFlag;
	}

	public int getSaturdayFlag() {
		return saturdayFlag;
	}

	public void setSaturdayFlag(int saturdayFlag) {
		this.saturdayFlag = saturdayFlag;
	}

	public int getSundayFlag() {
		return sundayFlag;
	}

	public void setSundayFlag(int sundayFlag) {
		this.sundayFlag = sundayFlag;
	}

	public int getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(int enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

}
