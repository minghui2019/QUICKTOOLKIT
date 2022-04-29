package com.eplugger.onekey.utils.sqlFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.addModule.entity.Module;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.utils.DBUtils;

public class ProduceSqlFiles {

	public static void produceCreateTableSqlFilesSingle(ModuleInfo moduleInfo) {
		String sqlCode = produceCreateTableSql(moduleInfo);
		FileUtils.write("C:/Users/Admin/Desktop/AddModule/sql" + File.separator + moduleInfo.getModuleName() + ".SQL", sqlCode);
	}
	
	/**
	 * 生产数据库表格sql文件
	 * @param mainModule
	 * @param authorModule
	 * @param databaseType
	 * @param database
	 * @param authorSwitch
	 * @param joinColumn
	 */
	public static void produceCreateTableSqlFiles(ModuleInfo mainModule, ModuleInfo authorModule, String databaseType,
			String database, boolean authorSwitch, String joinColumn) {
		String sqlCode = produceCreateTableSql(mainModule, databaseType, database, false, null, null);
		FileUtils.write("C:/Users/Admin/Desktop/AddModule/sql" + File.separator + mainModule.getModuleName() + ".SQL", sqlCode);
		if (authorSwitch) {
			String authorsqlCode = produceCreateTableSql(authorModule, databaseType, database, authorSwitch, joinColumn, mainModule.getTableName());
			FileUtils.write("C:/Users/Admin/Desktop/AddModule/sql" + File.separator + authorModule.getModuleName() + ".SQL", authorsqlCode);
		}
	}

