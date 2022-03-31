package com.eplugger.onekey.utils.sqlFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.util.DBUtil;
import com.eplugger.util.OtherUtils;

public class ProduceSqlFactory {
	/**
	 * 生产数据库表格sql代码
	 * @param module
	 * @param databaseType
	 * @param authorSwitch
	 * @param joinColumn
	 * @param mainTableName
	 * @return
	 */
	public static String produceCreateTableSql(ModuleInfo module, boolean authorSwitch, String joinColumn, String mainTableName) {
		StringBuffer sb = new StringBuffer(), endsb = new StringBuffer();
		String tableName = module.getTableName();
		List<Field> fieldList = module.getFields();
		if (DBUtil.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[").append(tableName).append("] (").append(OtherUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ,").append(OtherUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			if (authorSwitch) {
				endsb.append(",").append(OtherUtils.CRLF);
				endsb.append("CONSTRAINT [FK_").append(module.getTableName()).append("__").append(mainTableName).append("] FOREIGN KEY ([").append(SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn)).append("]) REFERENCES [dbo].[").append(mainTableName).append("] ([ID]) ON DELETE NO ACTION ON UPDATE NO ACTION").append(OtherUtils.CRLF);
			} else {
				endsb.append(OtherUtils.CRLF);
			}
			endsb.append(")").append(OtherUtils.CRLF);
			endsb.append("GO").append(OtherUtils.CRLF);
		} else if (DBUtil.isOracle()) {
			sb.append("CREATE TABLE \"").append(DBUtil.getDatabaseName()).append("\".").append(tableName).append(" (").append(OtherUtils.CRLF);
			sb.append("\"ID\" VARCHAR2(32) NOT NULL ,").append(OtherUtils.CRLF);
			
			endsb.append("PRIMARY KEY (\"ID\")");
			if (authorSwitch) {
				endsb.append(",").append(OtherUtils.CRLF);
				endsb.append("CONSTRAINT \"FK_").append(module.getTableName()).append("__").append(mainTableName).append("\" FOREIGN KEY (\"").append(joinColumn).append("\") REFERENCES \"").append(mainTableName).append("\" (\"ID\") ").append(OtherUtils.CRLF);
			} else {
				endsb.append(OtherUtils.CRLF);
			}
			endsb.append(");").append(OtherUtils.CRLF);
		} else {
			
		}
		
		sb.append(bulidFieldsSql(fieldList));
		return sb.toString() + endsb.toString();
	}
	
	private static String bulidFieldsSql(List<Field> fieldList) {
		StringBuffer sb = new StringBuffer();
		Set<String> set = new HashSet<String>();
		for (Field field : fieldList) {
			String dataType = field.getDataType();
			String tableFieldId = field.getTableFieldId();
			Integer precision = field.getPrecision();
			if (field.getTranSient() == true || field.getOnlyMeta() == true || OtherUtils.TPYE_LIST.equals(dataType)) {
				continue;
			}
			if (field.getJoinColumn() != null) {
				dataType = OtherUtils.TPYE_STRING;
				tableFieldId = field.getJoinColumn();
				precision = 32;
			}
			if (set.contains(tableFieldId)) {
				continue;
			}
			if (DBUtil.isSqlServer()) {
				sb.append("[").append(tableFieldId).append("] ").append(SqlUtils.getDatabaseDataType(dataType, precision)).append(" NULL ,").append(OtherUtils.CRLF);
			} else if (DBUtil.isOracle()) {
				sb.append("\"").append(tableFieldId).append("\" ").append(SqlUtils.getDatabaseDataType(dataType, precision)).append(" NULL ,").append(OtherUtils.CRLF);
			}
			set.add(tableFieldId);
		}
		return sb.toString();
	}
}
