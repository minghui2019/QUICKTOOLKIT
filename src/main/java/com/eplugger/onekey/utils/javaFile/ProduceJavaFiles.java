package com.eplugger.onekey.utils.javaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.entity.AppendSearch;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.Module;
import com.eplugger.onekey.entity.ModuleInfo;
import com.eplugger.utils.OtherUtils;

public class ProduceJavaFiles {
	private static String modulePath = FileUtils.getUserHomeDirectory() + "AddModule\\java\\";
	/**
	 * 
	 * @param packageName
	 * @param moduleInfo
	 * @param isAppField
	 */
	public static void produceJavaFilesSingle(String packageName, ModuleInfo moduleInfo) {
		// entity File
		String entityJavaCode = produceManyEntityJavaFile(packageName, moduleInfo);
		FileUtils.write(modulePath + moduleInfo.getBeanId() + File.separator + "entity" + File.separator + moduleInfo.getModuleName() + ".java", entityJavaCode);
		
		String entityJavaCodeSuper = produceOneEntityJavaFile(packageName, moduleInfo);
		FileUtils.write(modulePath + moduleInfo.getBeanId() + File.separator + "entity" + File.separator + moduleInfo.getModuleName() + "Super.java", entityJavaCodeSuper);
	}
	/**
	 * 生产java代码files
	 * @param packageName
	 * @param mainModule
	 * @param authorModule
	 * @param authorSwitch
	 * @param joinColumn
	 * @param template
	 */
	public static void produceJavaFiles(String packageName, ModuleInfo mainModule, ModuleInfo authorModule,
			boolean authorSwitch, String joinColumn, String template) {
		// entity File
		String entityJavaCode = ProduceJavaFactory.getInstance().produceEntityJavaFile(packageName, mainModule, false, null, "PersonAssess");
//		String entityJavaCode = produceEntityJavaFile(packageName, mainModule, false, null, null);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + mainModule.getModuleName() + ".java", entityJavaCode);
		if (authorSwitch) {
			String authorentityJavaCode = ProduceJavaFactory.getInstance().produceEntityJavaFile(packageName, authorModule, authorSwitch, mainModule.getBeanId(), mainModule.getModuleName());
			FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + authorModule.getModuleName() + ".java", authorentityJavaCode);
		}
		
