package com.eplugger.onekey.addModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eplugger.onekey.addCategoryEntry.utils.ProduceCategorySqlCodeFactory;
import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.entity.Module;
import com.eplugger.onekey.entity.Modules;
import com.eplugger.onekey.utils.javaFile.ProduceJavaFactory;
import com.eplugger.onekey.utils.javaFile.ProduceJavaFiles;
import com.eplugger.onekey.utils.jspFile.ProduceJspFiles;
import com.eplugger.onekey.utils.sqlFile.ProduceSqlFactory;
import com.eplugger.onekey.utils.sqlFile.ProduceSqlFiles;
import com.eplugger.onekey.utils.xmlFile.ProduceXmlFiles;
import com.eplugger.xml.dom4j.utils.parsers.ModuleParser;
import top.tobak.xml.dom4j.utils.ParseXmlUtils;

public class AddModuleFun {
	public static void main(String[] args) throws Exception {
		Modules modules = ParseXmlUtils.toBean("src/main/resource/module/Module.xml", Modules.class);
		Module module = modules.getValidModule();
		List<Category> categories = modules.getCategories();
		System.out.println(categories);
		System.out.println(module);
	}

	/**
	 * <p>单模块-一对多的多</p>
	 * <p>再生成一对多的一java代码</p>
	 * @throws Exception 
	 */
//	public static void AddSingleModuleFun() {
////		String packageName = "com.eplugger.assess.personAssess";
//		String packageName = "com.eplugger.business.projectApply"; //包名
//		Map<String, String> superClassMap = new HashMap<String, String>(); //继承的超类
//		List<Field> fieldList = Constants.getSingleFieldList(); //字段列表
//		String[] interfaces = null; //实现的接口
//		superClassMap.put("entity", "EntityImpl");
//		
//		ModuleInfo1 moduleInfo = new ModuleInfo1();
//		moduleInfo.setBeanId("projectApplyBookAchievements"); //beanId默认等同模块名
//		moduleInfo.setSuperClassMap(superClassMap);
//		moduleInfo.setInterfaces(interfaces);
//		moduleInfo.setFieldList(fieldList);
//		
//		ProduceJavaFiles.produceJavaFilesSingle(packageName, moduleInfo);
//		
//		ProduceSqlFiles.produceCreateTableSqlFilesSingle(moduleInfo);
//		
//		ProduceXmlFiles.produceXmlFileSingle(packageName, moduleInfo);
//	}
	
//	public static void AddMultipleModuleFun(String[] args) {
////		ModuleInfo1 moduleInfo = ModuleParse.getInstance().getValidModule("src/resource/module/Module.xml");
////		String packageName = "com.eplugger.assess.personAssess";
//		String packageName = "com.eplugger.business.project";
//		
//		Map<String, String> superClassMap = new HashMap<String, String>();
//		superClassMap.put("entity", "EntityImpl");
////		superClassMap.put("bo", "BusinessBO");
////		superClassMap.put("action", "BusinessAction");
//		String[] interfaces = null;
//		List<Field> fieldList = Constants.getFieldList();
//		
//		ModuleInfo1 mainModule = new ModuleInfo1();
//		mainModule.setModuleName("ZXProjectCooperationAgreement");//模块名
//		mainModule.setModuleZHName("合作协议");//模块中文名
//		mainModule.setTableName("BIZ_PROJECT_COOPERATION_AGREEMENT");//数据库表名
//		mainModule.setBeanId("zXProjectCooperationAgreement");//beanId默认等同模块名
//		mainModule.setSuperClassMap(superClassMap);
//		mainModule.setInterfaces(interfaces);
//		mainModule.setFieldList(fieldList);
//		
////		Map<String, String> authorsuperClassMap = new HashMap<String, String>();
////		authorsuperClassMap.put("entity", "ProductAuthor");
////		String[] authorinterfaces = null;
////		List<Field> authorfieldList = Constants.getAuthorfieldList();
////		
//		ModuleInfo1 authorModule = new ModuleInfo1();
////		authorModule.setModuleName("BookTestAuthor");
////		authorModule.setModuleZHName("著作作者");
////		authorModule.setTableName("BIZ_BOOK_TEST_AUTHOR");
////		authorModule.setBeanId("bookTestAuthor");
////		authorModule.setSuperClassMap(authorsuperClassMap);
////		authorModule.setInterfaces(authorinterfaces);
////		authorModule.setFieldList(authorfieldList);
//		
//		boolean authorSwitch = false;
//		String joinColumn = "personAssessId";
//		String template = "meeting";
//		
//		String databaseType = "sqlserver"; //数据库类型默认sqlserver,oracle
//		String database = "RDSYSEDUV82310831"; //oracle数据库username
//		
//		ProduceJavaFiles.produceJavaFiles(packageName, mainModule, authorModule, authorSwitch, joinColumn, template);
//		
//		ProduceSqlFiles.produceCreateTableSqlFiles(mainModule, authorModule, databaseType, database, authorSwitch, joinColumn);
//		
//		ProduceXmlFiles.produceXmlFile(packageName, mainModule, authorModule, authorSwitch, template);
//		
//		ProduceJspFiles.produceJspFiles(mainModule, template);
//	}
	
