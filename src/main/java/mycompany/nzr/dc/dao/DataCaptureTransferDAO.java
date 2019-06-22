package mycompany.nzr.dc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import mycompany.nzr.common.MultiTemplateDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataCaptureTransferDAO extends MultiTemplateDAO{

//	@Autowired
	//private JdbcTemplate jdbcTemplateNz;
	
	public int execDataCaptureInsertsSql(String sql, String externalTableName,
			String logDirectory, int parallelThreadCount, int currentThreadNum,
			long lowxid, long highxid, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).update(sql, externalTableName, logDirectory,
				parallelThreadCount, currentThreadNum, lowxid, highxid);
	}
	
	public int execDataCaptureDeletesSql(String sql, String externalTableName,
			String logDirectory, long lowxid, long highxid, long hostSkey) {
		ensureTemplateExists(hostSkey);
		int cnt = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = templateStore.get(hostSkey).getDataSource().getConnection();
			if (conn.getAutoCommit() == true)
				conn
						.setAutoCommit(false);

			ps = conn.prepareStatement("set show_deleted_records = true");
			ps.execute();
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, externalTableName);
			ps.setString(2, logDirectory);
			ps.setLong(3, lowxid);
			ps.setLong(4,  highxid);
			ps.setLong(5, lowxid);
			
			cnt = ps.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
		   //templateStore.get(hostSkey).update("set show_deleted_records = true");
		//	cnt = templateStore.get(hostSkey).update(sql, externalTableName, logDirectory,
		//			lowxid, highxid, lowxid);
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cnt;
	}
	
}
