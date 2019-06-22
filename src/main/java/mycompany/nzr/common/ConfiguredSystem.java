package mycompany.nzr.common;

public class ConfiguredSystem {
	
	private long hostSkey;
	private String databaseName;
	private String dnsIp;
	private String hostPort;
	private String userName;
	private String password;
	
	

	public long getHostSkey() {
		return hostSkey;
	}

	public void setHostSkey(long hostSkey) {
		this.hostSkey = hostSkey;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDnsIp() {
		return dnsIp;
	}

	public void setDnsIp(String dnsIp) {
		this.dnsIp = dnsIp;
	}

	public String getHostPort() {
		return hostPort;
	}

	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
