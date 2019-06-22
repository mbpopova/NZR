package mycompany.nzr.dc.dao;

import java.util.List;
import java.util.Map;

import mycompany.nzr.common.MultiTemplateDAO;
import mycompany.nzr.dc.dto.SrcColumnDTO;
import mycompany.nzr.dc.dto.SrcConstraintAttributeDTO;
import mycompany.nzr.dc.dto.SrcTableConstraintDTO;
import mycompany.nzr.dc.dto.SrcTableDTO;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.util.StringUtils;

public class DataCaptureSrcDAO extends MultiTemplateDAO {
	//@Autowired
	//private JdbcTemplate jdbcTemplateNz;
	
	//private Map<Integer, JdbcTemplate> templateStore = new TreeMap<Integer, JdbcTemplate>();
	
	public List<SrcTableDTO> fetchTableInfo(String sql, String[] tableNames, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).query(sql, tableNames,
				ParameterizedBeanPropertyRowMapper
						.newInstance(SrcTableDTO.class));
	}

	public long fetchStableXid(String sql, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).queryForLong(sql);
	}

	public List<SrcColumnDTO> fetchColumnInfo(String sql, String[] tableNames, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).query(sql, tableNames,
				ParameterizedBeanPropertyRowMapper
						.newInstance(SrcColumnDTO.class));
	}

	public List<SrcTableConstraintDTO> fetchTableConstraintInfo(String sql,
			String[] tableNames, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).query(sql, tableNames,
				ParameterizedBeanPropertyRowMapper
						.newInstance(SrcTableConstraintDTO.class));
	}

	public List<SrcConstraintAttributeDTO> fetchConstraintColsInfo(String sql,
			String[] tableNames, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).query(sql, tableNames,
				ParameterizedBeanPropertyRowMapper
						.newInstance(SrcConstraintAttributeDTO.class));
	}

	public long getHighStableXid(String sql, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return this.templateStore.get(hostSkey).queryForLong(sql);
	}

	public long getTableRowCount(String sql, String table, long lowxid,
			long highxid, long hostSkey) {
		ensureTemplateExists(hostSkey);
		sql = StringUtils.replace(sql, "??", table);
		return this.templateStore.get(hostSkey).queryForLong(sql, lowxid, highxid);
	}

	public List<Map<String, Object>> getPkValuesOfGhostRows(String sql, long hostSkey) {
		ensureTemplateExists(hostSkey);
		return templateStore.get(hostSkey).queryForList(sql);
	}
	/*
	private String getUrl(ConfiguredSystem system) {
		return "jdbc:netezza://" + system.getDnsIp() + ":" + system.getHostPort() + "/" + system.getDatabaseName();
	}
	
	
	
	public void init(ConfiguredSystem system) {
		if (templateStore.containsKey(system.getHostSkey())) {
			return;
		}
		org.springframework.jdbc.core.JdbcTemplate template = new org.springframework.jdbc.core.JdbcTemplate();

		DataSource netezzaDatasource = new BasicDataSource();// jdbcTemplateNz.getDataSource();
		// Example URL - jdbc:netezza://192.168.233.128:5480/NZR
		((BasicDataSource) netezzaDatasource)
				.setDriverClassName("org.netezza.Driver");
		((BasicDataSource) netezzaDatasource).setUrl(getUrl(system));
		((BasicDataSource) netezzaDatasource).setPassword(system.getPassword());
		((BasicDataSource) netezzaDatasource).setUsername(system.getUserName());
		template.setDataSource(netezzaDatasource);
		templateStore.put(system.getHostSkey(), template);
	}
	
	public void createAndAssignDataSource_(ConfiguredSystem system) throws SQLException {
		DataSource netezzaDatasource =	jdbcTemplateNz.getDataSource();
		//Example URL - jdbc:netezza://192.168.233.128:5480/NZR
		((BasicDataSource)netezzaDatasource).setUrl(getUrl(system));
		((BasicDataSource)netezzaDatasource).setPassword(system.getPassword());
		((BasicDataSource)netezzaDatasource).setUsername(system.getUserName());
		jdbcTemplateNz.setDataSource(netezzaDatasource);
	}
	*/
}
