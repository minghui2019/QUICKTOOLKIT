package com.eplugger.onekey.utils.sqlFile;

import java.util.ArrayList;
import java.util.List;

import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.addModule.entity.Module;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.util.DBUtil;
import com.eplugger.util.FileUtil;
import com.eplugger.util.OtherUtils;

public class ProduceSqlFiles {

	public static void produceCreateTableSqlFilesSingle(ModuleInfo moduleInfo) {
		String sqlCode = produceCreateTableSql(moduleInfo);
		FileUtil.outFile(sqlCode, "C:/Users/Admin/Desktop/AddModule/sql", moduleInfo.getModuleName() + ".SQL");
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
		FileUtil.outFile(sqlCode, "C:/Users/Admin/Desktop/AddModule/sql", mainModule.getModuleName() + ".SQL");
		if (authorSwitch) {
			String authorsqlCode = produceCreateTableSql(authorModule, databaseType, database, authorSwitch, joinColumn, mainModule.getTableName());
			FileUtil.outFile(authorsqlCode, "C:/Users/Admin/Desktop/AddModule/sql", authorModule.getModuleName() + ".SQL");
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
		if (DBUtil.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[" + tableName + "] (" + OtherUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ," + OtherUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			if (authorSwitch) {
				endsb.append("," + OtherUtils.CRLF);
				endsb.append("CONSTRAINT [FK_" + module.getTableName() + "__" + mainTableName + "] FOREIGN KEY ([" + SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn) + "]) REFERENCES [dbo].[" + mainTableName + "] ([ID]) ON DELETE NO ACTION ON UPDATE NO ACTION" + OtherUtils.CRLF);
			} else {
				endsb.append(OtherUtils.CRLF);
			}
			endsb.append(")" + OtherUtils.CRLF);
			endsb.append("GO" + OtherUtils.CRLF);
		} else if (DBUtil.isOracle()) {
			sb.append("CREATE TABLE \"" + database + "\"." + tableName + " (" + OtherUtils.CRLF);
			sb.append("\"ID\" VARCHAR2(32) NOT NULL ," + OtherUtils.CRLF);
			
			endsb.append("PRIMARY KEY (\"ID\")");
			if (authorSwitch) {
				endsb.append("," + OtherUtils.CRLF);
				endsb.append("CONSTRAINT \"FK_" + module.getTableName() + "__" + mainTableName + "\" FOREIGN KEY (\"" + joinColumn + "\") REFERENCES \"" + mainTableName + "\" (\"ID\") " + OtherUtils.CRLF);
			} else {
				endsb.append(OtherUtils.CRLF);
			}
			endsb.append(");" + OtherUtils.CRLF);
		} else {
			
		}
		
		for (Field field : fieldList) {
			if (field.getTranSient() || "List".equals(field.getDataType()) || field.getJoinColumn() != null) {
				continue;
			}
			if (DBUtil.isSqlServer()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + OtherUtils.CRLF);
			} else if (DBUtil.isOracle()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + OtherUtils.CRLF);
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
		if (DBUtil.isSqlServer()) {
			sb.append("CREATE TABLE [dbo].[" + tableName + "] (" + OtherUtils.CRLF);
			sb.append("[ID] varchar(32) NOT NULL ," + OtherUtils.CRLF);
			
			endsb.append("PRIMARY KEY ([ID])");
			endsb.append(OtherUtils.CRLF);
			endsb.append(")" + OtherUtils.CRLF);
			endsb.append("GO" + OtherUtils.CRLF);
		} else if (DBUtil.isOracle()) {
			sb.append("CREATE TABLE \"" + DBUtil.getDatabaseName() + "\"." + tableName + " (" + OtherUtils.CRLF);
			sb.append("\"ID\" VARCHAR2(32) NOT NULL ," + OtherUtils.CRLF);
			
			endsb.append("PRIMARY KEY (\"ID\")");
			endsb.append(OtherUtils.CRLF);
			endsb.append(");" + OtherUtils.CRLF);
		} else {
			
		}
		
		for (Field field : fieldList) {
			if (field.getTranSient() || "List".equals(field.getDataType()) || field.getJoinColumn() != null) {
				continue;
			}
			if (DBUtil.isSqlServer()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + OtherUtils.CRLF);
			} else if (DBUtil.isOracle()) {
				sb.append("[" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL ," + OtherUtils.CRLF);
			}
		}
		sb.append(getSuperClassField(superClass));
		return sb.toString() + endsb.toString();
	}

	/**
	 * 生产sql代码
	 * @param tableName
	 * @param fieldList
	 * @param database
	 * @return
	 */
	public static String produceSqlCode(String tableName, List<Field> fieldList) {
		StringBuffer sb = new StringBuffer();
		if (DBUtil.isSqlServer()) {
			for (Field field : fieldList) {
				if (field.getTranSient()) {
					continue;
				}
				sb.append("ALTER TABLE [dbo].[" + tableName + "] ADD [" + field.getTableFieldId() + "] " + SqlUtils.getDatabaseDataType(field.getDataType(), field.getPrecision()) + " NULL" + OtherUtils.CRLF);
				sb.append("GO" + OtherUtils.CRLF + OtherUtils.CRLF);
			}
		} else if (DBUtil.isOracle()) {
			for (int i = 0, size = fieldList.size(); i < size; i++) {
				if (fieldList.get(i).getTranSient()) {
					continue;
				}
				sb.append("ADD ( \"" + fieldList.get(i).getTableFieldId() + "\" " + SqlUtils.getDatabaseDataType(fieldList.get(i).getDataType(), fieldList.get(i).getPrecision()) + " NULL  ) ");
				if (i < size - 1) {
					sb.append(OtherUtils.CRLF);
				} else {
					sb.append(";" + OtherUtils.CRLF);
				}
			}
			if (sb.length() > 0) {
				sb.insert(0, "ALTER TABLE \"" + DBUtil.getDatabaseName() + "\".\"" + tableName + "\"" + OtherUtils.CRLF);
			}
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
				sb.append("[NAME] varchar(512) NULL ," + OtherUtils.CRLF);
				sb.append("[UNIT_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[DIVISION_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[AUTHOR_NUMBER] int NULL ," + OtherUtils.CRLF);
				sb.append("[NOTE] text NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_NAME] varchar(64) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ACCOUNT] varchar(64) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_TITLE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_LEVEL_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_DEGREE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_SEXID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FILE_IDS] varchar(500) NULL ," + OtherUtils.CRLF);
				sb.append("[AUTHORPIDS] varchar(2000) NULL ," + OtherUtils.CRLF);
				sb.append("[AUTHORUNITIDS] varchar(2000) NULL ," + OtherUtils.CRLF);
				sb.append("[COMPLETEDATASTATUS] varchar(40) NULL ," + OtherUtils.CRLF);
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("[ORDER_ID] int NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_TYPE] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[PERSON_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_ACCOUNT] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_NAME] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[SEX_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[EDU_LEVEL_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[TITLE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_UNIT] varchar(256) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_UNIT_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[SUBJECT_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[EDU_DEGREE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[WORK_RATIO] decimal(18,6) NULL ," + OtherUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("[CHECKSTATUS] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[CHECKDATE] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[CHECKER] varchar(80) NULL ," + OtherUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("[CREATEUSERID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[CREATEUSERNAME] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[CREATEDATE] datetime NULL ," + OtherUtils.CRLF);
					sb.append("[LASTEDITUSERID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[LASTEDITUSERNAME] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[LASTEDITDATE] datetime NULL ," + OtherUtils.CRLF);
				}
				break;
			default:
				break;
			}
		} else if ("oracle".equals(databaseType)) {
			switch (superClass) {
			case "Product":
				sb.append("\"NAME\" VARCHAR2(512), " + OtherUtils.CRLF);
				sb.append("\"UNIT_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"DIVISION_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"AUTHOR_NUMBER\" NUMBER, " + OtherUtils.CRLF);
				sb.append("\"NOTE\" CLOB, " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_NAME\" VARCHAR2(64), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ACCOUNT\" VARCHAR2(64), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_TITLE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_LEVEL_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_DEGREE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_SEXID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FILE_IDS\" VARCHAR2(1000), " + OtherUtils.CRLF);
				sb.append("\"AUTHORPIDS\" VARCHAR2(1000), " + OtherUtils.CRLF);
				sb.append("\"AUTHORUNITIDS\" VARCHAR2(1000), " + OtherUtils.CRLF);
				sb.append("\"COMPLETEDATASTATUS\" VARCHAR2(40), " + OtherUtils.CRLF); 
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("\"ORDER_ID\" NUMBER, " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_TYPE\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"PERSON_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_ACCOUNT\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_NAME\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"SEX_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"EDU_LEVEL_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"TITLE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_UNIT\" VARCHAR2(256), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_UNIT_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"SUBJECT_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"EDU_DEGREE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"WORK_RATIO\" NUMBER(18,6), " + OtherUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("\"CHECKSTATUS\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"CHECKDATE\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"CHECKER\" VARCHAR2(80)," + OtherUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("\"CREATEUSERID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"CREATEUSERNAME\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"CREATEDATE\" DATE," + OtherUtils.CRLF);
					sb.append("\"LASTEDITUSERID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"LASTEDITUSERNAME\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"LASTEDITDATE\" DATE, " + OtherUtils.CRLF);
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
		if (DBUtil.isSqlServer()) {
			switch (superClass) {
			case "Product":
				sb.append("[NAME] varchar(512) NULL ," + OtherUtils.CRLF);
				sb.append("[UNIT_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[DIVISION_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[AUTHOR_NUMBER] int NULL ," + OtherUtils.CRLF);
				sb.append("[NOTE] text NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_NAME] varchar(64) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ACCOUNT] varchar(64) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_TITLE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_LEVEL_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_EDU_DEGREE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_SEXID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FIRST_AUTHOR_ID] varchar(32) NULL ," + OtherUtils.CRLF);
				sb.append("[FILE_IDS] varchar(500) NULL ," + OtherUtils.CRLF);
				sb.append("[AUTHORPIDS] varchar(2000) NULL ," + OtherUtils.CRLF);
				sb.append("[AUTHORUNITIDS] varchar(2000) NULL ," + OtherUtils.CRLF);
				sb.append("[COMPLETEDATASTATUS] varchar(40) NULL ," + OtherUtils.CRLF);
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("[ORDER_ID] int NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_TYPE] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[PERSON_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_ACCOUNT] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_NAME] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[SEX_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[EDU_LEVEL_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[TITLE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_UNIT] varchar(256) NULL ," + OtherUtils.CRLF);
					sb.append("[AUTHOR_UNIT_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[SUBJECT_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[EDU_DEGREE_ID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[WORK_RATIO] decimal(18,6) NULL ," + OtherUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("[CHECKSTATUS] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[CHECKDATE] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[CHECKER] varchar(80) NULL ," + OtherUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("[CREATEUSERID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[CREATEUSERNAME] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[CREATEDATE] datetime NULL ," + OtherUtils.CRLF);
					sb.append("[LASTEDITUSERID] varchar(32) NULL ," + OtherUtils.CRLF);
					sb.append("[LASTEDITUSERNAME] varchar(64) NULL ," + OtherUtils.CRLF);
					sb.append("[LASTEDITDATE] datetime NULL ," + OtherUtils.CRLF);
				}
				break;
			default:
				break;
			}
		} else if (DBUtil.isOracle()) {
			switch (superClass) {
			case "Product":
				sb.append("\"NAME\" VARCHAR2(512), " + OtherUtils.CRLF);
				sb.append("\"UNIT_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"DIVISION_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"AUTHOR_NUMBER\" NUMBER, " + OtherUtils.CRLF);
				sb.append("\"NOTE\" CLOB, " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_NAME\" VARCHAR2(64), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ACCOUNT\" VARCHAR2(64), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_TITLE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_LEVEL_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_EDU_DEGREE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_SEXID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FIRST_AUTHOR_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
				sb.append("\"FILE_IDS\" VARCHAR2(1000), " + OtherUtils.CRLF);
				sb.append("\"AUTHORPIDS\" VARCHAR2(1000), " + OtherUtils.CRLF);
				sb.append("\"AUTHORUNITIDS\" VARCHAR2(1000), " + OtherUtils.CRLF);
				sb.append("\"COMPLETEDATASTATUS\" VARCHAR2(40), " + OtherUtils.CRLF); 
				productflag = false;
			case "ProductAuthor":
				if (productflag) {
					sb.append("\"ORDER_ID\" NUMBER, " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_TYPE\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"PERSON_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_ACCOUNT\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_NAME\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"SEX_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"EDU_LEVEL_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"TITLE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_UNIT\" VARCHAR2(256), " + OtherUtils.CRLF);
					sb.append("\"AUTHOR_UNIT_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"SUBJECT_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"EDU_DEGREE_ID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"WORK_RATIO\" NUMBER(18,6), " + OtherUtils.CRLF);
					productauthorflag = false;
				}
			case "CheckBusinessEntity":
				if (productauthorflag) {
					sb.append("\"CHECKSTATUS\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"CHECKDATE\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"CHECKER\" VARCHAR2(80)," + OtherUtils.CRLF);
				}
			case "BusinessEntity":
			case "BizEntity":
				if (productauthorflag) {
					sb.append("\"CREATEUSERID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"CREATEUSERNAME\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"CREATEDATE\" DATE," + OtherUtils.CRLF);
					sb.append("\"LASTEDITUSERID\" VARCHAR2(32), " + OtherUtils.CRLF);
					sb.append("\"LASTEDITUSERNAME\" VARCHAR2(64), " + OtherUtils.CRLF);
					sb.append("\"LASTEDITDATE\" DATE, " + OtherUtils.CRLF);
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
		
		FileUtil.outFile(sqlCode + metadata, "C:/Users/Admin/Desktop/AddListModule/sql", module.getMainModule().getModuleName() + ".SQL");
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
		
		FileUtil.outFile(sqlCode + metadata, "C:/Users/Admin/Desktop/AddListModule/sql", module.getMainModule().getModuleName() + ".SQL");
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
