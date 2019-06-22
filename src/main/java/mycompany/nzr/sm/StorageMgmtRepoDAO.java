package mycompany.nzr.sm;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class StorageMgmtRepoDAO  {
	@Autowired
	private StorageMgmtSqlBuilder smRepoSqlBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplateOra;

	public Map<String, Object> getHostAndDatacaptureSet(long batchId) {
		String sql = smRepoSqlBuilder.getHostAndDatacaptureSetSQL();
		Map<String, Object> result = jdbcTemplateOra.queryForMap(sql, batchId);
		return result;
	}

}