	public static void AddMultipleModuleFun() throws Exception {
		Module module = ParseXmlUtils.toBean("src/resource/module/Module.xml", Modules.class).getValidModule();
		module.getMainModule().setPackageName("com.eplugger.business.project");
//		String packageName = "com.eplugger.assess.personAssess";
		Map<String, String> superClassMap = new HashMap<String, String>();
		superClassMap.put("entity", "EntityImpl");
		superClassMap.put("bo", "BusinessBO");
		superClassMap.put("action", "BusinessAction");
		module.getMainModule().setSuperClassMap(superClassMap);
		String[] interfaces = null;
//		List<Field> fieldList = Constants.getFieldList();
		
//		ModuleInfo1 mainModule = new ModuleInfo1();
//		mainModule.setModuleName("ZXProjectCooperationAgreement");//模块名
//		mainModule.setModuleZHName("合作协议");//模块中文名
//		mainModule.setTableName("BIZ_PROJECT_COOPERATION_AGREEMENT");//数据库表名
//		mainModule.setBeanId("zXProjectCooperationAgreement");//beanId默认等同模块名
//		mainModule.setSuperClassMap(superClassMap);
//		mainModule.setInterfaces(interfaces);
//		mainModule.setFieldList(fieldList);
		
		Map<String, String> authorsuperClassMap = new HashMap<String, String>();
		authorsuperClassMap.put("entity", "EntityImpl");
		module.getAuthorModule().setSuperClassMap(authorsuperClassMap);
//		String[] authorinterfaces = null;
//		List<Field> authorfieldList = Constants.getAuthorfieldList();
//		
//		ModuleInfo1 authorModule = new ModuleInfo1();
//		authorModule.setModuleName("BookTestAuthor");
//		authorModule.setModuleZHName("著作作者");
//		authorModule.setTableName("BIZ_BOOK_TEST_AUTHOR");
//		authorModule.setBeanId("bookTestAuthor");
//		authorModule.setSuperClassMap(authorsuperClassMap);
//		authorModule.setInterfaces(authorinterfaces);
//		authorModule.setFieldList(authorfieldList);
		
		boolean authorSwitch = false;
		String joinColumn = "personAssessId";
		String template = "meeting";
		
		String databaseType = "sqlserver"; //数据库类型默认sqlserver,oracle
		String database = "RDSYSEDUV82310831"; //oracle数据库username
		
		ProduceJavaFiles.produceJavaFiles(module.getMainModule().getPackageName(), module.getMainModule(), module.getAuthorModule(), authorSwitch, joinColumn, template);
		
		ProduceSqlFactory.getInstance().produceCreateTableSqlFiles(module.getMainModule(), module.getAuthorModule(), authorSwitch);
		
		ProduceXmlFiles.produceXmlFile(module.getMainModule().getPackageName(), module.getMainModule(), module.getAuthorModule(), authorSwitch, template);
		
		ProduceJspFiles.produceJspFiles(module.getMainModule(), template);
	}
	
	/**
	 * 生成一个列表字段的所有东西
	 * @throws Exception 
	 */
	public static void AddListModuleFun() throws Exception {
		Module module = ParseXmlUtils.toBean("src/main/resource/module/Module.xml", Modules.class).getValidModule();
//		module.getMainModule().setPackageName("com.eplugger.business.projectApply");
//		Map<String, String> superClassMap = new HashMap<String, String>();
//		superClassMap.put("entity", "BizEntity");
//		module.getMainModule().setSuperClassMap(superClassMap);
		
//		String version = "V8.5.2";//eadp版本
		
		//多对一的字段
//		Field field = new Field("projectApplyBook", null, "ProjectApplyBook", null, "APPLY_BOOK_ID", null);
//		field.setAssociation(Constants.MANY_TO_ONE);
//		field.setIgnoreImport(true);
//		module.getMainModule().getFields().add(field);
		String template = "meeting";
		
		ProduceJavaFiles.produceJavaFiles(module);
		
		ProduceSqlFiles.produceCreateTableSqlFiles(module);
		
		ProduceXmlFiles.produceXmlFile(module.getMainModule().getPackageName(), module.getMainModule(), module.getAuthorModule(), false, template);
		
		ProduceJspFiles.produceJspFiles(module.getMainModule(), template);
	}
	
	/**
	 * 多个关联模块
	 * @throws Exception 
	 */
	public static void AddMultiModuleFun() throws Exception {
		List<Module> moduleList = ParseXmlUtils.toBean("src/main/resource/module/Module.xml", Modules.class).getValidList();
		for (Module module : moduleList) {
			AddSingleModuleFun(module);
			Constants.putFullClassNameMap(module.getMainModule().getModuleName(), module.getMainModule().getPackageName() + "." + module.getMainModule().getModuleName());
		}
	}
	
	public static void AddSingleModuleFun(Module module) {
		ProduceJavaFiles.produceJavaFiles(module);
		
		ProduceSqlFiles.produceCreateTableSqlFiles(module);
	}
	
	public static void AddMultipleModuleFun1(boolean authorSwitch, String template) throws Exception {
		ParseXmlUtils.registerBean(new ModuleParser(), Modules.class);
		List<Module> validList = ParseXmlUtils.toBean("src/main/resource/module/Module.xml", Modules.class).getValidList();
		ProduceCategorySqlCodeFactory.getInstance().produceSqlFiles(validList);
		for (Module module : validList) {
			ProduceJavaFactory.getInstance().produceJavaFiles(module, template);

			ProduceSqlFactory.getInstance().produceCreateTableSqlFiles(module.getMainModule(), module.getAuthorModule(), authorSwitch);

			ProduceXmlFiles.produceXmlFile(module.getMainModule().getPackageName(), module.getMainModule(), module.getAuthorModule(), authorSwitch, template);

			ProduceJspFiles.produceJspFiles(module.getMainModule(), template);

		}
	}
}
