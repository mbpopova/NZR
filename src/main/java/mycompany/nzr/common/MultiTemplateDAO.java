package mycompany.nzr.common;

import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class MultiTemplateDAO {
	protected Map<Long, JdbcTemplate> templateStore = new TreeMap<Long, JdbcTemplate>();
	private static String DRIVER_CLASS = "org.netezza.Driver";

	public void createAndStoreTemplate(ConfiguredSystem system) {
		if (templateStore.containsKey(system.getHostSkey())) {
			return;
		}
		org.springframework.jdbc.core.JdbcTemplate template = new org.springframework.jdbc.core.JdbcTemplate();

		DataSource netezzaDatasource = new BasicDataSource();
		// Example URL - jdbc:netezza://192.168.233.128:5480/NZR
		((BasicDataSource) netezzaDatasource).setDriverClassName(DRIVER_CLASS);
		((BasicDataSource) netezzaDatasource).setUrl(getUrl(system));
		((BasicDataSource) netezzaDatasource).setPassword(system.getPassword());
		((BasicDataSource) netezzaDatasource).setUsername(system.getUserName());
		template.setDataSource(netezzaDatasource);
		templateStore.put(system.getHostSkey(), template);
	}

	private String getUrl(ConfiguredSystem system) {
		return "jdbc:netezza://" + system.getDnsIp() + ":"
				+ system.getHostPort() + "/" + system.getDatabaseName();
	}

	protected void ensureTemplateExists(long hostSkey) {
		if (!templateStore.containsKey(hostSkey)) {
			throw new IllegalStateException(
					"Data Access Object is not configured to access system "
							+ hostSkey);
		}
	}
}
