package com.eplugger.onekey.utils.javaFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.eplugger.onekey.addField.entity.AppendSearch;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.util.OtherUtils;
import com.eplugger.util.StringUtils;

public class ProduceJavaFactory {
	
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
	public static String produceActionJavaFile(String packageName, ModuleInfo module, boolean authorSwitch, String joinColumn, String template) {
		String superClass = module.getSuperClassMap().get("action");
		if (StringUtils.isBlank(superClass)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String beanId = module.getBeanId();
		String moduleName = module.getModuleName();
		String moduleZHName = module.getModuleZHName();
		sb.append("package " + packageName + OtherUtils.SPOT + "action;" + OtherUtils.CRLF); //包名
		sb.append(OtherUtils.CRLF);
		
		sb.append("import " + packageName + OtherUtils.SPOT + "bo" + OtherUtils.SPOT + moduleName + "BO;" + OtherUtils.CRLF);
		sb.append("import " + packageName + OtherUtils.SPOT + "entity" + OtherUtils.SPOT + moduleName + ";" + OtherUtils.CRLF);
		sb.append("import " + Constants.getFullClassNameMap(superClass) + ";" + OtherUtils.CRLF);
		
		sb.append("import com.eplugger.abilities.workflow.utils.WorkFlowServ;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.business.person.entity.Person;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.role.user.bo.BaseUserBO;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.business.product.bo.VProductBO;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.serv.servPoint.BO.ServPointOperationLogBO;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.serv.servPoint.entity.ServPointEnum;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.serv.servPoint.entity.ServPointOperationLog;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.service.action.support.NoBackLog;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.checkWorkFlowLog.bo.CheckWorkflowLogBO;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.entity.UserInfo;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.utils.bean.SpringContextUtil;" + OtherUtils.CRLF);
		sb.append("import org.apache.commons.lang3.StringUtils;" + OtherUtils.CRLF);
		
		sb.append(OtherUtils.CRLF);
		sb.append("/**" + OtherUtils.CRLF);
		sb.append(" * " + moduleZHName + "控制类" + OtherUtils.CRLF);
		sb.append(" * @author minghui" + OtherUtils.CRLF);
		sb.append(" */" + OtherUtils.CRLF);
		
		sb.append("public class " + moduleName + "Action extends " + superClass + "<" + moduleName + "BO, " + moduleName + "> {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "private static final long serialVersionUID = 1L;" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "Action() {}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "Action(" + moduleName + "BO bo) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "super(bo);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		String getEntityById = moduleName + " entity = bo.getEntityById(null, " + moduleName + ".class, getEntity().getId());";
		String getCurUserInfo = "UserInfo user = BaseUserBO.getCurUserInfo(UserInfo.class);";
		String getRequestAttribute = "String " + joinColumn + " = getRequestAttribute(\"" + joinColumn + "\");";
		String checkAuthor = "bo.checkAuthor(getEntity().getAuthors());";
		String updateFirstAuthor = "bo.updateFirstAuthor(getEntity(), getEntity().getAuthors());";
		
		if ("honor".equals(template)) {
			//to_add
			sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_add() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " entity = getEntity();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getCurUserInfo + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (user.isTeacher()) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "Person person = user.getPerson();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "bo.setCurUserAsAuthor(entity.getAuthors(), person);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "entity.setSubjectClassId(person.getSubjectClassId());" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "entity.setSubjectId(person.getSubjectId());" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return super.to_add();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//do_save
			sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "@NoBackLog" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_save() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + checkAuthor + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_save();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 更新第一作者信息" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + updateFirstAuthor + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (getIsNextStep()) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "setRequestAttribute(\"" + joinColumn + "\", getEntity().getId());" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "return \"" + beanId + "Next\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return result;" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);;
			
			//do_submit
			sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_submit() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + checkAuthor + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_submit();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 更新第一作者信息" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + updateFirstAuthor + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String checkStatus = getEntity().getCheckStatus();//得到当前审核状态，用于判断是否跳转评价" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 记录服务点使用次数" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getCurUserInfo + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (user.isServModel()) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "ServPointOperationLogBO.getInstance().save(new ServPointOperationLog(getEntity().getId(), getEntity().getName() , ServPointEnum.**.toString()));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String haveSubmit = WorkFlowServ.getPassVal(user.getGroupId(), getEntity());//获得当前角色已提交状态" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return to_Evaluate(ServPointEnum.**.toString(), getEntity().getId(), haveSubmit, checkStatus, result);//跳到评论列表" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//do_checkResultSave
			sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_checkResultSave() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_checkResultSave();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 审核后计数完成" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "ServPointOperationLogBO.getInstance().save(getEntity().getId(), ServPointEnum.**.toString(), getEntity().getCheckStatus());" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "// 审核后记录" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "CheckWorkflowLogBO.getInstance().recordItem(getEntity(), getEntity().getFirstAuthorId(), false);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return result;" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//do_personalLeftQuery
			sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_personalLeftQuery() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "super.do_personalLeftQuery();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "VProductBO vProductBO = SpringContextUtil.getBean(VProductBO.class);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "request.setAttribute(\"chargeNum\", vProductBO.getJoinCount(\"1\"));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "request.setAttribute(\"joinNum\", vProductBO.getJoinCount(\"2\"));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"personalProductList\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
		}
		
		if ("paper".equals(template)) {
			//step 1
			sb.append(OtherUtils.TAB_FOUR + "/**" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * step 1" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_" + beanId + "Add() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setPageType(\"ADD\");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(1);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return to_edit();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//step 2
			sb.append(OtherUtils.TAB_FOUR + "/**" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * step 2" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_supportProjectAdd() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getRequestAttribute + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (StringUtils.isNotBlank(" + joinColumn + ")) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "getEntity().setId(" + joinColumn + ");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getEntityById + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setPageType(\"ADD\");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(2);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"prvStepUrl\", \"" + beanId + "Action!to_" + beanId + "Add.action?entity.id=\" + getEntity().getId());//到基本信息" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectList\", bo.getSupportProject(getEntity().getId()));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"saveProjectList\", bo.getSaveSupportProject(getEntity().getId()));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"memberSelectHtml\", bo.createAuthorSelect(entity, null));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"supportProject\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//step 2
			sb.append(OtherUtils.TAB_FOUR + "/**" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * step 2" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String to_supportProject() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getEntityById + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectList\", bo.getSupportProject(getEntity().getId()));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"saveProjectList\", bo.getSaveSupportProject(getEntity().getId()));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"memberSelectHtml\", bo.createAuthorSelect(entity, null));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"supportProject\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//do_saveProductProject
			sb.append(OtherUtils.TAB_FOUR + "@NoBackLog" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public String do_saveProductProject() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " entity = (" + moduleName + ") bo.createProjectProduct(getEntity(),getProjectIds());" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String result = super.do_save();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (getIsNextStep()) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "return \"supportProjectNext\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return result;" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//to_finishStep
			sb.append(OtherUtils.TAB_FOUR + "public String to_finishStep() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getRequestAttribute + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (StringUtils.isBlank(" + joinColumn + ")) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + joinColumn + " = getEntity().getId();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " entity = bo.getEntityById(null, " + moduleName + ".class, " + joinColumn + ");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String prvStepUrl = \"" + beanId + "Action!to_supportProjectAdd.action?entity.id=\" + entity.getId();//到依托项目" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setPageType(\"ADD\");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(3);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"prvStepUrl\", prvStepUrl);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"finishStep\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			//do_searchSupportProject
			sb.append(OtherUtils.TAB_FOUR + "public String do_searchSupportProject() {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String projectState = getRequestParameter(\"projectState\");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String memberId = getRequestParameter(\"memberId\");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + getEntityById + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setEntity(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectList\", bo.getSupportProject(entity.getAuthors(), entity.getId(), projectState, memberId));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"saveProjectList\", bo.getSaveSupportProject(getEntity().getId()));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"memberSelectHtml\", bo.createAuthorSelect(entity, memberId));" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setRequestAttribute(\"projectState\", projectState);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "setCurStepNum(2);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return \"supportProject\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
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
	public static String produceTodoJavaFile(String packageName, ModuleInfo module) {
		if (StringUtils.isBlank(module.getSuperClassMap().get("bo"))) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String beanId = module.getBeanId();
		String moduleName = module.getModuleName();
		sb.append("package " + packageName + OtherUtils.SPOT + "bo;" + OtherUtils.CRLF); //包名
		sb.append(OtherUtils.CRLF);
		
		sb.append("import java.util.ArrayList;" + OtherUtils.CRLF);
		sb.append("import java.util.List;" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		sb.append("import com.eplugger.abilities.workflow.entity.ICheckAble;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.abilities.workflow.flowChart.Workflow;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.abilities.workflow.utils.WorkFlowServ;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.appService.base.entity.Token;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.appService.base.utils.TokenUtils;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.appService.todoItem.entity.AppTodoItem;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.business." + beanId + ".entity." + moduleName + ";" + OtherUtils.CRLF);
		sb.append("import com.eplugger.business.pub.bo.BaseToDoProvider;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.service.action.HomepageBaseAction;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.service.convert.convert.date.DateConverter;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.category.CategoryDAO;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.entity.TodoItem;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.entity.UserInfo;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.role.user.bo.BaseUserBO;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.system.security.utils.SecurityUtils;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.utils.category.CategoryUtil;" + OtherUtils.CRLF);
		sb.append("import com.eplugger.utils.date.DateUtil;" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append("public class " + moduleName + "ToDoProvider extends BaseToDoProvider {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "private String moduleName = \"" + module.getModuleZHName() + "\";" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "private String moduleActionName = \"" + beanId + "Action\";" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "ToDoProvider() {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT+ "HomepageBaseAction.registerPortal(getName(), " + moduleName + "ToDoProvider.class);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public String getName() {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return \"todos\";" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public List<TodoItem> getInfo() {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "List<TodoItem> infos = new ArrayList<TodoItem>();" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "List<" + moduleName + "> list = getList(" + moduleName + ".class);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "UserInfo userInfo = BaseUserBO.getCurUserInfo(UserInfo.class);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "for (" + moduleName + " " + beanId+ " : list) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "TodoItem item = new TodoItem();" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setModuleId(getBeanId());" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setModuleName(moduleName);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setTitle(" + beanId + ".getName());" + OtherUtils.CRLF);
		String superClass = module.getSuperClassMap().get("entity");
		if ("Product".equals(superClass )) {
			sb.append(OtherUtils.TAB_TWELVE + "item.setChargerName(" + beanId + ".getFirstAuthorName());" + OtherUtils.CRLF);
		} else {
			sb.append(OtherUtils.TAB_TWELVE + "item.setChargerName();" + OtherUtils.CRLF);
		}
		sb.append(OtherUtils.TAB_TWELVE + "item.setHref(SecurityUtils.noCsrfURL(\"business/\" + moduleActionName + \"!to_checkPage.action?entity.id=\" + " + beanId + ".getId()));" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setLastEditDate(" + beanId + ".getLastEditDate());" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setDataId(" + beanId + ".getId());" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setOwnHref(SecurityUtils.noCsrfURL(\"business/\" + moduleActionName + \"!to_view.action?entity.id=\" + " + beanId + ".getId()));" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "item.setOwnDetails(DateConverter.toString(" + beanId + ".getLastEditDate(), DateUtil.DATE_FORMAT_DATEONLY) + \"      \" + CategoryDAO.getInstance().convertVal(" + beanId + ".getCheckStatus(), \"CHECK_STATE\"));" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "if (WorkFlowServ.isCanOperation(userInfo.getGroupId(), (ICheckAble) " + beanId + ")) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_SIXTEEN + "item.setEditHref(SecurityUtils.noCsrfURL(\"business/\" + moduleActionName + \"!to_edit.action?entity.id=\" + " + beanId + ".getId()));" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "if (WorkFlowServ.isCanDelete((ICheckAble) " + beanId + ")) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_SIXTEEN + "item.setDeleteHref(SecurityUtils.noCsrfURL(moduleActionName + \"!do_delete.action?entity.id=\" + " + beanId + ".getId()));" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "infos.add(item);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return infos;" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public String getBeanId() {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return \"" + beanId + "\";" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public List<TodoItem> getServTodoItems(List<String> dataIds) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return null;" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public List<AppTodoItem> getAppTodoItems(List<String> dataIds) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "return null;" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
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
	public static String produceBOJavaFile(String packageName, ModuleInfo module, boolean authorSwitch, String joinColumn, boolean appendSearch) {
		String superClass = module.getSuperClassMap().get("bo");
		if (StringUtils.isBlank(superClass)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String beanId = module.getBeanId();
		sb.append("package " + packageName + OtherUtils.SPOT + "bo;" + OtherUtils.CRLF); //包名
		sb.append(OtherUtils.CRLF);
		
		String moduleName = module.getModuleName();
		sb.append("import " + packageName + OtherUtils.SPOT + "entity" + OtherUtils.SPOT + moduleName  + ";" + OtherUtils.CRLF);
		if (authorSwitch) { //作者
			sb.append("import " + packageName + OtherUtils.SPOT + "entity" + OtherUtils.SPOT + moduleName + "Author;" + OtherUtils.CRLF);
		}
		sb.append("import " + Constants.getFullClassNameMap(superClass) + ";" + OtherUtils.CRLF);
		sb.append("import com.eplugger.business.pub.dao.BusinessDAO;" + OtherUtils.CRLF);
		if (true) { //关联项目
			sb.append("import com.eplugger.business.pub.entity.VProject;" + OtherUtils.CRLF);
			sb.append("import " + Constants.getFullClassNameMap("ArrayList") + ";" + OtherUtils.CRLF);
			sb.append("import " + Constants.getFullClassNameMap("List") + ";" + OtherUtils.CRLF);
		}
		sb.append(OtherUtils.CRLF);
		sb.append("/**" + OtherUtils.CRLF);
		sb.append(" * " + module.getModuleZHName() + "业务类" + OtherUtils.CRLF);
		sb.append(" * @author minghui" + OtherUtils.CRLF);
		sb.append(" */" + OtherUtils.CRLF);
		
		sb.append("public class " + moduleName + "BO extends " + superClass + "<BusinessDAO, " + moduleName + "> {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "BO() {}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public " + moduleName + "BO(BusinessDAO dao) {" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "super(dao);" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		String tableName = module.getTableName();
		if (true) { //关联项目
			sb.append(OtherUtils.TAB_FOUR + "/**" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * 获取依托项目" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public List<VProject> getSupportProject(String " + joinColumn + ") {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + moduleName + " " + beanId + " = getEntityById(null, " + moduleName + ".class, " + joinColumn + ");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "if (" + beanId + ".getAuthors().isEmpty()) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "return new ArrayList<VProject>();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "String sql = \"select PERSON_ID from " + tableName + "_AUTHOR where " + SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn) + "='\" + " + joinColumn + " + \"'\";" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "return getSupportProject(sql, " + joinColumn + ");" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
		}
		
		if (authorSwitch) {
			sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "public void save(" + moduleName + " entity) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "mangeRelation(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "super.save(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			sb.append(OtherUtils.TAB_FOUR + "private void mangeRelation(" + moduleName + " entity) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "mangeAuthorRelation(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
			
			sb.append(OtherUtils.TAB_FOUR + "/**" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * 维护关联关系" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " * @param entity" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + " */" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "private void mangeAuthorRelation(" + moduleName + " entity) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "List<" + moduleName + "Author> authors = entity.getAuthors();" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "for (" + moduleName + "Author author : authors) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "author.set" + moduleName + "(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "author.set" + OtherUtils.fristWorldUpperCase(joinColumn) + "(entity.getId());" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
		}
		
		sb.append(OtherUtils.TAB_FOUR + "@Override" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "public void cascadeUpdateUnit(String personId, String oldUnitId, String newUnitId) {" + OtherUtils.CRLF);
		if (joinColumn != null) {
			sb.append(OtherUtils.TAB_EIGHT + "super.cascadeUpdateUnit(\"" + tableName + "\", \"" + SqlUtils.lowerCamelCase2UnderScoreCase(joinColumn) + "\", personId, oldUnitId, newUnitId);" + OtherUtils.CRLF);
		}
		sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		if (appendSearch) {
			sb.append(OtherUtils.TAB_FOUR + "public void loadAppendSearch(" + moduleName+ " entity) {" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "dao.loadAppendSearch(entity);" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "}" + OtherUtils.CRLF);
			sb.append(OtherUtils.CRLF);
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
	public static String produceEntityJavaFile(String packageName, ModuleInfo module, boolean authorSwitch, String mainbeanId, String mainmoduleName) {
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(packageName).append(OtherUtils.SPOT).append("entity;").append(OtherUtils.CRLF); //包名
		sb.append(OtherUtils.CRLF);
		
		List<Field> fieldList = module.getFields();
		List<String> javaTypeList = fieldList .stream().map(a -> a.getDataType())
				.filter(a -> !(OtherUtils.TPYE_STRING.equals(a) || OtherUtils.TPYE_INTEGER.equals(a) || OtherUtils.TPYE_DOUBLE.equals(a)))
				.distinct().collect(Collectors.toList()); 
		for (String javaType : javaTypeList) {
			if (OtherUtils.TPYE_LIST.equals(javaType)) {
				sb.append("import ").append(Constants.getFullClassNameMap(OtherUtils.TPYE_ARRAYLIST)).append(";").append(OtherUtils.CRLF);
			}
			if (!authorSwitch || !javaType.equals(mainmoduleName)) {
				if (Constants.getFullClassNameMap(javaType) != null) {
					sb.append("import ").append(Constants.getFullClassNameMap(javaType)).append(";").append(OtherUtils.CRLF);
				}
			}
		}
		sb.append(OtherUtils.CRLF);
		
		Set<String> collectAssociation = fieldList.stream().map(a -> a.getAssociation()).filter(a -> a != null).distinct().collect(Collectors.toSet());
		if (collectAssociation.size() != 0) {
			if (collectAssociation.contains(Constants.ONE_TO_MANY)) {
				sb.append("import javax.persistence.OneToMany;").append(OtherUtils.CRLF);
			}
			if (collectAssociation.contains(Constants.MANY_TO_ONE)) {
				sb.append("import javax.persistence.ManyToOne;").append(OtherUtils.CRLF);
			}
			sb.append("import javax.persistence.JoinColumn;").append(OtherUtils.CRLF);
			sb.append("import javax.persistence.FetchType;").append(OtherUtils.CRLF);
			sb.append("import javax.persistence.CascadeType;").append(OtherUtils.CRLF);
		}
		
		Set<String> collectOrderBy = fieldList.stream().map(a -> a.getOrderBy()).filter(a -> a != null).distinct().collect(Collectors.toSet());
		if (collectOrderBy.size() != 0) {
			sb.append("import javax.persistence.OrderBy;").append(OtherUtils.CRLF);
		}
		Set<String> collectFetch = fieldList.stream().map(a -> a.getFetch()).filter(a -> a != null).distinct().collect(Collectors.toSet());
		if (collectFetch.size() != 0) {
			sb.append("import org.hibernate.annotations.Fetch;").append(OtherUtils.CRLF);
			sb.append("import org.hibernate.annotations.FetchMode;").append(OtherUtils.CRLF);
		}
		Set<String> collectGenericity = fieldList.stream().map(a -> a.getGenericity()).filter(a -> a != null && !(a.endsWith("Author") || a.endsWith("Member"))).distinct().collect(Collectors.toSet());
		for (String genericity : collectGenericity) {
			sb.append("import ").append(Constants.getFullClassNameMap(genericity)).append(";").append(OtherUtils.CRLF);
		}
		
		Set<AppendSearch> collectAppendSearch = fieldList.stream().map(a -> a.getAppendSearch()).filter(a -> StringUtils.isNotBlank(a.getValue())).distinct().collect(Collectors.toSet());
		if (collectAppendSearch.size() != 0) {
			sb.append("import com.eplugger.service.dao.query.AppendSearch;").append(OtherUtils.CRLF);
		}
		
		sb.append("import javax.persistence.Column;").append(OtherUtils.CRLF);
		sb.append("import javax.persistence.Entity;").append(OtherUtils.CRLF);
		sb.append("import javax.persistence.Table;").append(OtherUtils.CRLF);
		sb.append("import javax.persistence.Transient;").append(OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		String superClass = module.getSuperClassMap().get("entity");
		sb.append("import ").append(Constants.getFullClassNameMap(superClass)).append(";").append(OtherUtils.CRLF);
		String impl = "";
		String[] interfaces = module.getInterfaces();
		if (interfaces  != null) {
			impl = " implements ";
			for (int i = 0; i < interfaces.length; i++) {
				sb.append("import ").append(Constants.getFullClassNameMap(interfaces[i])).append(";").append(OtherUtils.CRLF);
				impl += interfaces[i];
				if (i < interfaces.length - 1) {
					impl += ", ";
				}
			}
		}
		sb.append("import com.eplugger.system.entityMeta.support.EntityInfo;").append(OtherUtils.CRLF);
		sb.append("import com.eplugger.system.entityMeta.support.Meaning;").append(OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append("/**").append(OtherUtils.CRLF);
		sb.append(" * ").append(module.getModuleZHName()).append("实体类").append(OtherUtils.CRLF);
		sb.append(" * @author minghui").append(OtherUtils.CRLF);
		sb.append(" */").append(OtherUtils.CRLF);
		sb.append("@Entity").append(OtherUtils.CRLF);
		sb.append("@Table(name = \"").append(module.getTableName()).append("\")").append(OtherUtils.CRLF);
		sb.append("@EntityInfo(\"").append(module.getModuleZHName()).append("\")").append(OtherUtils.CRLF);
		sb.append("public class ").append(module.getModuleName());
		sb.append(" extends ").append(superClass);
		sb.append(impl).append(" {").append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_FOUR).append("private static final long serialVersionUID = 1L;").append(OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(produceEntityJavaCode(fieldList));

		sb.append(productSuperClassJavaCode(superClass));
		
		sb.append("}");
		return sb.toString();
	}

	private static String productSuperClassJavaCode(String superClass) {
		StringBuffer sb = new StringBuffer();
		if ("BizEntity".equals(superClass)) {
			sb.append(OtherUtils.TAB_FOUR).append("@Override").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR).append("@Transient").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR).append("public String getName() {").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT).append("return null;").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR).append("}").append(OtherUtils.CRLF);
		} else if ("CheckBusinessEntity".equals(superClass)) {
			sb.append(OtherUtils.TAB_FOUR).append("@Override").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR).append("@Transient").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR).append("public String getBizOwner() {").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT).append("return null;").append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR).append("}").append(OtherUtils.CRLF);
		}
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
			if (field.getOnlyMeta() == true) {
				continue;
			}
			if (OtherUtils.TPYE_STRING.equals(field.getDataType()) || OtherUtils.TPYE_INTEGER.equals(field.getDataType())
					|| OtherUtils.TPYE_DOUBLE.equals(field.getDataType()) || OtherUtils.TPYE_DATE.equals(field.getDataType())
					|| OtherUtils.TPYE_TIMESTAMP.equals(field.getDataType())) {
				fieldSb.append(OtherUtils.TAB_FOUR).append("@Meaning(\"").append(field.getFieldName()).append("\")").append(OtherUtils.CRLF);
				fieldSb.append(OtherUtils.TAB_FOUR).append("private ").append(field.getDataType()).append(" ").append(field.getFieldId()).append(";").append(OtherUtils.CRLF);
			
				if (field.getTranSient()) {
					sgSb.append(OtherUtils.TAB_FOUR).append("@Transient").append(OtherUtils.CRLF);
					AppendSearch appendSearch = field.getAppendSearch();
					if (appendSearch != null) {
//						sgSb.append(OtherUtils.TAB_FOUR).append("@AppendSearch(value=\"").append(field.getAppendSearch()).append("\", relativeField=\"").append(field.getJoinColumn()).append("\", mergeMultiVals=true, symbol=\";\")").append(OtherUtils.CRLF);
						sgSb.append(OtherUtils.TAB_FOUR)
						.append("@AppendSearch(value=\"").append(appendSearch.getValue())
						.append("\", relativeField=\"").append(appendSearch.getRelativeField())
						.append("\", relativeThisProperty=\"").append(appendSearch.getRelativeThisProperty())
						.append("\", mergeMultiVals=").append(appendSearch.getMergeMultiVals())
						.append(", symbol=\"").append(appendSearch.getSymbol())
						.append("\")").append(OtherUtils.CRLF);
					}
				} else {
					sgSb.append(OtherUtils.TAB_FOUR).append("@Column(name = \"").append(field.getTableFieldId()).append("\"");
					if (!field.getUpdateInsert()) {
						sgSb.append(", updatable = false, insertable = false");
					}
					sgSb.append(")").append(OtherUtils.CRLF);
				}
				getterBuilder(sgSb, field);
				setterBuilder(sgSb, field);
			} else if (OtherUtils.TPYE_LIST.equals(field.getDataType())) {
				fieldSb.append(OtherUtils.TAB_FOUR).append("@Meaning(\"").append(field.getFieldName()).append("\")").append(OtherUtils.CRLF);
				fieldSb.append(OtherUtils.TAB_FOUR).append("private ").append(field.getDataType()).append("<").append(field.getGenericity()).append("> ").append(field.getFieldId()).append(" = new ArrayList<>();").append(OtherUtils.CRLF);
				
				sgSb.append(OtherUtils.TAB_FOUR).append(Constants.getAssociationMap(field.getAssociation())).append(OtherUtils.CRLF);
				sgSb.append(OtherUtils.TAB_FOUR).append("@JoinColumn(name = \"").append(field.getJoinColumn()).append("\")").append(OtherUtils.CRLF);
				if (field.getOrderBy() != null) {
					sgSb.append(OtherUtils.TAB_FOUR).append("@OrderBy(\"").append(field.getOrderBy()).append("\")").append(OtherUtils.CRLF);
				}
				if (field.getFetch() != null) {
					sgSb.append(OtherUtils.TAB_FOUR).append("@Fetch(value=").append(field.getFetch()).append(")").append(OtherUtils.CRLF);
				}
				getterBuilder(sgSb, field);
				setterBuilder(sgSb, field);
			} else {
				fieldSb.append(OtherUtils.TAB_FOUR).append("private ").append(field.getDataType()).append(" ").append(field.getFieldId()).append(";").append(OtherUtils.CRLF);
				
				sgSb.append(OtherUtils.TAB_FOUR).append(Constants.getAssociationMap(field.getAssociation())).append(OtherUtils.CRLF);
				sgSb.append(OtherUtils.TAB_FOUR).append("@JoinColumn(name = \"").append(field.getJoinColumn()).append("\"");
				if (!field.getUpdateInsert()) {
					sgSb.append(", updatable = false, insertable = false");
				}
				sgSb.append(")").append(OtherUtils.CRLF);
				getterBuilder(sgSb, field);
				setterBuilder(sgSb, field);
			}
			
		}
		return fieldSb.toString() + OtherUtils.CRLF + sgSb.toString();
	}

	private static void setterBuilder(StringBuffer sgSb, Field field) {
		sgSb.append(OtherUtils.TAB_FOUR).append("public void set").append(OtherUtils.fristWorldUpperCase(field.getFieldId())).append("(").append(field.getDataType());
		if (StringUtils.isNotBlank(field.getGenericity())) {
			sgSb.append("<").append(field.getGenericity()).append("> ");
		} else {
			sgSb.append(" ");
		}
		sgSb.append(field.getFieldId()).append(") {").append(OtherUtils.CRLF);
		sgSb.append(OtherUtils.TAB_EIGHT).append("this.").append(field.getFieldId()).append(" = ").append(field.getFieldId()).append(";").append(OtherUtils.CRLF);
		sgSb.append(OtherUtils.TAB_FOUR).append("}").append(OtherUtils.CRLF);
		sgSb.append(OtherUtils.CRLF);
	}

	private static void getterBuilder(StringBuffer sgSb, Field field) {
		sgSb.append(OtherUtils.TAB_FOUR).append("public ").append(field.getDataType());
		if (StringUtils.isNotBlank(field.getGenericity())) {
			sgSb.append("<").append(field.getGenericity()).append("> get");
		} else {
			sgSb.append(" get");
		}
		sgSb.append(OtherUtils.fristWorldUpperCase(field.getFieldId())).append("() {").append(OtherUtils.CRLF);
		sgSb.append(OtherUtils.TAB_EIGHT).append("return ").append(field.getFieldId()).append(";").append(OtherUtils.CRLF);
		sgSb.append(OtherUtils.TAB_FOUR).append("}").append(OtherUtils.CRLF);
		sgSb.append(OtherUtils.CRLF);
	}
	
	
}
