package com.eplugger.onekey.utils.javaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.entity.AppendSearch;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.addModule.entity.Module;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.utils.OtherUtils;
import com.google.common.base.Strings;

public class ProduceJavaFiles {
	private static String modulePath = "C:/Users/Admin/Desktop/AddModule/java/";
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
		String entityJavaCode = produceEntityJavaFile(packageName, mainModule, false, null, "PersonAssess");
//		String entityJavaCode = produceEntityJavaFile(packageName, mainModule, false, null, null);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + mainModule.getModuleName() + ".java", entityJavaCode);
		if (authorSwitch) {
			String authorentityJavaCode = produceEntityJavaFile(packageName, authorModule, authorSwitch, mainModule.getBeanId(), mainModule.getModuleName());
			FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + authorModule.getModuleName() + ".java", authorentityJavaCode);
		}
		
		// bo File
		Set<AppendSearch> collectAppendSearch = mainModule.getFields().stream().map(a -> a.getAppendSearch()).filter(a -> StringUtils.isNotBlank(a.getValue())).distinct().collect(Collectors.toSet());
		String boJavaCode = produceBOJavaFile(packageName, mainModule, authorSwitch, joinColumn, collectAppendSearch.size() != 0);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "BO.java", boJavaCode);
		
		// todo File
		String todoJavaCode = produceTodoJavaFile(packageName, mainModule);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "ToDoProvider.java", todoJavaCode);
		
		// action File
		String actionJavaCode = produceActionJavaFile(packageName, mainModule, authorSwitch, joinColumn, template);
		FileUtils.write(modulePath + mainModule.getBeanId() + File.separator + "action" + File.separator + mainModule.getModuleName() + "Action.java", actionJavaCode);
	}

	/**
	 * <p>生产Action控制类file</p>
	 * <p>action超类为空，返回<code>null</code></p>
	 * @param packageName
	 * @param module
	 * @param authorSwitch
	 * @param joinColumn
	 * @param template
	 * @return
	 */
	private static String produceActionJavaFile(String packageName, ModuleInfo module, boolean authorSwitch, String joinColumn, String template) {
		String superClass = module.getSuperClassMap().get("action");
		if (StringUtils.isBlank(superClass)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String beanId = module.getBeanId();
		String moduleName = module.getModuleName();
		String moduleZHName = module.getModuleZHName();
		sb.append("package " + packageName + OtherUtils.SPOT + "action;" + StringUtils.CRLF); //包名
		sb.append(StringUtils.CRLF);
		
		sb.append("import " + packageName + OtherUtils.SPOT + "bo" + OtherUtils.SPOT + moduleName + "BO;" + StringUtils.CRLF);
		sb.append("import " + packageName + OtherUtils.SPOT + "entity" + OtherUtils.SPOT + moduleName + ";" + StringUtils.CRLF);
		sb.append("import " + Constants.getFullClassNameMap(superClass) + ";" + StringUtils.CRLF);
		
		sb.append("import com.eplugger.abilities.workflow.utils.WorkFlowServ;" + StringUtils.CRLF);
		sb.append("import com.eplugger.business.person.entity.Person;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.role.user.bo.BaseUserBO;" + StringUtils.CRLF);
		sb.append("import com.eplugger.business.product.bo.VProductBO;" + StringUtils.CRLF);
		sb.append("import com.eplugger.serv.servPoint.BO.ServPointOperationLogBO;" + StringUtils.CRLF);
		sb.append("import com.eplugger.serv.servPoint.entity.ServPointEnum;" + StringUtils.CRLF);
		sb.append("import com.eplugger.serv.servPoint.entity.ServPointOperationLog;" + StringUtils.CRLF);
		sb.append("import com.eplugger.service.action.support.NoBackLog;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.checkWorkFlowLog.bo.CheckWorkflowLogBO;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.entity.UserInfo;" + StringUtils.CRLF);
		sb.append("import com.eplugger.utils.bean.SpringContextUtil;" + StringUtils.CRLF);
		sb.append("import org.apache.commons.lang3.StringUtils;" + StringUtils.CRLF);
		
		sb.append(StringUtils.CRLF);
		sb.append("/**" + StringUtils.CRLF);
		sb.append(" * " + moduleZHName + "控制类" + StringUtils.CRLF);
		sb.append(" * @author minghui" + StringUtils.CRLF);
		sb.append(" */" + StringUtils.CRLF);
		
		sb.append("public class " + moduleName + "Action extends " + superClass + "<" + moduleName + "BO, " + moduleName + "> {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "private static final long serialVersionUID = 1L;" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "Action() {}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "Action(" + moduleName + "BO bo) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "super(bo);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		String getEntityById = moduleName + " entity = bo.getEntityById(null, " + moduleName + ".class, getEntity().getId());";
		String getCurUserInfo = "UserInfo user = BaseUserBO.getCurUserInfo(UserInfo.class);";
		String getRequestAttribute = "String " + joinColumn + " = getRequestAttribute(\"" + joinColumn + "\");";
		String checkAuthor = "bo.checkAuthor(getEntity().getAuthors());";
		String updateFirstAuthor = "bo.updateFirstAuthor(getEntity(), getEntity().getAuthors());";
		
		if ("honor".equals(template)) {
			//to_add
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_add() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " entity = getEntity();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getCurUserInfo + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (user.isTeacher()) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "Person person = user.getPerson();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "bo.setCurUserAsAuthor(entity.getAuthors(), person);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "entity.setSubjectClassId(person.getSubjectClassId());" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "entity.setSubjectId(person.getSubjectId());" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return super.to_add();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//do_save
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "@NoBackLog" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_save() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + checkAuthor + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_save();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 更新第一作者信息" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + updateFirstAuthor + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (getIsNextStep()) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "setRequestAttribute(\"" + joinColumn + "\", getEntity().getId());" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "return \"" + beanId + "Next\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return result;" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);;
			
			//do_submit
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_submit() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + checkAuthor + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_submit();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 更新第一作者信息" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + updateFirstAuthor + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String checkStatus = getEntity().getCheckStatus();//得到当前审核状态，用于判断是否跳转评价" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 记录服务点使用次数" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getCurUserInfo + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (user.isServModel()) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "ServPointOperationLogBO.getInstance().save(new ServPointOperationLog(getEntity().getId(), getEntity().getName() , ServPointEnum.**.toString()));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String haveSubmit = WorkFlowServ.getPassVal(user.getGroupId(), getEntity());//获得当前角色已提交状态" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return to_Evaluate(ServPointEnum.**.toString(), getEntity().getId(), haveSubmit, checkStatus, result);//跳到评论列表" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//do_checkResultSave
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_checkResultSave() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_checkResultSave();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 审核后计数完成" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "ServPointOperationLogBO.getInstance().save(getEntity().getId(), ServPointEnum.**.toString(), getEntity().getCheckStatus());" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 审核后记录" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "CheckWorkflowLogBO.getInstance().recordItem(getEntity(), getEntity().getFirstAuthorId(), false);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return result;" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//do_personalLeftQuery
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_personalLeftQuery() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "super.do_personalLeftQuery();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "VProductBO vProductBO = SpringContextUtil.getBean(VProductBO.class);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "request.setAttribute(\"chargeNum\", vProductBO.getJoinCount(\"1\"));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "request.setAttribute(\"joinNum\", vProductBO.getJoinCount(\"2\"));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"personalProductList\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
		}
		
		if ("paper".equals(template)) {
			//step 1
			sb.append(OtherUtils.TAB_FOUR + "/**" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * step 1" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_" + beanId + "Add() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setPageType(\"ADD\");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(1);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return to_edit();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//step 2
			sb.append(OtherUtils.TAB_FOUR + "/**" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * step 2" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_supportProjectAdd() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getRequestAttribute + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (StringUtils.isNotBlank(" + joinColumn + ")) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "getEntity().setId(" + joinColumn + ");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getEntityById + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setPageType(\"ADD\");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(2);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"prvStepUrl\", \"" + beanId + "Action!to_" + beanId + "Add.action?entity.id=\" + getEntity().getId());//到基本信息" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectList\", bo.getSupportProject(getEntity().getId()));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"saveProjectList\", bo.getSaveSupportProject(getEntity().getId()));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"memberSelectHtml\", bo.createAuthorSelect(entity, null));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"supportProject\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//step 2
			sb.append(OtherUtils.TAB_FOUR + "/**" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * step 2" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_supportProject() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getEntityById + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectList\", bo.getSupportProject(getEntity().getId()));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"saveProjectList\", bo.getSaveSupportProject(getEntity().getId()));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"memberSelectHtml\", bo.createAuthorSelect(entity, null));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"supportProject\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//do_saveProductProject
			sb.append(OtherUtils.TAB_FOUR + "@NoBackLog" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_saveProductProject() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " entity = (" + moduleName + ") bo.createProjectProduct(getEntity(),getProjectIds());" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_save();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (getIsNextStep()) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "return \"supportProjectNext\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return result;" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//to_finishStep
			sb.append(OtherUtils.TAB_FOUR + "public String to_finishStep() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getRequestAttribute + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (StringUtils.isBlank(" + joinColumn + ")) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + joinColumn + " = getEntity().getId();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " entity = bo.getEntityById(null, " + moduleName + ".class, " + joinColumn + ");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String prvStepUrl = \"" + beanId + "Action!to_supportProjectAdd.action?entity.id=\" + entity.getId();//到依托项目" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setPageType(\"ADD\");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(3);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"prvStepUrl\", prvStepUrl);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"finishStep\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			//do_searchSupportProject
			sb.append(OtherUtils.TAB_FOUR + "public String do_searchSupportProject() {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String projectState = getRequestParameter(\"projectState\");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String memberId = getRequestParameter(\"memberId\");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getEntityById + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectList\", bo.getSupportProject(entity.getAuthors(), entity.getId(), projectState, memberId));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"saveProjectList\", bo.getSaveSupportProject(getEntity().getId()));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"memberSelectHtml\", bo.createAuthorSelect(entity, memberId));" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectState\", projectState);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(2);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"supportProject\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
		}
		
		sb.append("}");
		return sb.toString();
	}

	/**
	 * <p>生产Todo代办类file</p>
	 * <p>bo超类为空，返回<code>null</code></p>
	 * @param packageName
	 * @param module
	 * @return
	 */
	private static String produceTodoJavaFile(String packageName, ModuleInfo module) {
		if (StringUtils.isBlank(module.getSuperClassMap().get("bo"))) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String beanId = module.getBeanId();
		String moduleName = module.getModuleName();
		sb.append("package " + packageName + OtherUtils.SPOT + "bo;" + StringUtils.CRLF); //包名
		sb.append(StringUtils.CRLF);
		
		sb.append("import java.util.ArrayList;" + StringUtils.CRLF);
		sb.append("import java.util.List;" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("import com.eplugger.abilities.workflow.entity.ICheckAble;" + StringUtils.CRLF);
		sb.append("import com.eplugger.abilities.workflow.flowChart.Workflow;" + StringUtils.CRLF);
		sb.append("import com.eplugger.abilities.workflow.utils.WorkFlowServ;" + StringUtils.CRLF);
		sb.append("import com.eplugger.appService.base.entity.Token;" + StringUtils.CRLF);
		sb.append("import com.eplugger.appService.base.utils.TokenUtils;" + StringUtils.CRLF);
		sb.append("import com.eplugger.appService.todoItem.entity.AppTodoItem;" + StringUtils.CRLF);
		sb.append("import com.eplugger.business." + beanId + ".entity." + moduleName + ";" + StringUtils.CRLF);
		sb.append("import com.eplugger.business.pub.bo.BaseToDoProvider;" + StringUtils.CRLF);
		sb.append("import com.eplugger.service.action.HomepageBaseAction;" + StringUtils.CRLF);
		sb.append("import com.eplugger.service.convert.convert.date.DateConverter;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.category.CategoryDAO;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.entity.TodoItem;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.entity.UserInfo;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.role.user.bo.BaseUserBO;" + StringUtils.CRLF);
		sb.append("import com.eplugger.system.security.utils.SecurityUtils;" + StringUtils.CRLF);
		sb.append("import com.eplugger.utils.category.CategoryUtil;" + StringUtils.CRLF);
		sb.append("import com.eplugger.utils.date.DateUtil;" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append("public class " + moduleName + "ToDoProvider extends BaseToDoProvider {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "private String moduleName = \"" + module.getModuleZHName() + "\";" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "private String moduleActionName = \"" + beanId + "Action\";" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "ToDoProvider() {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT+ "HomepageBaseAction.registerPortal(getName(), " + moduleName + "ToDoProvider.class);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public String getName() {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return \"todos\";" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public List<TodoItem> getInfo() {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "List<TodoItem> infos = new ArrayList<TodoItem>();" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "List<" + moduleName + "> list = getList(" + moduleName + ".class);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "UserInfo userInfo = BaseUserBO.getCurUserInfo(UserInfo.class);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "for (" + moduleName + " " + beanId+ " : list) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "TodoItem item = new TodoItem();" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setModuleId(getBeanId());" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setModuleName(moduleName);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setTitle(" + beanId + ".getName());" + StringUtils.CRLF);
		String superClass = module.getSuperClassMap().get("entity");
		if ("Product".equals(superClass )) {
			sb.append(OtherUtils.TAB_TWELVE + "item.setChargerName(" + beanId + ".getFirstAuthorName());" + StringUtils.CRLF);
		} else {
			sb.append(OtherUtils.TAB_TWELVE + "item.setChargerName();" + StringUtils.CRLF);
		}
		sb.append(OtherUtils.TAB_TWELVE + "item.setHref(SecurityUtils.noCsrfURL(\"business/\" + moduleActionName + \"!to_checkPage.action?entity.id=\" + " + beanId + ".getId()));" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setLastEditDate(" + beanId + ".getLastEditDate());" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setDataId(" + beanId + ".getId());" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setOwnHref(SecurityUtils.noCsrfURL(\"business/\" + moduleActionName + \"!to_view.action?entity.id=\" + " + beanId + ".getId()));" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setOwnDetails(DateConverter.toString(" + beanId + ".getLastEditDate(), DateUtil.DATE_FORMAT_DATEONLY) + \"      \" + CategoryDAO.getInstance().convertVal(" + beanId + ".getCheckStatus(), \"CHECK_STATE\"));" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "if (WorkFlowServ.isCanOperation(userInfo.getGroupId(), (ICheckAble) " + beanId + ")) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_SIXTEEN + "item.setEditHref(SecurityUtils.noCsrfURL(\"business/\" + moduleActionName + \"!to_edit.action?entity.id=\" + " + beanId + ".getId()));" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "}" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "if (WorkFlowServ.isCanDelete((ICheckAble) " + beanId + ")) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_SIXTEEN + "item.setDeleteHref(SecurityUtils.noCsrfURL(moduleActionName + \"!do_delete.action?entity.id=\" + " + beanId + ".getId()));" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "}" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "infos.add(item);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return infos;" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public String getBeanId() {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return \"" + beanId + "\";" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public List<TodoItem> getServTodoItems(List<String> dataIds) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return null;" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public List<AppTodoItem> getAppTodoItems(List<String> dataIds) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return null;" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * <p>生产BO业务类file</p>
	 * <p>超类为空，返回<code>null</code></p>
	 * @param packageName
	 * @param module
	 * @param authorSwitch
	 * @param joinColumn
	 * @param appendSearch
	 * @return
	 */
	private static String produceBOJavaFile(String packageName, ModuleInfo module, boolean authorSwitch, String joinColumn, boolean appendSearch) {
		String superClass = module.getSuperClassMap().get("bo");
		if (StringUtils.isBlank(superClass)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String beanId = module.getBeanId();
		sb.append("package " + packageName + OtherUtils.SPOT + "bo;" + StringUtils.CRLF); //包名
		sb.append(StringUtils.CRLF);
		
		String moduleName = module.getModuleName();
		sb.append("import " + packageName + OtherUtils.SPOT + "entity" + OtherUtils.SPOT + moduleName  + ";" + StringUtils.CRLF);
		if (authorSwitch) { //作者
			sb.append("import " + packageName + OtherUtils.SPOT + "entity" + OtherUtils.SPOT + moduleName + "Author;" + StringUtils.CRLF);
		}
		sb.append("import " + Constants.getFullClassNameMap(superClass) + ";" + StringUtils.CRLF);
		sb.append("import com.eplugger.business.pub.dao.BusinessDAO;" + StringUtils.CRLF);
		if (true) { //关联项目
			sb.append("import com.eplugger.business.pub.entity.VProject;" + StringUtils.CRLF);
			sb.append("import " + Constants.getFullClassNameMap("ArrayList") + ";" + StringUtils.CRLF);
			sb.append("import " + Constants.getFullClassNameMap("List") + ";" + StringUtils.CRLF);
		}
		sb.append(StringUtils.CRLF);
		sb.append("/**" + StringUtils.CRLF);
		sb.append(" * " + module.getModuleZHName() + "业务类" + StringUtils.CRLF);
		sb.append(" * @author minghui" + StringUtils.CRLF);
		sb.append(" */" + StringUtils.CRLF);
		
		sb.append("public class " + moduleName + "BO extends " + superClass + "<BusinessDAO, " + moduleName + "> {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "BO() {}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "BO(BusinessDAO dao) {" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "super(dao);" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		String tableName = module.getTableName();
		if (true) { //关联项目
			sb.append(OtherUtils.TAB_FOUR + "/**" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * 获取依托项目" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public List<VProject> getSupportProject(String " + joinColumn + ") {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " " + beanId + " = getEntityById(null, " + moduleName + ".class, " + joinColumn + ");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (" + beanId + ".getAuthors().isEmpty()) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "return new ArrayList<VProject>();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String sql = \"select PERSON_ID from " + tableName + "_AUTHOR where " + SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn) + "='\" + " + joinColumn + " + \"'\";" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return getSupportProject(sql, " + joinColumn + ");" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
		}
		
		if (authorSwitch) {
			sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public void save(" + moduleName + " entity) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "mangeRelation(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "super.save(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			sb.append(OtherUtils.TAB_FOUR + "private void mangeRelation(" + moduleName + " entity) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "mangeAuthorRelation(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
			
			sb.append(OtherUtils.TAB_FOUR + "/**" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * 维护关联关系" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * @param entity" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "private void mangeAuthorRelation(" + moduleName + " entity) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "List<" + moduleName + "Author> authors = entity.getAuthors();" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "for (" + moduleName + "Author author : authors) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "author.set" + moduleName + "(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "author.set" + StringUtils.firstCharUpperCase(joinColumn) + "(entity.getId());" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
		}
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public void cascadeUpdateUnit(String personId, String oldUnitId, String newUnitId) {" + StringUtils.CRLF);
		if (joinColumn != null) {
			sb.append(OtherUtils.TAB_EIGHT + "super.cascadeUpdateUnit(\"" + tableName + "\", \"" + SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn) + "\", personId, oldUnitId, newUnitId);" + StringUtils.CRLF);
		}
		sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		if (appendSearch) {
			sb.append(OtherUtils.TAB_FOUR + "public void loadAppendSearch(" + moduleName+ " entity) {" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "dao.loadAppendSearch(entity);" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + StringUtils.CRLF);
			sb.append(StringUtils.CRLF);
		}
		sb.append("}");
		return sb.toString();
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
		
		List<Field> fieldList = module.getFields();
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
		
		sb.append(produceEntityJavaCode(fieldList));
		
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
		for (Field field : module.getFields()) {
			if (StringUtils.isNotBlank(field.getAssociation()) && Constants.MANY_TO_ONE.equals(field.getAssociation())) {
				Field field1 = new Field(module.getBeanId() + "s", module.getModuleZHName(), "List", null, field.getJoinColumn(), module.getModuleName());
				field1.setAssociation(Constants.ONE_TO_MANY);
				field1.setOrderBy("orderId asc");
				fieldList.add(field1);
			}
		}
		sb.append(produceEntityJavaCode(fieldList));
		return sb.toString();
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
	@Deprecated
	private static String produceEntityJavaFile(String packageName, ModuleInfo module, boolean authorSwitch, String mainbeanId, String mainmoduleName) {
		StringBuffer sb = new StringBuffer();
		if (authorSwitch) {
			sb.append("package " + packageName + OtherUtils.SPOT + "entity;" + StringUtils.CRLF); //包名
		} else {
			sb.append("package " + packageName + OtherUtils.SPOT + "entity;" + StringUtils.CRLF); //包名
		}
		sb.append(StringUtils.CRLF);
		
		List<Field> fieldList = module.getFields();
		List<String> javaTypeList = fieldList .stream().map(a -> a.getDataType())
				.filter(a -> !(OtherUtils.TPYE_STRING.equals(a) || OtherUtils.TPYE_INTEGER.equals(a) || OtherUtils.TPYE_DOUBLE.equals(a)))
				.distinct().collect(Collectors.toList());
		for (String javaType : javaTypeList) {
			if ("List".equals(javaType)) {
				sb.append("import " + Constants.getFullClassNameMap(OtherUtils.TPYE_ARRAYLIST) + ";" + StringUtils.CRLF);
			}
			if (!authorSwitch || !javaType.equals(mainmoduleName)) {
				if (Constants.getFullClassNameMap(javaType) != null) {
					sb.append("import " + Constants.getFullClassNameMap(javaType) + ";" + StringUtils.CRLF);
				}
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
		
		sb.append(produceEntityJavaCode(fieldList));
		
		sb.append(StringUtils.CRLF);
		
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
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 生产field代码，包括setter和getter
	 * @param fieldList
	 * @return
	 */
	public static String produceEntityJavaCode(List<Field> fieldList) {
		StringBuffer fieldSb = new StringBuffer();
		StringBuffer sgSb = new StringBuffer();
		
		for (Field field : fieldList) {
			if (field.isOnlyMeta() == true) {
				continue;
			}
			if (field.getFieldName() == null) {
				fieldSb.append(OtherUtils.TAB_FOUR + "private " + field.getDataType() + " " + field.getFieldId() + ";" + StringUtils.CRLF);
				
				sgSb.append(OtherUtils.TAB_FOUR + Constants.getAssociationMap(field.getAssociation()) + StringUtils.CRLF);
				sgSb.append(OtherUtils.TAB_FOUR + "@JoinColumn(name = \"" + field.getJoinColumn() + "\")" + StringUtils.CRLF);
				appendGetter(sgSb, field);
				appendSetter(sgSb, field);
			} else if (OtherUtils.TPYE_LIST.equals(field.getDataType())) {
				appendProperty(fieldSb, field);
				
				sgSb.append(OtherUtils.TAB_FOUR + Constants.getAssociationMap(field.getAssociation()) + StringUtils.CRLF);
				sgSb.append(OtherUtils.TAB_FOUR + "@JoinColumn(name = \"" + field.getJoinColumn() + "\")" + StringUtils.CRLF);
				if (field.getOrderBy() != null) {
					sgSb.append(OtherUtils.TAB_FOUR + "@OrderBy(\"" + field.getOrderBy() + "\")" + StringUtils.CRLF);
				}
				if (field.getFetch() != null) {
					sgSb.append(OtherUtils.TAB_FOUR + "@Fetch(value=" + field.getFetch() + ")" + StringUtils.CRLF);
				}
				appendGetter(sgSb, field);
				appendSetter(sgSb, field);
			} else {
				appendProperty(fieldSb, field);
			
				if (field.isTranSient()) {
					sgSb.append(OtherUtils.TAB_FOUR + "@Transient" + StringUtils.CRLF);
					AppendSearch appendSearch = field.getAppendSearch();
					if (appendSearch != null) {
						sgSb.append(OtherUtils.TAB_FOUR).append(appendSearch.toString());
					}
				} else {
					sgSb.append(OtherUtils.TAB_FOUR + "@Column(name = \"" + field.getTableFieldId() + "\"");
					if (!field.isUpdateInsert()) {
						sgSb.append(", updatable = false, insertable = false");
					}
					sgSb.append(")" + StringUtils.CRLF);
				}
				appendGetter(sgSb, field);
				appendSetter(sgSb, field);
			}
		}
		return fieldSb.toString() + StringUtils.CRLF + sgSb.toString();
	}
	
	private static void appendGetter(StringBuffer sb, Field field) {
		sb.append(OtherUtils.TAB_FOUR).append("public ").append(field.getDataType());
		if (!Strings.isNullOrEmpty(field.getGenericity())) {
			sb.append("<").append(field.getGenericity()).append(">");
		}
		sb.append(" get").append(StringUtils.firstCharUpperCase(field.getFieldId())).append("() {").append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT).append("return ").append(field.getFieldId()).append(";").append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR).append("}").append(StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
	}
	
	private static void appendSetter(StringBuffer sb, Field field) {
		sb.append(OtherUtils.TAB_FOUR).append("public void set").append(StringUtils.firstCharUpperCase(field.getFieldId())).append("(").append(field.getDataType());
		if (!Strings.isNullOrEmpty(field.getGenericity())) {
			sb.append("<").append(field.getGenericity()).append(">");
		}
		sb.append(" ").append(field.getFieldId()).append(") {").append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT).append("this.").append(field.getFieldId()).append(" = ").append(field.getFieldId()).append(";").append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR).append("}").append(StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
	}
	
	private static void appendProperty(StringBuffer sb, Field field) {
		sb.append(OtherUtils.TAB_FOUR).append("@Meaning(\"").append(field.getFieldName()).append("\")").append(StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR).append("private ").append(field.getDataType());
		if (!Strings.isNullOrEmpty(field.getGenericity())) {
			sb.append("<").append(field.getGenericity()).append(">");
		}
		sb.append(" ").append(field.getFieldId());
		if (!Strings.isNullOrEmpty(field.getGenericity())) {
			sb.append(" = new ArrayList<>()");
		}
		sb.append(";").append(StringUtils.CRLF);
	}
	
	private static String listModulePath = "C:/Users/Admin/Desktop/AddListModule/java/";
	
	public static void produceJavaFiles(Module module) {
		ModuleInfo mainModule = module.getMainModule();
		String moduleName = mainModule.getModuleName();
		boolean authorSwitch = module.getAuthorModule() != null;
		// entity File
		String entityJavaCode = ProduceJavaFactory.produceEntityJavaFile(mainModule.getPackageName(), mainModule, false, null, moduleName);
		FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + moduleName + ".java", entityJavaCode);
		
		if (authorSwitch) {
			String authorentityJavaCode = ProduceJavaFactory.produceEntityJavaFile(module.getAuthorModule().getPackageName(), module.getAuthorModule(), authorSwitch, mainModule.getBeanId(), mainModule.getModuleName());
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "entity" + File.separator + module.getAuthorModule().getModuleName() + ".java", authorentityJavaCode);
		}
		
		if (StringUtils.isNotBlank(mainModule.getSuperClassMap().get("bo"))) {
			// bo File
			Set<AppendSearch> collectAppendSearch = mainModule.getFields().stream().filter(a -> a.getAppendSearch() != null).map(a -> a.getAppendSearch()).distinct().collect(Collectors.toSet());
			List<String> joinColumn = mainModule.getFields().stream().map(a -> a.getJoinColumn()).filter(a -> a != null).distinct().collect(Collectors.toList());
			String boJavaCode = ProduceJavaFactory.produceBOJavaFile(mainModule.getPackageName(), mainModule, authorSwitch, joinColumn.get(0), collectAppendSearch.size() != 0);
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "BO.java", boJavaCode);
			// todo File
			String todoJavaCode = ProduceJavaFactory.produceTodoJavaFile(mainModule.getPackageName(), mainModule);
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "bo" + File.separator + mainModule.getModuleName() + "ToDoProvider.java", todoJavaCode);
			// action File
			String actionJavaCode = ProduceJavaFactory.produceActionJavaFile(mainModule.getPackageName(), mainModule, authorSwitch, joinColumn.get(0), "");
			FileUtils.write(listModulePath + mainModule.getBeanId() + File.separator + "action" + File.separator + mainModule.getModuleName() + "Action.java", actionJavaCode);
		}
	}
}
