package com.eplugger.onekey.utils.xmlFile;

import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.util.FileUtil;
import com.eplugger.util.OtherUtils;

public class ProduceXmlFiles {

	public static void produceXmlFileSingle(String packageName, ModuleInfo moduleInfo) {
		String moduleName = moduleInfo.getModuleName();
		String beanId = moduleInfo.getBeanId();
		String xmlCode = produceSpringXmlCode(packageName, moduleName, beanId);
		FileUtil.outFile(xmlCode, "C:\\Users\\Admin\\Desktop\\AddModule\\xml", "applicationContext-" + beanId + ".xml");
	}
	
	/**
	 * 
	 * @param packageName
	 * @param mainModule
	 * @param authorModule
	 * @param authorSwitch
	 * @param template 生成struts-business.xml开关，paper,honor,meeting
	 */
	public static void produceXmlFile(String packageName, ModuleInfo mainModule, ModuleInfo authorModule, boolean authorSwitch, String template) {
		String moduleName = mainModule.getModuleName();
		String beanId = mainModule.getBeanId();
		String moduleZHName = mainModule.getModuleZHName();
		String authormoduleName = authorModule.getModuleName();
		String authorbeanId = authorModule.getBeanId();
		String xmlCode = produceSpringXmlCode(packageName, moduleName, beanId, authormoduleName, authorbeanId, authorSwitch, mainModule.getSuperClassMap().get("bo"), mainModule.getSuperClassMap().get("action"));
		FileUtil.outFile(xmlCode, "C:\\Users\\Admin\\Desktop\\AddModule\\xml", "applicationContext-" + beanId + ".xml");
		
		if (!"meeting".equals(template)) {
			String strutsBusiness = produceStrutsXmlCode(packageName, moduleName, beanId, authormoduleName, authorbeanId, authorSwitch, moduleZHName, template);
			FileUtil.outFile(strutsBusiness, "C:\\Users\\Admin\\Desktop\\AddModule\\xml", "struts-business.xml");
		}
	}
	
	private static String produceStrutsXmlCode(String packageName, String moduleName, String beanId,
			String authormoduleName, String authorbeanId, boolean authorSwitch, String moduleZHName, String template) {
		StringBuffer sb = new StringBuffer();
		sb.append(OtherUtils.TAB_EIGHT + "<!-- " + moduleZHName + "-->" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "<action name=\"" + beanId + "Action!*\" method=\"{1}\" class=\"" + beanId + "Action\">" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<interceptor-ref name=\"actionSupport\"/>" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"assessResultDetail\">/business/" + beanId + "/assessResultDetail.jsp</result>" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"personalDelete\" type=\"chain\">vproductAction!do_personalLeftQuery</result>" + OtherUtils.CRLF);
		
		if ("paper".equals(template)) {
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "Next\" type=\"chain\">${beanId}Action!to_supportProjectAdd</result>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"supportProjectNext\" type=\"chain\">" + beanId + "Action!to_finishStep</result>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"supportProject\">/business/" + beanId + "/supportProject.jsp</result>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"finishStep\">/business/" + beanId + "/finishStep.jsp</result>" + OtherUtils.CRLF);
		}
		
		sb.append(OtherUtils.TAB_EIGHT + "</action>" + OtherUtils.CRLF);
		sb.append(OtherUtils.CRLF);
		
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "VIEW\" type=\"chain\">" + beanId + "Action!to_view</result>" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "EDIT\" type=\"chain\">" + beanId + "Action!to_edit</result>" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "list\" type=\"chain\">" + beanId + "Action!to_personalProductList</result>" + OtherUtils.CRLF);
		
		return sb.toString();
	}

	private static String produceSpringXmlCode(String packageName, String moduleName, String beanId) {
		StringBuffer sb = new StringBuffer();
		sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "\" class=\"" + packageName+ ".entity." + moduleName + "\" scope=\"prototype\" />" + OtherUtils.CRLF);
		return sb.toString();
	}
	
	private static String produceSpringXmlCode(String packageName, String moduleName, String beanId, String authormoduleName, String authorbeanId, boolean authorSwitch, String boSwitch, String actionSwitch) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + OtherUtils.CRLF);
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:aop=\"http://www.springframework.org/schema/aop\"" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "xmlns:context=\"http://www.springframework.org/schema/context\" xmlns:tx=\"http://www.springframework.org/schema/tx\"" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "xsi:schemaLocation=\"" + OtherUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd" + OtherUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd" + OtherUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd" + OtherUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\"" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "default-lazy-init=\"true\">" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "<!-- entity -->" + OtherUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "\" class=\"" + packageName+ ".entity." + moduleName + "\" scope=\"prototype\" />" + OtherUtils.CRLF);
		if (authorSwitch) {
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + authorbeanId + "\" class=\"" + packageName+ ".entity." + authormoduleName + "\" scope=\"prototype\" />" + OtherUtils.CRLF);
		}
		if (StringUtils.isNotBlank(boSwitch)) {
			sb.append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<!-- BO -->" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "BO\" class=\"" + packageName+ ".bo." + moduleName + "BO\" scope=\"singleton\" >" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<constructor-arg ref=\"businessDAO\"/>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "</bean>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "ToDoProvider\" class=\"" + packageName+ ".bo." + moduleName + "ToDoProvider\" scope=\"singleton\" lazy-init=\"false\"/>" + OtherUtils.CRLF);
		}
		if (StringUtils.isNotBlank(actionSwitch)) {
			sb.append(OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<!-- Action -->" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "Action\" class=\"" + packageName+ ".action." + moduleName + "Action\" scope=\"prototype\" >" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<constructor-arg ref=\"" + beanId + "BO\"/>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<property name=\"pagination\" ref=\"paginationImpl\"/>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<property name=\"entity\" ref=\"" + beanId + "\"/>" + OtherUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "</bean>" + OtherUtils.CRLF);
		}
		sb.append("</beans>" + OtherUtils.CRLF);
		return sb.toString();
	}

}
