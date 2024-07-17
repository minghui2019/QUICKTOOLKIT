package com.eplugger.onekey.utils.sqlFile;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.eplugger.common.lang.CustomStringBuilder;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.ModuleInfo;
import com.eplugger.onekey.factory.AbstractProduceCodeFactory;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;
import com.eplugger.uuid.UUIDFactory;
import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;

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
	 * @param authorSwitch
	 * @param joinColumn
	 * @param mainTableName
	 * @return
	 */
	public String produceCreateTableSql(ModuleInfo module, boolean authorSwitch, String joinColumn, String mainTableName) {
		StringBuffer sb = new StringBuffer(), endsb = new StringBuffer();
		List<Field> fieldList = module.getFieldList();
		String key = module.getSuperClassMap().get("entity");
		List<Field> addSuperClassFields = Constants.addSuperClassFields(key);
		if (addSuperClassFields != null) {
			fieldList.addAll(addSuperClassFields);
		}
		
		String tableName = module.getTableName();
		if (DBUtils.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[").append(tableName).append("] (").append(StringUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ,").append(StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			if (authorSwitch) {
				endsb.append(",").append(StringUtils.CRLF);
				endsb.append("CONSTRAINT [FK_").append(tableName).append("__").append(mainTableName).append("] FOREIGN KEY ([").append(joinColumn).append("]) REFERENCES [dbo].[").append(mainTableName).append("] ([ID]) ON DELETE NO ACTION ON UPDATE NO ACTION").append(StringUtils.CRLF);
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
				endsb.append("CONSTRAINT \"FK_").append(tableName).append("__").append(mainTableName).append("\" FOREIGN KEY (\"").append(joinColumn).append("\") REFERENCES \"").append(mainTableName).append("\" (\"ID\") ").append(StringUtils.CRLF);
			} else {
				endsb.append(StringUtils.CRLF);
			}
			endsb.append(");").append(StringUtils.CRLF);
		} else {
			
		}
		
		sb.append(bulidFieldsSql(fieldList));
		sb.append(endsb);
		String produceMetadata = ProduceMetaDataFactory.getInstance().produceMetadata(module.getBeanId(), fieldList);
		sb.append(produceMetadata);
		return sb.toString();
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
	 * @return
	 */
	public String produceSqlCode(String tableName, List<Field> fieldList) {
		CustomStringBuilder dsb = new CustomStringBuilder();
		dsb.appendln("-- 建表");
		for (Field field : fieldList) {
			if (field.isTranSient() || field.isOnlyMeta() == true) {
				continue;
			}
			dsb.append("ALTER TABLE ").append(tableName).append(" ADD ").append(field.getTableFieldId()).append(" ").append(SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision())).append(";").appendln();
		}
		return dsb.toString();
	}

	public void produceCreateTableSqlFiles(ModuleInfo mainModule, ModuleInfo authorModule, boolean authorSwitch) {
		List<String> joinColumn = mainModule.getFieldList().stream().map(a -> a.getJoinColumn()).filter(a -> a != null).distinct().collect(Collectors.toList());
		String joinColumn_ = null;
		if (joinColumn != null && joinColumn.size() != 0) {
			joinColumn_ = joinColumn.get(0);
		}
		String sqlCode = produceCreateTableSql(mainModule, false, joinColumn_, mainModule.getTableName());
		FileUtils.write(FileUtils.getUserHomeDirectory() + "AddModule\\sql" + File.separator + mainModule.getModuleName() + ".SQL", sqlCode);
		if (authorSwitch && authorModule != null) {
			String authorsqlCode = produceCreateTableSql(authorModule, true, joinColumn_, mainModule.getTableName());
			FileUtils.write(FileUtils.getUserHomeDirectory() + "AddModule\\sql" + File.separator + authorModule.getModuleName() + ".SQL", authorsqlCode);
		}
		UUIDFactory.getInstance().destroy();
	}
}
