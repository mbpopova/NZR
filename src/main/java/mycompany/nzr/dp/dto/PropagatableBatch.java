package mycompany.nzr.dp.dto;

import java.util.Date;




public class PropagatableBatch {

	private long dcBatchSkey;
	private long dcSkey;
	private long subscriptionSkey;
	private int dropIfExists;
	
	
	public int getDropIfExists() {
		return dropIfExists;
	}

	public void setDropIfExists(int dropIfExists) {
		this.dropIfExists = dropIfExists;
	}

	private String dcBatchType;
	private Date dcBatchStartTs;


	public Date getDcBatchStartTs() {
		return dcBatchStartTs;
	}

	public void setDcBatchStartTs(Date dcBatchStartTs) {
		this.dcBatchStartTs = dcBatchStartTs;
	}

	public String getDcBatchType() {
		return dcBatchType;
	}

	public void setDcBatchType(String dcBatchType) {
		this.dcBatchType = dcBatchType;
	}

	public long getDcBatchSkey() {
		return dcBatchSkey;
	}

	public void setDcBatchSkey(long dcBatchSkey) {
		this.dcBatchSkey = dcBatchSkey;
	}

	public long getDcSkey() {
		return dcSkey;
	}

	public void setDcSkey(long dcSkey) {
		this.dcSkey = dcSkey;
	}

	public long getSubscriptionSkey() {
		return subscriptionSkey;
	}

	public void setSubscriptionSkey(long subscriptionSkey) {
		this.subscriptionSkey = subscriptionSkey;
	}

}