	/**
	 * 生产数据库表格sql代码
	 * @param module
	 * @param databaseType
	 * @param database
	 * @param authorSwitch
	 * @param joinColumn
	 * @param mainTableName
	 * @return
	 */
	@Deprecated
	private static String produceCreateTableSql(ModuleInfo module, String databaseType, String database, boolean authorSwitch, String joinColumn, String mainTableName) {
		StringBuffer sb = new StringBuffer();
		StringBuffer endsb = new StringBuffer();
		String tableName = module.getTableName();
		List<Field> fieldList = module.getFields();
		String superClass = module.getSuperClassMap().get("entity");
		if (DBUtils.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[" + tableName + "] (" + StringUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ," + StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			if (authorSwitch) {
				endsb.append("," + StringUtils.CRLF);
				endsb.append("CONSTRAINT [FK_" + module.getTableName() + "__" + mainTableName + "] FOREIGN KEY ([" + SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn) + "]) REFERENCES [dbo].[" + mainTableName + "] ([ID]) ON DELETE NO ACTION ON UPDATE NO ACTION" + StringUtils.CRLF);
			} else {
				endsb.append(StringUtils.CRLF);
			}
			endsb.append(")" + StringUtils.CRLF);
			endsb.append("GO" + StringUtils.CRLF);
		} else if (DBUtils.isOracle()) {
			sb.append("CREATE TABLE \"" + database + "\"." + tableName + " (" + StringUtils.CRLF);
			sb.append("\"ID\" VARCHAR2(32) NOT NULL ," + StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY (\"ID\")");
			if (authorSwitch) {
				endsb.append("," + StringUtils.CRLF);
				endsb.append("CONSTRAINT \"FK_" + module.getTableName() + "__" + mainTableName + "\" FOREIGN KEY (\"" + joinColumn + "\") REFERENCES \"" + mainTableName + "\" (\"ID\") " + StringUtils.CRLF);
			} else {
				endsb.append(StringUtils.CRLF);
			}
			endsb.append(");" + StringUtils.CRLF);
		} else {
			
		}
		
		for (Field field : fieldList) {
			if (field.isTranSient() || "List".equals(field.getDataType()) || field.getJoinColumn() != null) {
				continue;
			}
			if (DBUtils.isSqlServer()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + StringUtils.CRLF);
			} else if (DBUtils.isOracle()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + StringUtils.CRLF);
			}
		}
		sb.append(getSuperClassField(superClass, databaseType));
		return sb.toString() + endsb.toString();
	}
	
	/**
	 * 生产数据库表格sql代码
	 * @param module
	 * @return
	 */
	private static String produceCreateTableSql(ModuleInfo module) {
		StringBuffer sb = new StringBuffer();
		StringBuffer endsb = new StringBuffer();
		String tableName = module.getTableName();
		List<Field> fieldList = module.getFields();
		String superClass = module.getSuperClassMap().get("entity");
		if (DBUtils.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[" + tableName + "] (" + StringUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ," + StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			endsb.append(StringUtils.CRLF);
			endsb.append(")" + StringUtils.CRLF);
			endsb.append("GO" + StringUtils.CRLF);
		} else if (DBUtils.isOracle()) {
			sb.append("CREATE TABLE \"" + DBUtils.getDatabaseName() + "\"." + tableName + " (" + StringUtils.CRLF);
			sb.append("\"ID\" VARCHAR2(32) NOT NULL ," + StringUtils.CRLF);
			
			endsb.append("PRIMARY KEY (\"ID\")");
			endsb.append(StringUtils.CRLF);
			endsb.append(");" + StringUtils.CRLF);
		} else {
			
		}
		
		for (Field field : fieldList) {
			if (field.isTranSient() || "List".equals(field.getDataType()) || field.getJoinColumn() != null) {
				continue;
			}
			if (DBUtils.isSqlServer()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + StringUtils.CRLF);
			} else if (DBUtils.isOracle()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + StringUtils.CRLF);
			}
		}
		sb.append(getSuperClassField(superClass));
		return sb.toString() + endsb.toString();
	}

	/**
	 * 生产sql代码
	 * 20220429 修改
	 * @param tableName
	 * @param fieldList
	 * @param database
	 * @return
	 */
	public static String produceSqlCode(String tableName, List<Field> fieldList) {
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
				sb.append("ADD ( \"").append(field.getTableFieldId()).append("\" ").append(SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision())).append(" NULL  ) ");
				sb.append(StringUtils.CRLF);
			}
		}
		if (DBUtils.isOracle() && sb.length() > 0) {
			sb.append(";").append(StringUtils.CRLF);
			sb.insert(0, "ALTER TABLE \"" + DBUtils.getDatabaseName() + "\".\"" + tableName + "\"" + StringUtils.CRLF);
		}
		return sb.toString();
	}

	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @param databaseType
	 * @return
	 */
	@Deprecated
	private static String getSuperClassField(String superClass, String databaseType) {
		StringBuffer sb = new StringBuffer();
		boolean productflag = true;
		boolean productauthorflag = true;
		if ("sqlserver".equals(databaseType)) {
			switch (superClass) {
			case "Product":
				sb.append("[NAME] varchar(512) NULL ," + StringUtils.CRLF);
				sb.append("[UNIT_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[DIVISION_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[AUTHOR_NUMBER] int NULL ," + StringUtils.CRLF);
				sb.append("[NOTE] text NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_NAME] varchar(64) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ACCOUNT] varchar(64) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_TITLE_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_LEVEL_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_DEGREE_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_SEXID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FILE_IDS] varchar(500) NULL ," + StringUtils.CRLF);
				sb.append("[AUTHORPIDS] varchar(2000) NULL ," + StringUtils.CRLF);
				sb.append("[AUTHORUNITIDS] varchar(2000) NULL ," + StringUtils.CRLF);
				sb.append("[COMPLETEDATASTATUS] varchar(40) NULL ," + StringUtils.CRLF);
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("[ORDER_ID] int NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_TYPE] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[PERSON_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_ACCOUNT] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_NAME] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[SEX_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[EDU_LEVEL_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[TITLE_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_UNIT] varchar(256) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_UNIT_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[SUBJECT_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[EDU_DEGREE_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[WORK_RATIO] decimal(18,6) NULL ," + StringUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("[CHECKSTATUS] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[CHECKDATE] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[CHECKER] varchar(80) NULL ," + StringUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("[CREATEUSERID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[CREATEUSERNAME] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[CREATEDATE] datetime NULL ," + StringUtils.CRLF);
					sb.append("[LASTEDITUSERID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[LASTEDITUSERNAME] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[LASTEDITDATE] datetime NULL ," + StringUtils.CRLF);
				}
				break;
			default:
				break;
			}
		} else if ("oracle".equals(databaseType)) {
			switch (superClass) {
			case "Product":
				sb.append("\"NAME\" VARCHAR2(512), " + StringUtils.CRLF);
				sb.append("\"UNIT_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"DIVISION_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"AUTHOR_NUMBER\" NUMBER, " + StringUtils.CRLF);
				sb.append("\"NOTE\" CLOB, " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_NAME\" VARCHAR2(64), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ACCOUNT\" VARCHAR2(64), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_TITLE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_LEVEL_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_DEGREE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_SEXID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FILE_IDS\" VARCHAR2(1000), " + StringUtils.CRLF);
				sb.append("\"AUTHORPIDS\" VARCHAR2(1000), " + StringUtils.CRLF);
				sb.append("\"AUTHORUNITIDS\" VARCHAR2(1000), " + StringUtils.CRLF);
				sb.append("\"COMPLETEDATASTATUS\" VARCHAR2(40), " + StringUtils.CRLF); 
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("\"ORDER_ID\" NUMBER, " + StringUtils.CRLF);
					sb.append("\"AUTHOR_TYPE\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"PERSON_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_ACCOUNT\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_NAME\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"SEX_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"EDU_LEVEL_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"TITLE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_UNIT\" VARCHAR2(256), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_UNIT_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"SUBJECT_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"EDU_DEGREE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"WORK_RATIO\" NUMBER(18,6), " + StringUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("\"CHECKSTATUS\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"CHECKDATE\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"CHECKER\" VARCHAR2(80)," + StringUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("\"CREATEUSERID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"CREATEUSERNAME\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"CREATEDATE\" DATE," + StringUtils.CRLF);
					sb.append("\"LASTEDITUSERID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"LASTEDITUSERNAME\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"LASTEDITDATE\" DATE, " + StringUtils.CRLF);
				}
				break;
			default:
				break;
			}
		}
		return sb.toString();
	}
	
	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @return
	 */
	private static String getSuperClassField(String superClass) {
		StringBuffer sb = new StringBuffer();
		boolean productflag = true;
		boolean productauthorflag = true;
		if (DBUtils.isSqlServer()) {
			switch (superClass) {
			case "Product":
				sb.append("[NAME] varchar(512) NULL ," + StringUtils.CRLF);
				sb.append("[UNIT_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[DIVISION_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[AUTHOR_NUMBER] int NULL ," + StringUtils.CRLF);
				sb.append("[NOTE] text NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_NAME] varchar(64) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ACCOUNT] varchar(64) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_TITLE_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_LEVEL_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_DEGREE_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_SEXID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ID] varchar(32) NULL ," + StringUtils.CRLF);
				sb.append("[FILE_IDS] varchar(500) NULL ," + StringUtils.CRLF);
				sb.append("[AUTHORPIDS] varchar(2000) NULL ," + StringUtils.CRLF);
				sb.append("[AUTHORUNITIDS] varchar(2000) NULL ," + StringUtils.CRLF);
				sb.append("[COMPLETEDATASTATUS] varchar(40) NULL ," + StringUtils.CRLF);
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("[ORDER_ID] int NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_TYPE] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[PERSON_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_ACCOUNT] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_NAME] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[SEX_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[EDU_LEVEL_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[TITLE_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_UNIT] varchar(256) NULL ," + StringUtils.CRLF);
					sb.append("[AUTHOR_UNIT_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[SUBJECT_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[EDU_DEGREE_ID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[WORK_RATIO] decimal(18,6) NULL ," + StringUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("[CHECKSTATUS] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[CHECKDATE] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[CHECKER] varchar(80) NULL ," + StringUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("[CREATEUSERID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[CREATEUSERNAME] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[CREATEDATE] datetime NULL ," + StringUtils.CRLF);
					sb.append("[LASTEDITUSERID] varchar(32) NULL ," + StringUtils.CRLF);
					sb.append("[LASTEDITUSERNAME] varchar(64) NULL ," + StringUtils.CRLF);
					sb.append("[LASTEDITDATE] datetime NULL ," + StringUtils.CRLF);
				}
				break;
			default:
				break;
			}
		} else if (DBUtils.isOracle()) {
			switch (superClass) {
			case "Product":
				sb.append("\"NAME\" VARCHAR2(512), " + StringUtils.CRLF);
				sb.append("\"UNIT_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"DIVISION_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"AUTHOR_NUMBER\" NUMBER, " + StringUtils.CRLF);
				sb.append("\"NOTE\" CLOB, " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_NAME\" VARCHAR2(64), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ACCOUNT\" VARCHAR2(64), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_TITLE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_LEVEL_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_DEGREE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_SEXID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ID\" VARCHAR2(32), " + StringUtils.CRLF);
				sb.append("\"FILE_IDS\" VARCHAR2(1000), " + StringUtils.CRLF);
				sb.append("\"AUTHORPIDS\" VARCHAR2(1000), " + StringUtils.CRLF);
				sb.append("\"AUTHORUNITIDS\" VARCHAR2(1000), " + StringUtils.CRLF);
				sb.append("\"COMPLETEDATASTATUS\" VARCHAR2(40), " + StringUtils.CRLF); 
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("\"ORDER_ID\" NUMBER, " + StringUtils.CRLF);
					sb.append("\"AUTHOR_TYPE\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"PERSON_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_ACCOUNT\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_NAME\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"SEX_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"EDU_LEVEL_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"TITLE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_UNIT\" VARCHAR2(256), " + StringUtils.CRLF);
					sb.append("\"AUTHOR_UNIT_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"SUBJECT_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"EDU_DEGREE_ID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"WORK_RATIO\" NUMBER(18,6), " + StringUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("\"CHECKSTATUS\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"CHECKDATE\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"CHECKER\" VARCHAR2(80)," + StringUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("\"CREATEUSERID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"CREATEUSERNAME\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"CREATEDATE\" DATE," + StringUtils.CRLF);
					sb.append("\"LASTEDITUSERID\" VARCHAR2(32), " + StringUtils.CRLF);
					sb.append("\"LASTEDITUSERNAME\" VARCHAR2(64), " + StringUtils.CRLF);
					sb.append("\"LASTEDITDATE\" DATE, " + StringUtils.CRLF);
				}
				break;
			default:
				break;
			}
		}
		return sb.toString();
	}
	
	/**
	 * 生产数据库表格sql文件
	 * @param mainModule
	 * @param authorModule
	 * @param databaseType
	 * @param database
	 * @param authorSwitch
	 * @param joinColumn
	 * @param database 
	 * @param version 
	 */
	public static void produceCreateTableSqlFiles(Module module, String version) {
		String sqlCode = ProduceSqlFactory.produceCreateTableSql(module.getMainModule(), false, null, null);
		
		List<Field> fields = module.getMainModule().getFields();
		fields.addAll(Constants.getSuperClassFieldMap(module.getMainModule().getSuperClassMap().get("entity")));
		
		String metadata = ProduceMetaDataFiles.produceMetadata(module.getMainModule().getBeanId(), fields);
		
		FileUtils.write("C:/Users/Admin/Desktop/AddListModule/sql" + File.separator + module.getMainModule().getModuleName() + ".SQL", sqlCode + metadata);
	}
	
	public static void produceCreateTableSqlFiles(Module module) {
		List<Field> fields = module.getMainModule().getFields();
		String key = module.getMainModule().getSuperClassMap().get("entity");
		List<Field> addSuperClassFields = addSuperClassFields(key);
		if (addSuperClassFields != null) {
			fields.addAll(addSuperClassFields);
		}
		String sqlCode = ProduceSqlFactory.produceCreateTableSql(module.getMainModule(), false, null, null);
		
		String metadata = ProduceMetaDataFiles.produceMetadata(module.getMainModule().getBeanId(), fields);
		
		FileUtils.write("C:/Users/Admin/Desktop/AddListModule/sql" + File.separator + module.getMainModule().getModuleName() + ".SQL", sqlCode + metadata);
	}
	
	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @return
	 */
	private static List<Field> addSuperClassFields(String superClass) {
		List<Field> fields = new ArrayList<Field>();
		boolean productflag = true;
		boolean productauthorflag = true;
		switch (superClass) {
		case "Product":
			fields.addAll(Constants.getSuperClassFieldMap("Product"));
			productflag = false;
		case "ProductAuthor":
			if (productflag) {
				fields.addAll(Constants.getSuperClassFieldMap("ProductAuthor"));
				productauthorflag = false;
			}
		case "CheckBusinessEntity":
			if (productauthorflag) {
				fields.addAll(Constants.getSuperClassFieldMap("CheckBusinessEntity"));
			}
		case "BusinessEntity":
		case "BizEntity":
			if (productauthorflag) {
				fields.addAll(Constants.getSuperClassFieldMap("BizEntity"));
			}
			break;
		default:
			break;
		}
		return fields;
	}
}
