package mycompany.nzr.sm;


public class StorageMgmtSqlBuilder {
public static String EOL = System.getProperty("line.separator");


	public String getHostAndDatacaptureSetSQL() {
		StringBuffer bfr = new StringBuffer();
		
		bfr
		.append("SELECT host_name || host_port as src_host, dc_set_name").append(EOL)   
		.append("FROM nzr_dc_batch b, nzr_dc_set s, nzr_host h").append(EOL)
		.append("WHERE").append(EOL)  
		.append("b.dc_skey = s.dc_skey AND ").append(EOL)
		.append("h.host_skey = s.src_host_skey AND ").append(EOL)
		.append("dc_batch_skey = ?");
		
		return bfr.toString();
	}
	
	
}
