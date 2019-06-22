package mycompany.nzr.dc.dao;

import java.util.List;

import mycompany.nzr.dc.dto.CreateConstraintColumnDTO;
import mycompany.nzr.dc.dto.CreateTableDTO;
import mycompany.nzr.dc.dto.ExtendVarcharColumnDTO;
import mycompany.nzr.dc.dto.RenameColumnDTO;
import mycompany.nzr.dc.dto.UpdateColumnCommentDTO;
import mycompany.nzr.dc.dto.UpdateDefaultValueDTO;
import mycompany.nzr.dc.dto.UpdateOrganizeOnDTO;
import mycompany.nzr.dc.dto.UpdateTableCommentDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

public class DataCaptureDdlSourceDAO {

	@Autowired
	private JdbcTemplate jdbcTemplateOra;
	
	
	public List<CreateConstraintColumnDTO> getConstraintData(String sql,
			long tableSkey, long batchSkey) {
		return jdbcTemplateOra.query(sql,
				new Object[] { tableSkey, batchSkey },
				ParameterizedBeanPropertyRowMapper
						.newInstance(CreateConstraintColumnDTO.class));
	}

	public List<CreateConstraintColumnDTO> getConstraintData(String sql,
			long tableSkey, String constraintName, long batchSkey) {
		return jdbcTemplateOra.query(sql, new Object[] { tableSkey,
				constraintName, batchSkey }, ParameterizedBeanPropertyRowMapper
				.newInstance(CreateConstraintColumnDTO.class));
	}

	public List<ExtendVarcharColumnDTO> getExtendVarcharColumnDdlData(
			String sql, long batchSkey, long tableSkey, String newColumnName) {
		return jdbcTemplateOra.query(sql, new Object[] { tableSkey,
				newColumnName, batchSkey }, ParameterizedBeanPropertyRowMapper
				.newInstance(ExtendVarcharColumnDTO.class));
	}

	/*
	 * WITH data AS 
		(
				SELECT
						old_ta.table_skey old_tableskey,
						old_ta.attribute_name
						oldattr,
						new_ta.table_skey new_tableskey,
						new_ta.attribute_name newattr
				FROM
				(SELECT * FROM nzr_dc_detail_attr_hist WHERE table_name = ? AND	dc_batch_skey = ?) old_ta
				INNER JOIN 	(SELECT * FROM nzr_dc_detail_attr_hist WHERE dc_batch_skey = ? AND	table_name = ?) new_ta
				  ON (new_ta.attribute_position = old_ta.attribute_position)
		) 
		SELECT * FROM data WHERE newattr = ?
	 */
	public List<RenameColumnDTO> getRenameColumnDdlData(String sql,
			long prevBatchSkey, long batchSkey, long tableSkey, String newAttrName) {
		return jdbcTemplateOra.query(sql, new Object[] { tableSkey, prevBatchSkey, batchSkey, tableSkey, newAttrName},
				ParameterizedBeanPropertyRowMapper
						.newInstance(RenameColumnDTO.class));
	}

	public List<CreateTableDTO> getTableDdlData(String sql, long tableSkey) {
		Long args[] = new Long[] { tableSkey };
		return jdbcTemplateOra.query(sql, args,
				ParameterizedBeanPropertyRowMapper
						.newInstance(CreateTableDTO.class));
	}

	public List<UpdateColumnCommentDTO> getUpdateColumnCommentDdlData(
			String sql, long batchSkey, String tableName, String columnName) {
		String args[] = new String[] { tableName, columnName,
				String.valueOf(batchSkey) };
		return jdbcTemplateOra.query(sql, args,
				ParameterizedBeanPropertyRowMapper
						.newInstance(UpdateColumnCommentDTO.class));
	}

	public List<UpdateDefaultValueDTO> getUpdateDefaultValueDdlData(String sql,
			long batchSkey, String tableName, String columnName) {
		String args[] = new String[] { tableName, columnName,
				String.valueOf(batchSkey) };
		return jdbcTemplateOra.query(sql, args,
				ParameterizedBeanPropertyRowMapper
						.newInstance(UpdateDefaultValueDTO.class));
	}

	public List<UpdateOrganizeOnDTO> getUpdateOrganizeOnDdlData(String sql, long prevBatchSkey, 
			long batchSkey, String tableName) {
		return jdbcTemplateOra.query(sql, new Object[]{prevBatchSkey, batchSkey, tableName},
				ParameterizedBeanPropertyRowMapper
						.newInstance(UpdateOrganizeOnDTO.class));
	}

	public List<UpdateTableCommentDTO> getUpdateTableCommentDdlData(String sql,
			long batchSkey, String tableName) {
		String args[] = new String[] { tableName, String.valueOf(batchSkey) };
		return jdbcTemplateOra.query(sql, args,
				ParameterizedBeanPropertyRowMapper
						.newInstance(UpdateTableCommentDTO.class));
	}
}
