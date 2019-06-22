package mycompany.nzr.dp.dao;

import java.util.List;
import java.util.Set;

import mycompany.nzr.common.MultiTemplateDAO;
import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

public class TargetDAO extends MultiTemplateDAO {

	public int insertFromExternalFile(String sql, String tableName,
			String externalFile, String logDir, long hostSkey) {
		return templateStore.get(hostSkey).update(sql, logDir);
	}

	public void executeDdl(String sql, long hostSkey) {
		templateStore.get(hostSkey).execute(sql);
	}

	public int performDelete(String createTempTblSql,
			String insertIntoTempTblSql, String deleteFromTargetTblSql,
			String tempTable, String externalFile, String logDir, long hostSkey) {
		try {
			templateStore.get(hostSkey).execute(createTempTblSql);
			templateStore.get(hostSkey).update(insertIntoTempTblSql, logDir);
			return templateStore.get(hostSkey).update(deleteFromTargetTblSql);
		} finally {
			try {
				templateStore.get(hostSkey).execute("DROP TABLE " + tempTable);
			} catch (Exception e) {
			}
		}
	}

	public void executeDdlBatched(String[] sql, long hostSkey) {
		templateStore.get(hostSkey).batchUpdate(sql);
	}

	public void executeDdlBatched(String sql, List<Object[]> args, long hostSkey) {
		templateStore.get(hostSkey).batchUpdate(sql, args);
	}

	public List<CreateConstraintColumnDTO> getConstraints(String sql,
			Set<String> tableNames, long hostSkey) {
		return templateStore.get(hostSkey).query(
				sql,
				tableNames.toArray(),
				ParameterizedBeanPropertyRowMapper
						.newInstance(CreateConstraintColumnDTO.class));
	}

	public int checkTableExists(String sql, String tableName, long hostSkey) {
		return templateStore.get(hostSkey).queryForInt(sql,
				new Object[] { tableName });

	}

}