		// bo File
		Set<AppendSearch> collectAppendSearch = mainModule.getFieldList().stream().map(a -> a.getAppendSearch()).filter(a -> StringUtils.isNotBlank(a.getValue())).distinct().collect(Collectors.toSet());
		String boJavaCode = ProduceJavaFactory.getInstance().produceBOJavaFile(mainModule, authorModule, authorSwitch, joinColumn, collectAppendSearch.size() != 0, false);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "BO.java", boJavaCode);
		
		// todo File
		String todoJavaCode = ProduceJavaFactory.getInstance().produceTodoJavaFile(packageName, mainModule);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "ToDoProvider.java", todoJavaCode);
		
		// action File
		String actionJavaCode = ProduceJavaFactory.getInstance().produceActionJavaFile(packageName, mainModule, authorSwitch, joinColumn, template);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "action" + File.separator + mainModule.getModuleName() + "Action.java", actionJavaCode);
	}

	
	/**
	 * 生产entity实体类file
	 * @param packageName
	 * @param module
	 * @param authorSwitch
	 * @param mainbeanId
	 * @param mainmoduleName
	 * @return
	 */
	private static String produceManyEntityJavaFile(String packageName, ModuleInfo module) {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + packageName + OtherUtils.SPOT + "entity;" + StringUtils.CRLF); //包名
		sb.append(StringUtils.CRLF);
		
		List<Field> fieldList = module.getFieldList();
		List<Field> newfieldList = fieldList .stream()
				.filter(a -> !(OtherUtils.TPYE_STRING.equals(a.getDataType()) || OtherUtils.TPYE_INTEGER.equals(a.getDataType()) || OtherUtils.TPYE_DOUBLE.equals(a.getDataType())))
				.filter(Field.distinctByKey(a -> a.getDataType())).collect(Collectors.toList());
		for (Field field : newfieldList) {
			if ("List".equals(field.getDataType())) {
				sb.append("import " + Constants.getFullClassNameMap(OtherUtils.TPYE_ARRAYLIST) + ";" + StringUtils.CRLF);
			} else if (!field.isIgnoreImport()) {
				sb.append("import " + Constants.getFullClassNameMap(field.getDataType()) + ";" + StringUtils.CRLF);
			}
		}
		
		sb.append(StringUtils.CRLF);
		
		Set<String> collectAssociation = fieldList.stream().map(a -> a.getAssociation()).filter(a -> a != null).distinct().collect(Collectors.toSet());
		if (collectAssociation.size() != 0) {
			if (collectAssociation.contains(Constants.ONE_TO_MANY)) {
				sb.append("import javax.persistence.OneToMany;" + StringUtils.CRLF);
			}
			if (collectAssociation.contains(Constants.MANY_TO_ONE)) {
				sb.append("import javax.persistence.ManyToOne;" + StringUtils.CRLF);
			}
			sb.append("import javax.persistence.JoinColumn;" + StringUtils.CRLF);
			sb.append("import javax.persistence.FetchType;" + StringUtils.CRLF);
			sb.append("import javax.persistence.CascadeType;" + StringUtils.CRLF);
		}
		
		Set<String> collectOrderBy = fieldList.stream().map(a -> a.getOrderBy()).filter(a -> a != null).distinct().collect(Collectors.toSet());
		if (collectOrderBy.size() != 0) {
			sb.append("import javax.persistence.OrderBy;" + StringUtils.CRLF);
		}
		Set<String> collectFetch = fieldList.stream().map(a -> a.getFetch()).filter(a -> a != null).distinct().collect(Collectors.toSet());
		if (collectFetch.size() != 0) {
			sb.append("import org.hibernate.annotations.Fetch;" + StringUtils.CRLF);
			sb.append("import org.hibernate.annotations.FetchMode;" + StringUtils.CRLF);
		}
		Set<String> collectGenericity = fieldList.stream().map(a -> a.getGenericity()).filter(a -> a != null && !(a.endsWith("Author") || a.endsWith("Member"))).distinct().collect(Collectors.toSet());
		for (String genericity : collectGenericity) {
			sb.append("import " + Constants.getFullClassNameMap(genericity) + ";" + StringUtils.CRLF);
		}
		
		Set<AppendSearch> collectAppendSearch = fieldList.stream().map(a -> a.getAppendSearch()).filter(a -> StringUtils.isNotBlank(a.getValue())).distinct().collect(Collectors.toSet());
		if (collectAppendSearch.size() != 0) {
			sb.append("import com.eplugger.service.dao.query.AppendSearch;" + StringUtils.CRLF);
		}
		
		sb.append("import javax.persistence.Column;" + StringUtils.CRLF);
		sb.append("import javax.persistence.Entity;" + StringUtils.CRLF);
		sb.append("import javax.persistence.Table;" + StringUtils.CRLF);
		sb.append("import javax.persistence.Transient;" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		String superClass = module.getSuperClassMap().get("entity");
		sb.append("import " + Constants.getFullClassNameMap(superClass) + ";" + StringUtils.CRLF);
		String impl = "";
		String[] interfaces = module.getInterfaces();
		if (interfaces  != null) {
			impl = " implements ";
			for (int i = 0; i < interfaces.length; i++) {
				sb.append("import " + Constants.getFullClassNameMap(interfaces[i]) + ";" + StringUtils.CRLF);
				impl += interfaces[i];
				if (i < interfaces.length - 1) {
					impl += ", ";
				}
			}
		}
		sb.append("import com.eplugger.system.entityMeta.support.EntityInfo;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.entityMeta.support.Meaning;" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append("/**" + StringUtils.CRLF);
		sb.append(" * " + module.getModuleZHName() + "实体类" + StringUtils.CRLF);
		sb.append(" * @author minghui" + StringUtils.CRLF);
		sb.append(" */" + StringUtils.CRLF);
		sb.append("@Entity" + StringUtils.CRLF);
		sb.append("@Table(name = \"" + module.getTableName() + "\")" + StringUtils.CRLF);
		sb.append("@EntityInfo(\"" + module.getModuleZHName() + "\")" + StringUtils.CRLF);
		sb.append("public class " + module.getModuleName());
		sb.append(" extends " + superClass);
		sb.append(impl + " {" + StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "private static final long serialVersionUID = 1L;" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(ProduceJavaFactory.getInstance().produceEntityJavaCode(fieldList));
		
		if ("BizEntity".equals(superClass)) {
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "@Transient" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String getName() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return null;" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		} else if ("CheckBusinessEntity".equals(superClass)) {
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "@Transient" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String getBizOwner() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return null;" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		} else {
			
		}
		sb.append("}");
		return sb.toString();
	}
	
	private static String produceOneEntityJavaFile(String packageName, ModuleInfo module) {
		StringBuffer sb = new StringBuffer();
		List<Field> fieldList = new ArrayList<Field>();
		for (Field field : module.getFieldList()) {
			if (StringUtils.isNotBlank(field.getAssociation()) && Constants.MANY_TO_ONE.equals(field.getAssociation())) {
				Field field1 = new Field(module.getBeanId() + "s", module.getModuleZHName(), "List", null, field.getJoinColumn(), module.getModuleName());
				field1.setAssociation(Constants.ONE_TO_MANY);
				field1.setOrderBy("orderId asc");
				fieldList.add(field1);
			}
		}
		sb.append(ProduceJavaFactory.getInstance().produceEntityJavaCode(fieldList));
		return sb.toString();
	}
	
	private static String listModulePath = FileUtils.getUserHomeDirectory() + "AddListModule\\java\\";
	
	public static void produceJavaFiles(Module module) {
		ModuleInfo mainModule = module.getMainModule();
		String moduleName = mainModule.getModuleName();
		boolean authorSwitch = module.getAuthorModule() != null;
		// entity File
		String entityJavaCode = ProduceJavaFactory.getInstance().produceEntityJavaFile(mainModule.getPackageName(), mainModule, false, null, moduleName);
		FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + moduleName + ".java", entityJavaCode);
		
		if (authorSwitch) {
			String authorentityJavaCode = ProduceJavaFactory.getInstance().produceEntityJavaFile(module.getAuthorModule().getPackageName(), module.getAuthorModule(), authorSwitch, mainModule.getBeanId(), mainModule.getModuleName());
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + module.getAuthorModule().getModuleName() + ".java", authorentityJavaCode);
		}
		
		if (StringUtils.isNotBlank(mainModule.getSuperClassMap().get("bo"))) {
			// bo File
			Set<AppendSearch> collectAppendSearch = mainModule.getFieldList().stream().filter(a -> a.getAppendSearch() != null).map(a -> a.getAppendSearch()).distinct().collect(Collectors.toSet());
			List<String> joinColumn = mainModule.getFieldList().stream().map(a -> a.getJoinColumn()).filter(a -> a != null).distinct().collect(Collectors.toList());
			String boJavaCode = ProduceJavaFactory.getInstance().produceBOJavaFile(mainModule, mainModule, authorSwitch, joinColumn.get(0), collectAppendSearch.size() != 0, false);
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "BO.java", boJavaCode);
			// todo File
			String todoJavaCode = ProduceJavaFactory.getInstance().produceTodoJavaFile(mainModule.getPackageName(), mainModule);
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "ToDoProvider.java", todoJavaCode);
			// action File
			String actionJavaCode = ProduceJavaFactory.getInstance().produceActionJavaFile(mainModule.getPackageName(), mainModule, authorSwitch, joinColumn.get(0), "");
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "action" + File.separator + mainModule.getModuleName() + "Action.java", actionJavaCode);
		}
	}
}
