package com.eplugger.onekey.utils.xmlFile;

import java.io.File;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.entity.ModuleInfo;
import com.eplugger.utils.OtherUtils;

public class ProduceXmlFiles {

	public static void produceXmlFileSingle(String packageName, ModuleInfo moduleInfo) {
		String moduleName = moduleInfo.getModuleName();
		String beanId = moduleInfo.getBeanId();
		String xmlCode = produceSpringXmlCode(packageName, moduleName, beanId);
		FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\xml" + File.separator + "applicationContext-" + beanId + ".xml", xmlCode);
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
		String authormoduleName = null, authorbeanId = null;
		try {
			authormoduleName = authorModule.getModuleName();
			authorbeanId = authorModule.getBeanId();
		} catch (Exception e) {
		}
		String xmlCode = produceSpringXmlCode(packageName, moduleName, beanId, authormoduleName, authorbeanId, authorSwitch, mainModule.getSuperClassMap().get("bo"), mainModule.getSuperClassMap().get("action"));
		FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\xml" + File.separator + "applicationContext-" + beanId + ".xml", xmlCode);
		
		if (!"meeting".equals(template)) {
			String strutsBusiness = produceStrutsXmlCode(packageName, moduleName, beanId, authormoduleName, authorbeanId, authorSwitch, moduleZHName, template);
			FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\xml" + File.separator + "struts-business.xml", strutsBusiness);
		}
	}
	
	private static String produceStrutsXmlCode(String packageName, String moduleName, String beanId,
			String authormoduleName, String authorbeanId, boolean authorSwitch, String moduleZHName, String template) {
		StringBuffer sb = new StringBuffer();
		sb.append(OtherUtils.TAB_EIGHT + "<!-- " + moduleZHName + "-->" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_EIGHT + "<action name=\"" + beanId + "Action!*\" method=\"{1}\" class=\"" + beanId + "Action\">" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<interceptor-ref name=\"actionSupport\"/>" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"assessResultDetail\">/business/" + beanId + "/assessResultDetail.jsp</result>" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"personalDelete\" type=\"chain\">vproductAction!do_personalLeftQuery</result>" + StringUtils.CRLF);
		
		if ("paper".equals(template)) {
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "Next\" type=\"chain\">${beanId}Action!to_supportProjectAdd</result>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"supportProjectNext\" type=\"chain\">" + beanId + "Action!to_finishStep</result>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"supportProject\">/business/" + beanId + "/supportProject.jsp</result>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_TWELVE + "<result name=\"finishStep\">/business/" + beanId + "/finishStep.jsp</result>" + StringUtils.CRLF);
		}
		
		sb.append(OtherUtils.TAB_EIGHT + "</action>" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "VIEW\" type=\"chain\">" + beanId + "Action!to_view</result>" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "EDIT\" type=\"chain\">" + beanId + "Action!to_edit</result>" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_TWELVE + "<result name=\"" + beanId + "list\" type=\"chain\">" + beanId + "Action!to_personalProductList</result>" + StringUtils.CRLF);
		
		return sb.toString();
	}

	private static String produceSpringXmlCode(String packageName, String moduleName, String beanId) {
		StringBuffer sb = new StringBuffer();
		sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "\" class=\"" + packageName+ ".entity." + moduleName + "\" scope=\"prototype\" />" + StringUtils.CRLF);
		return sb.toString();
	}
	
	private static String produceSpringXmlCode(String packageName, String moduleName, String beanId, String authormoduleName, String authorbeanId, boolean authorSwitch, String boSwitch, String actionSwitch) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + StringUtils.CRLF);
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:aop=\"http://www.springframework.org/schema/aop\"" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "xmlns:context=\"http://www.springframework.org/schema/context\" xmlns:tx=\"http://www.springframework.org/schema/tx\"" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "xsi:schemaLocation=\"" + StringUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd" + StringUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd" + StringUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd" + StringUtils.CRLF);
		sb.append("       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\"" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "default-lazy-init=\"true\">" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "<!-- entity -->" + StringUtils.CRLF);
		sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "\" class=\"" + packageName+ ".entity." + moduleName + "\" scope=\"prototype\" />" + StringUtils.CRLF);
		if (authorSwitch) {
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + authorbeanId + "\" class=\"" + packageName+ ".entity." + authormoduleName + "\" scope=\"prototype\" />" + StringUtils.CRLF);
		}
		if (StringUtils.isNotBlank(boSwitch)) {
			sb.append(StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<!-- BO -->" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "BO\" class=\"" + packageName+ ".bo." + moduleName + "BO\" scope=\"singleton\" >" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<constructor-arg ref=\"businessDAO\"/>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "</bean>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "ToDoProvider\" class=\"" + packageName+ ".bo." + moduleName + "ToDoProvider\" scope=\"singleton\" lazy-init=\"false\"/>" + StringUtils.CRLF);
		}
		if (StringUtils.isNotBlank(actionSwitch)) {
			sb.append(StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<!-- Action -->" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "<bean id=\"" + beanId + "Action\" class=\"" + packageName+ ".action." + moduleName + "Action\" scope=\"prototype\" >" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<constructor-arg ref=\"" + beanId + "BO\"/>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<property name=\"pagination\" ref=\"paginationImpl\"/>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_EIGHT + "<property name=\"entity\" ref=\"" + beanId + "\"/>" + StringUtils.CRLF);
			sb.append(OtherUtils.TAB_FOUR + "</bean>" + StringUtils.CRLF);
		}
		sb.append("</beans>" + StringUtils.CRLF);
		return sb.toString();
	}

}
