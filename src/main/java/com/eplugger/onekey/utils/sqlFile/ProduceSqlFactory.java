package com.eplugger.onekey.utils.sqlFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.onekey.factory.AbstractProduceCodeFactory;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;

public class ProduceSqlFactory extends AbstractProduceCodeFactory {
	private static class ProduceSqlFactorySingleton {
		private static final ProduceSqlFactory FACTORY = new ProduceSqlFactory(); 
	}
	
	public static ProduceSqlFactory getInstance() {
		return ProduceSqlFactorySingleton.FACTORY;
	}
	
	private ProduceSqlFactory() { }
	
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
		if (DBUtils.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[").append(tableName).append("] (").append(StringUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ,").append(StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			if (authorSwitch) {
				endsb.append(",").append(StringUtils.CRLF);
				endsb.append("CONSTRAINT [FK_").append(module.getTableName()).append("__").append(mainTableName).append("] FOREIGN KEY ([").append(SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn)).append("]) REFERENCES [dbo].[").append(mainTableName).append("] ([ID]) ON DELETE NO ACTION ON UPDATE NO ACTION").append(StringUtils.CRLF);
			} else {
				endsb.append(StringUtils.CRLF);
			}
			endsb.append(")").append(StringUtils.CRLF);
			endsb.append("GO").append(StringUtils.CRLF);
		} else if (DBUtils.isOracle()) {
			sb.append("CREATE TABLE \"").append(DBUtils.getDatabaseName()).append("\".").append(tableName).append(" (").append(StringUtils.CRLF);
			sb.append("\"ID\" VARCHAR2(32) NOT NULL ,").append(StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY (\"ID\")");
			if (authorSwitch) {
				endsb.append(",").append(StringUtils.CRLF);
				endsb.append("CONSTRAINT \"FK_").append(module.getTableName()).append("__").append(mainTableName).append("\" FOREIGN KEY (\"").append(joinColumn).append("\") REFERENCES \"").append(mainTableName).append("\" (\"ID\") ").append(StringUtils.CRLF);
			} else {
				endsb.append(StringUtils.CRLF);
			}
			endsb.append(");").append(StringUtils.CRLF);
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
			if (field.isTranSient() == true || field.isOnlyMeta() == true || OtherUtils.TPYE_LIST.equals(dataType)) {
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
			if (DBUtils.isSqlServer()) {
				sb.append("[").append(tableFieldId).append("] ").append(SqlUtils.getDatabaseDataType(dataType, precision)).append(" NULL ,").append(StringUtils.CRLF);
			} else if (DBUtils.isOracle()) {
				sb.append("\"").append(tableFieldId).append("\" ").append(SqlUtils.getDatabaseDataType(dataType, precision)).append(" NULL ,").append(StringUtils.CRLF);
			}
			set.add(tableFieldId);
		}
		return sb.toString();
	}

	/**
	 * 生产sql代码
	 * 20220429 修改
	 * @param tableName
	 * @param fieldList
	 * @param database
	 * @return
	 */
	public String produceSqlCode(String tableName, List<Field> fieldList) {
		StringBuffer sb = new StringBuffer();
		for (Field field : fieldList) {
			if (field.isTranSient()) {
				continue;
			}
			if (DBUtils.isSqlServer()) {
				sb.append("ALTER TABLE [dbo].[").append(tableName).append("] ADD [").append(field.getTableFieldId()).append("] ").append(SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision())).append(" NULL").append(StringUtils.CRLF);
				sb.append("GO").append(StringUtils.CRLF).append(StringUtils.CRLF);
			}
			if (DBUtils.isOracle()) {
				sb.append("ADD ( \"").append(field.getTableFieldId()).append("\" ").append(SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision())).append(" NULL ) ");
				sb.append(StringUtils.CRLF);
			}
		}
		if (DBUtils.isOracle() && sb.length() > 0) {
			sb.append(";").append(StringUtils.CRLF);
			sb.insert(0, "ALTER TABLE \"" + DBUtils.getDatabaseName() + "\".\"" + tableName + "\"" + StringUtils.CRLF);
		}
		return sb.toString();
	}
}
