package com.eplugger.onekey.utils.jspFile;

import java.io.File;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addModule.entity.ModuleInfo;

public class ProduceSingleJspFiles {

	public static void produceJspFiles(ModuleInfo moduleInfo, String template) {
		String beanId = moduleInfo.getBeanId();
		String moduleZHName = moduleInfo.getModuleZHName();
		if (StringUtils.isBlank(moduleInfo.getSuperClassMap().get("bo"))) {
			return;
		}
		// add File
		String addCode = produceAddJspFile(template, beanId);
		FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + beanId + "Add.jsp", addCode);
		
		// check File
		String checkCode = produceCheckJspFile(template, beanId);
		FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + beanId + "Check.jsp", checkCode);
		
		// list File
		String listCode = produceListJspFile(beanId, template, moduleZHName);
		FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + beanId + "List.jsp", listCode);
		
		// top File
		if ("paper".equals(template)) {
			String topCode = produceTopJspFile(beanId);
			FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + beanId + "Top.jsp", topCode);
		}
		
		// view File
		String viewCode = produceViewJspFile(beanId, template, moduleZHName);
		FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + beanId + "View.jsp", viewCode);
		
		// finishStep File
		if ("paper".equals(template)) {
			String finishStepCode = produceFinishStepJspFile(beanId, template, moduleZHName);
			FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + "finishStep.jsp", finishStepCode);
		}
		
		// supportProject File
		if ("paper".equals(template)) {
			String supportProjectCode = produceSupportProjectJspFile(beanId, template, moduleZHName);
			FileUtils.write("C:\\Users\\Admin\\Desktop\\AddModule\\jsp\\" + beanId + File.separator + "supportProject.jsp", supportProjectCode);
		}
	}
	
	private static String produceSupportProjectJspFile(String beanId, String template, String moduleZHName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html; charset=UTF-8\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"fn\" uri=\"http://java.sun.com/jsp/jstl/functions\"%>" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("<!DOCTYPE html>" + StringUtils.CRLF);
		sb.append("<html lang=\"zh-cn\">" + StringUtils.CRLF);
		sb.append("<head>" + StringUtils.CRLF);
		sb.append("<e:import resource=\"datepicker,validator,float,ep\" />" + StringUtils.CRLF);
		sb.append("<script src=\"${contextPath}/business/js/pub.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
		sb.append("<script src=\"${contextPath}/business/js/selectTable.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
		sb.append("<script type=\"text/javascript\">" + StringUtils.CRLF);
		sb.append("function checkForm() {" + StringUtils.CRLF);
		sb.append("    var exportCols = $(\"#toSelect\");" + StringUtils.CRLF);
		sb.append("    exportCols.children().each(function() {" + StringUtils.CRLF);
		sb.append("        $(this).prop(\"selected\", \"selected\");" + StringUtils.CRLF);
		sb.append("    });" + StringUtils.CRLF);
		sb.append("    return true;" + StringUtils.CRLF);
		sb.append("}" + StringUtils.CRLF);
		sb.append("/**" + StringUtils.CRLF);
		sb.append(" * 查询操作" + StringUtils.CRLF);
		sb.append(" */" + StringUtils.CRLF);
		sb.append("function submitSearch(){" + StringUtils.CRLF);
		sb.append("    window.location.href=\"${contextPath}/business/" + beanId + "Action!do_searchSupportProject.action?${e:csrfparam()}&projectState=\"+$(\"select[name='projectState']\").val()+\"&webFlowId=\"+$(\"input[name='webFlowId']\").val()+\"&memberId=\"+$(\"select[name='memberId']\").val()+\"&entity.id=\"+$(\"select[name='entity.id']\").val()+\"&pageType=\"+$(\"select[name='pageType']\").val();" + StringUtils.CRLF);
		sb.append("}" + StringUtils.CRLF);
		sb.append("</script>" + StringUtils.CRLF);
		sb.append("</head>" + StringUtils.CRLF);
		sb.append("<body class=\"ep-nopadding-body popOperate\">" + StringUtils.CRLF);
		sb.append("<form action=\"${actionPath}!do_saveProductProject.action\" method=\"post\">" + StringUtils.CRLF);
		sb.append("    <e:csrfToken/>" + StringUtils.CRLF);
		sb.append("    <input type=\"hidden\" name=\"entity.id\" value=\"${entity.id}\"/>" + StringUtils.CRLF);
		sb.append("    <input type=\"hidden\" name=\"pageType\" value=\"${pageType}\"/>" + StringUtils.CRLF);
		sb.append("    <s:include value=\"" + beanId + "Top.jsp\">" + StringUtils.CRLF);
		sb.append("        <s:param name=\"module\">supportProject</s:param>" + StringUtils.CRLF);
		sb.append("    </s:include> " + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("    <s:include value=\"../product/supportProject.jsp\">" + StringUtils.CRLF);
		sb.append("        <s:param name=\"entityName\">" + beanId + "</s:param>" + StringUtils.CRLF);
		sb.append("    </s:include>" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("    <e:foot layerFooter=\"true\">" + StringUtils.CRLF);
		sb.append("        <div class=\"ep-footer\">" + StringUtils.CRLF);
		sb.append("            <e:save submitHref=\"${actionPath}!do_productProjectSubmit.action\" icon4Save=\"\" icon4Submit=\"\" cls4Submit=\"btn btn-primary btn-sm\" cls4Save=\"btn btn-primary btn-sm\" visible=\"${pageType=='EDIT'}\" onSubmit=\"checkForm\"/>" + StringUtils.CRLF);
		sb.append("            <e:btn label=\"上一步\" href=\"${prvStepUrl}\" cls=\"btn btn-primary btn-sm\" test=\"${pageType=='ADD'}\"/>" + StringUtils.CRLF);
		sb.append("            <e:btn label=\"下一步\" onclick=\"ep.saveForm(this,'',true,true,checkForm,null,true,'');\" cls=\"btn btn-primary btn-sm\" test=\"${pageType=='ADD'}\"/> " + StringUtils.CRLF);
		sb.append("            <e:btn label=\"关闭\" onclick=\"closeThis(${e:userInfo().servModel},${pageType == 'ADD'},false,null)\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
		sb.append("        </div>" + StringUtils.CRLF);
		sb.append("</e:foot>" + StringUtils.CRLF);
		sb.append("</form>" + StringUtils.CRLF);
		sb.append("</body>" + StringUtils.CRLF);
		sb.append("</html>" + StringUtils.CRLF);
		return sb.toString();
	}

	private static String produceFinishStepJspFile(String beanId, String template, String moduleZHName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html; charset=UTF-8\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\" %>" + StringUtils.CRLF);
		sb.append("<!DOCTYPE html>" + StringUtils.CRLF);
		sb.append("<html lang=\"zh-cn\">" + StringUtils.CRLF);
		sb.append("<head>" + StringUtils.CRLF);
		sb.append("<e:import resource=\"ep,validator\" />" + StringUtils.CRLF);
		sb.append("<link href=\"${contextPath}/business/css/finishStep.css\" rel=\"stylesheet\" charset=\"UTF-8\">" + StringUtils.CRLF);
		sb.append("<script src=\"${contextPath}/business/js/pub.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
		sb.append("<script type=\"text/javascript\">" + StringUtils.CRLF);
		sb.append("$(function() {" + StringUtils.CRLF);
		sb.append("    var csrfparam = \"${e:csrfparam()}\";" + StringUtils.CRLF);
		sb.append("    var entityId = \"${entity.id}\";" + StringUtils.CRLF);
		sb.append("    getAssessScore(entityId,csrfparam);" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("    $(\"#finishForm\").bind(\"afterSubmit\",function(){" + StringUtils.CRLF);
		sb.append("        var windowName = window.name;" + StringUtils.CRLF);
		sb.append("        window.parent[windowName] = \"refresh\";" + StringUtils.CRLF);
		sb.append("    });" + StringUtils.CRLF);
		sb.append("});" + StringUtils.CRLF);
		sb.append("</script>" + StringUtils.CRLF);
		sb.append("</head>" + StringUtils.CRLF);
		sb.append("<body class=\"ep-nopadding-body popOperate\">" + StringUtils.CRLF);
		sb.append("    <form id=\"finishForm\" action=\"${actionPath}!do_save.action\" method=\"post\">" + StringUtils.CRLF);
		sb.append("            <s:include value=\"" + beanId + "Top.jsp\">" + StringUtils.CRLF);
		sb.append("                <s:param name=\"module\">finishStep</s:param>" + StringUtils.CRLF);
		sb.append("            </s:include> " + StringUtils.CRLF);
		sb.append("        <e:csrfToken/>" + StringUtils.CRLF);
		sb.append("        <input type=\"hidden\" name=\"entity.id\" value=\"${entity.id}\">" + StringUtils.CRLF);
		sb.append("         <div id=\"contentAll\">" + StringUtils.CRLF);
		sb.append("             <div class=\"leftPic\"><img style=\"padding: 0 0 30px 10px;\" src=\"${contextPath}/business/img/ok.png\"></div>" + StringUtils.CRLF);
		sb.append("             <div id='rightDec'>" + StringUtils.CRLF);
		sb.append("                <span style='margin-top:10px'>" + StringUtils.CRLF);
		sb.append("                    <font style='color:#000;font-size:18px;font-weight:bold;'>信息登记完成！</font><br/>" + StringUtils.CRLF);
		sb.append("                    <div style='color:#000;font-size:14px;display:none;color:green;' id=\"entityAssessArea\">" + StringUtils.CRLF);
		sb.append("                        该成果的考核得分为：<span id=\"entityScoreArea\"></span>分" + StringUtils.CRLF);
		sb.append("                    </div>" + StringUtils.CRLF);
		sb.append("                    <c:if test=\"${e:userInfo().teacher && !e:userInfo().servModel}\">" + StringUtils.CRLF);
		sb.append("                        信息已<span class=\"saveRemind\">暂存</span>，您可以直接<span class=\"submitRemind\">提交</span>，由管理员进行审核。" + StringUtils.CRLF);
		sb.append("                    </c:if>" + StringUtils.CRLF);
		sb.append("                    <c:if test=\"${e:userInfo().teacher && e:userInfo().servModel}\">" + StringUtils.CRLF);
		sb.append("                        信息已<span class=\"saveRemind\">暂存</span>，您可以直接<span class=\"submitRemind\">提交</span>，由管理员进行审核。" + StringUtils.CRLF);
		sb.append("                    </c:if>" + StringUtils.CRLF);
		sb.append("                    <c:if test=\"${!e:userInfo().teacher}\">" + StringUtils.CRLF);
		sb.append("                        信息已<span class=\"saveRemind\">暂存</span>，您可以直接<span class=\"submitRemind\">审核通过</span>。" + StringUtils.CRLF);
		sb.append("                    </c:if>" + StringUtils.CRLF);
		sb.append("                </span>" + StringUtils.CRLF);
		sb.append("            </div>" + StringUtils.CRLF);
		sb.append("        </div>" + StringUtils.CRLF);
		sb.append("        <e:foot layerFooter=\"true\">" + StringUtils.CRLF);
		sb.append("            <div class=\"ep-footer\">" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"上一步\" href=\"${prvStepUrl}\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"${e:userInfo().teacher ? '提交' : '审核通过'}\" onclick=\"temp2submit(this, ${e:userInfo().teacher}, ${e:userInfo().servModel}, '${contextPath}/business/" + beanId + "Action')\" cls=\"btn btn-primary btn-sm\"></e:btn>" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"关闭\" onclick=\"closeThis(${e:userInfo().servModel},${pageType == 'ADD'},false,null)\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
		sb.append("            </div>" + StringUtils.CRLF);
		sb.append("        </e:foot>" + StringUtils.CRLF);
		sb.append("    </form>" + StringUtils.CRLF);
		sb.append("</body>" + StringUtils.CRLF);
		sb.append("</html>" + StringUtils.CRLF);
		return sb.toString();
	}

	private static String produceTopJspFile(String beanId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html; charset=UTF-8\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>" + StringUtils.CRLF);
		sb.append("<c:if test=\"${!isHistoryDateComplete }\">" + StringUtils.CRLF);
		sb.append("    <c:if test=\"${pageType!='ADD'}\">" + StringUtils.CRLF);
		sb.append("        <e:tabPanel>" + StringUtils.CRLF);
		sb.append("            <e:tab label=\"基本信息\" href=\"${contextPath}/business/" + beanId + "Action!${pageType=='EDIT'?'to_edit':'to_view'}.action?entity.id=${param.entityId == null ? entity.id : param.entityId}\" active=\"${param.module == 'baseInfo'}\" static=\"${param.module == 'baseInfo'}\"/>" + StringUtils.CRLF);
		sb.append("            <e:tab label=\"依托项目\" href=\"${contextPath}/business/" + beanId + "Action!to_supportProject.action?entity.id=${param.entityId == null ? entity.id : param.entityId}&pageType=${pageType}\" active=\"${param.module == 'supportProject'}\" static=\"${param.module == 'supportProject'}\"/>" + StringUtils.CRLF);
		sb.append("        </e:tabPanel>" + StringUtils.CRLF);
		sb.append("    </c:if>" + StringUtils.CRLF);
		sb.append("    <c:if test=\"${pageType=='ADD'}\">" + StringUtils.CRLF);
		sb.append("        <div class='step'>" + StringUtils.CRLF);
		sb.append("            <input type=\"hidden\" name=\"isNextStep\" value=\"${(pageType!='ADD' || param.module == 'finishStep')?'false':'true'}\">" + StringUtils.CRLF);
		sb.append("            <dl class='step1 ${curStepNum==1?\"\":\"step_n\"}'>" + StringUtils.CRLF);
		sb.append("                <dt class='${curStepNum==1?\"step_give\":(curStepNum>1?\"step_give2\":\"step2\")}'><a>1</a></dt>" + StringUtils.CRLF);
		sb.append("                <dd class='${curStepNum==1?\"step_give1\":(curStepNum>1?\"step_give4\":\"step3\")}'><span>1.基本信息</span></dd>" + StringUtils.CRLF);
		sb.append("            </dl>" + StringUtils.CRLF);
		sb.append("            <dl class='step1 ${curStepNum==2?\"\":\"step_n\"}'>" + StringUtils.CRLF);
		sb.append("                <dt class='${curStepNum==2?\"step_give\":(curStepNum>2?\"step_give2\":\"step2\")}'><a>2</a></dt>" + StringUtils.CRLF);
		sb.append("                <dd class='${curStepNum==2?\"step_give1\":(curStepNum>2?\"step_give4\":\"step3\")}'><span>2.依托项目</span></dd>" + StringUtils.CRLF);
		sb.append("            </dl>" + StringUtils.CRLF);
		sb.append("            <dl class='step1 ${curStepNum==3?\"\":\"step_n\"}'>" + StringUtils.CRLF);
		sb.append("                <dt class='${curStepNum==3?\"step_give\":(curStepNum>3?\"step_give2\":\"step2\")}'><a>3</a></dt>" + StringUtils.CRLF);
		sb.append("                <dd class='${curStepNum==3?\"step_give1\":(curStepNum>3?\"step_give4\":\"step3\")}'><span>3.完成登记</span></dd>" + StringUtils.CRLF);
		sb.append("            </dl>" + StringUtils.CRLF);
		sb.append("        </div> " + StringUtils.CRLF);
		sb.append("    </c:if>" + StringUtils.CRLF);
		sb.append("    <div class=\"${pageType=='ADD'?'stepAddDiv':'stepNotAddDiv'}\"></div>" + StringUtils.CRLF);
		sb.append("</c:if>" + StringUtils.CRLF);
		return sb.toString();
	}

	private static String produceViewJspFile(String beanId, String template, String moduleZHName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html;charset=utf-8\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\" %>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("<!DOCTYPE html>" + StringUtils.CRLF);
		sb.append("<html lang=\"zh-cn\">" + StringUtils.CRLF);
		sb.append("<head>" + StringUtils.CRLF);
		sb.append("<e:import resource=\"float,ep\" />" + StringUtils.CRLF);
		if ("paper".equals(template) || "honor".equals(template)) {
        	sb.append("<script src=\"${contextPath}/business/js/pub.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
        }
		sb.append("</head>" + StringUtils.CRLF);
		sb.append("<body class=\"ep-nopadding-body popOperate\">" + StringUtils.CRLF);
		if ("paper".equals(template)) {
			sb.append("    <s:include value=\"" + beanId + "Top.jsp\">" + StringUtils.CRLF);
        	sb.append("        <s:param name=\"module\">baseInfo</s:param>" + StringUtils.CRLF);
        	sb.append("    </s:include>" + StringUtils.CRLF);
        }
		sb.append("    <s:if test=\"pageType=='VIEW'\">" + StringUtils.CRLF);
		sb.append("        <s:include value=\"../pub/pubCheckHeader.jsp\">" + StringUtils.CRLF);
		sb.append("            <s:param name=\"refuseCheckLogList\">${request.refuseCheckLogList}</s:param>" + StringUtils.CRLF);
		sb.append("        </s:include>" + StringUtils.CRLF);
		sb.append("    </s:if>" + StringUtils.CRLF);
		sb.append("    <e:form pageType=\"view\" value=\"${entity}\" hasZoning=\"true\"/>" + StringUtils.CRLF);
		if ("paper".equals(template) || "honor".equals(template)) {
        	sb.append("    <div eprole=\"fieldset\" legend=\"作者信息\">" + StringUtils.CRLF);
        	sb.append("        <a class=\"a-legend\">作者信息</a>" + StringUtils.CRLF);
        	sb.append("        <e:grid beanId=\"" + beanId + "Author\" value=\"${entity.authors}\" pagination=\"\" cls=\"editableGridDiv\"></e:grid>" + StringUtils.CRLF);
        	sb.append("    </div>" + StringUtils.CRLF);
        	sb.append("    <!-- 模块考核得分 -->" + StringUtils.CRLF);
        	sb.append("    <s:include value=\"../entityAssessScore/entityAssessScore.jsp\">" + StringUtils.CRLF);
        	sb.append("        <s:param name=\"entityId\">${entity.id}</s:param>" + StringUtils.CRLF);
        	sb.append("    </s:include>" + StringUtils.CRLF);
        } 
		sb.append("    <e:foot layerFooter=\"true\">" + StringUtils.CRLF);
		sb.append("        <div class=\"ep-footer\">" + StringUtils.CRLF);
		sb.append("            <c:if test=\"${!e:userInfo().servModel}\">" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"编辑\" key=\"edit\" test=\"${e:isCanOperation(e:userInfo().groupId, entity)}\" onclick=\"ep.loadPage('layer', '${actionPath}!to_edit.action?${e:csrfparam()}&entity.id=${entity.id}','" + moduleZHName + "编辑', this)\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
		sb.append("            </c:if>" + StringUtils.CRLF);
		sb.append("            <c:if test=\"${e:userInfo().servModel}\">" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"编辑\" key=\"edit\" test=\"${e:isCanOperation(e:userInfo().groupId, entity) && flag!='personalCenter'}\" onclick=\"window.location.href='${actionPath}!to_edit.action?${e:csrfparam()}&entity.id=${entity.id}'\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
		sb.append("            </c:if>" + StringUtils.CRLF);
		sb.append("            <e:btn label=\"关闭\" onclick=\"${e:userInfo().servModel ? 'ep.closeTab()' : 'ep.closeLayer()'}\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
		sb.append("        </div>" + StringUtils.CRLF);
		sb.append("    </e:foot>" + StringUtils.CRLF);
		sb.append("</body>" + StringUtils.CRLF);
		sb.append("</html>");
		return sb.toString();
	}

	private static String produceListJspFile(String beanId, String template, String moduleZHName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html;charset=utf-8\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\"%>" + StringUtils.CRLF);
		sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>" + StringUtils.CRLF);
		sb.append(StringUtils.CRLF);
		sb.append("<!DOCTYPE html>" + StringUtils.CRLF);
		sb.append("<html lang=\"zh-cn\">" + StringUtils.CRLF);
		sb.append("<head>" + StringUtils.CRLF);
		sb.append("<e:import resource=\"datepicker,notification,ep,tree,float\" />" + StringUtils.CRLF);
		if ("paper".equals(template) || "honor".equals(template)) {
        	sb.append("<script src=\"${contextPath}/business/js/pub.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
        	sb.append("</head>" + StringUtils.CRLF);
    		sb.append("<body style=\"background-color:#eee;\">" + StringUtils.CRLF);
        } else if ("meeting".equals(template)) {
        	sb.append("</head>" + StringUtils.CRLF);
    		sb.append("<body style=\"background-color:#eee;padding:10px;padding-top:0;\">" + StringUtils.CRLF);
        }
		sb.append("<form action=\"${actionPath}!do_query.action\" method=\"post\">" + StringUtils.CRLF);
		sb.append("    <e:grid beanId=\"" + beanId + "\" value=\"${request.resultList}\" collapsable=\"false\" >" + StringUtils.CRLF);
		sb.append("        <e:head title=\"" + moduleZHName + "列表\">" + StringUtils.CRLF);
		sb.append("            <e:buttonGroup>" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"新增\" key=\"add\" href=\"${actionPath}!to_add.action\" pop=\"true\" popConfig=\"{'width':'80%','height':'95%','title':'" + moduleZHName + "新增'}\"/>" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"删除\" key=\"delete\" onclick=\"ep.deleteAll(this, '${actionPath}!do_deleteMultiEntity.action');\"/>" + StringUtils.CRLF);
		sb.append("                <e:exportTemplete beanId=\"" + beanId + "\" key=\"export\"></e:exportTemplete>" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"导入\" key=\"import\"  href=\"importExcelAction!to_import.action?beanId=" + beanId + "\" pop=\"true\" popConfig=\"{'width':'1180','height':'600','title':'数据导入'}\"></e:btn>" + StringUtils.CRLF);
		sb.append("                <e:batchCheck label=\"批量审核\" key=\"check\" bean=\"" + beanId + "\" popConfig=\"{'width':'80%','height':'95%','title':'" + moduleZHName + "审核'}\"/>" + StringUtils.CRLF);
		sb.append("            </e:buttonGroup>" + StringUtils.CRLF);
		sb.append("            <e:buttonGroup>" + StringUtils.CRLF);
		sb.append("                <e:btn label=\"统计分析\" key=\"statistic\" icon=\"imgStatistics\" onclick=\"ep.statistic('${actionPath}!to_statisticList.action',this,'" + moduleZHName + "统计')\"/>" + StringUtils.CRLF);
		sb.append("            </e:buttonGroup>" + StringUtils.CRLF);
		sb.append("        </e:head>" + StringUtils.CRLF);
		sb.append("        <e:quickSearch/>" + StringUtils.CRLF);
		sb.append("        <e:toolbar value=\"${entity}\"/>" + StringUtils.CRLF);
		sb.append("        <e:pop popTarget=\"checkStatus\" popWorkFlow=\"true\"/>" + StringUtils.CRLF);
		sb.append("        <e:operate key=\"edit\" href=\"${actionPath}!to_edit.action?entity.id=^{id}\" label=\"编辑\" pop=\"true\" bindCheck=\"true\" popConfig=\"{'width':'80%','height':'95%','title':'" + moduleZHName + "编辑'}\"/>" + StringUtils.CRLF);
		if ("paper".equals(template) || "honor".equals(template)) {
			sb.append("        <e:link multiLink=\"true\" href=\"${contextPath}/business/personAction!to_personView.action?entity.id=^{allAuthorId}\" field=\"allAuthor\" pop=\"true\" popConfig=\"{'width':'1180','height':'600','title':'人员查看'}\"/>" + StringUtils.CRLF);
        }
		sb.append("        <e:link href=\"${actionPath}!to_view.action?entity.id=^{id}\" field=\"name\" pop=\"true\" popConfig=\"{'width':'80%','height':'95%','title':'" + moduleZHName + "查看'}\"/>" + StringUtils.CRLF);
		sb.append("        <e:choice bindCheck=\"true\"/>" + StringUtils.CRLF);
		sb.append("        <e:check label=\"审核\" size=\"2\" bean=\"" + beanId + "\" key=\"check\" popConfig=\"{'width':'80%','height':'95%','title':'" + moduleZHName + "审核'}\"/>" + StringUtils.CRLF);
		sb.append("        <e:delete label=\"删除\" bindCheck=\"true\" key=\"delete\" />" + StringUtils.CRLF);
		sb.append("    </e:grid>" + StringUtils.CRLF);
		sb.append("</form>" + StringUtils.CRLF);
		sb.append("</body>" + StringUtils.CRLF);
		sb.append("</html>");
		return sb.toString();
	}

	private static String produceCheckJspFile(String template, String beanId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html;charset=utf-8\"%>" + StringUtils.CRLF);
        sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\"%>" + StringUtils.CRLF);
        sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\"%>" + StringUtils.CRLF);
        sb.append(StringUtils.CRLF);
        sb.append("<!DOCTYPE html>" + StringUtils.CRLF);
        sb.append("<html lang=\"zh-cn\">" + StringUtils.CRLF);
        sb.append("<head>" + StringUtils.CRLF);
        sb.append("<e:import resource=\"notification,float,ep,validator\"/>" + StringUtils.CRLF);
        sb.append("</head>" + StringUtils.CRLF);
        sb.append("<body class=\"ep-nopadding-body popOperateCheck\">" + StringUtils.CRLF);
        sb.append("<form name=\"checkForm\" action=\"${actionPath}!do_checkResultSave.action\" method=\"post\">" + StringUtils.CRLF);
        sb.append("    <input type=\"hidden\" value=\"\" name=\"check.checkStatus\" />" + StringUtils.CRLF);
        sb.append("    <!-- 基本信息 -->" + StringUtils.CRLF);
        sb.append("    <e:form value=\"${entity}\" pageType=\"view\" beanId=\"${beanId}\" maximizable=\"false\" hasZoning=\"true\"/>" + StringUtils.CRLF);
        if ("paper".equals(template)) {
        	sb.append("    <div eprole=\"fieldset\" legend=\"作者信息\">" + StringUtils.CRLF);
        	sb.append("        <a class=\"a-legend\">作者信息</a>" + StringUtils.CRLF);
        	sb.append("        <e:editableGrid beanId=\"" + beanId + "Author\" cls=\"editableGridDiv\" value=\"${entity.authors}\" needAdd=\"false\"  propertyName=\"entity.authors\" readonlyFields=\"orderId\" orderColumn=\"orderId\" initRow=\"3\" pageType=\"view\"/>" + StringUtils.CRLF);
        	sb.append("    </div>" + StringUtils.CRLF);
        	sb.append("    <s:include value=\"../product/supportProject.jsp\"/>" + StringUtils.CRLF);
        } else if ("honor".equals(template)) {
        	sb.append("    <div eprole=\"fieldset\" legend=\"完成人信息\">" + StringUtils.CRLF);
        	sb.append("        <a class=\"a-legend\">完成人信息</a>" + StringUtils.CRLF);
        	sb.append("        <e:editableGrid beanId=\"" + beanId + "Author\" cls=\"editableGridDiv\" value=\"${entity.authors}\" needAdd=\"false\"  propertyName=\"entity.authors\" readonlyFields=\"orderId\" orderColumn=\"orderId\" initRow=\"3\" pageType=\"view\"/>" + StringUtils.CRLF);
        	sb.append("    </div>" + StringUtils.CRLF);
        } else if ("meeting".equals(template)) {
        }
        
        sb.append("    <s:include value=\"../pub/pubCheckFooter.jsp\"/>" + StringUtils.CRLF);
        sb.append("</form>" + StringUtils.CRLF);
        sb.append("</body>" + StringUtils.CRLF);
        sb.append("</html>");
		return sb.toString();
	}

	private static String produceAddJspFile(String template, String beanId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page contentType=\"text/html; charset=UTF-8\"%>" + StringUtils.CRLF);
        sb.append("<%@ taglib prefix=\"e\" uri=\"/firefly-theme-taglib\"%>" + StringUtils.CRLF);
        sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\"%>" + StringUtils.CRLF);
        sb.append(StringUtils.CRLF);
        sb.append("<!DOCTYPE html>" + StringUtils.CRLF);
        sb.append("<html lang=\"zh-cn\">" + StringUtils.CRLF);
        sb.append("<head>" + StringUtils.CRLF);
        
        if ("paper".equals(template) || "honor".equals(template)) {
        	sb.append("<e:import resource=\"datepicker,validator,float,tableorder,ep,layer,select\" />" + StringUtils.CRLF);
        	sb.append("<script src=\"${contextPath}/business/js/pub.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
        	sb.append("<script src=\"${contextPath}/business/js/productAuthor.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
        	sb.append("<script src=\"${contextPath}/business/js/product.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
        	sb.append("" + StringUtils.CRLF);
        } else if ("meeting".equals(template)) {
        	sb.append("<e:import resource=\"datepicker,validator,float,ep,select\"/>" + StringUtils.CRLF);
        	sb.append("<script src=\"${contextPath}/business/js/product.js\" charset=\"UTF-8\"></script>" + StringUtils.CRLF);
        }
        
        sb.append("<script type=\"text/javascript\">" + StringUtils.CRLF);
        if ("paper".equals(template) || "honor".equals(template)) {
        	sb.append("var csrfparam = \"${e:csrfparam()}\";" + StringUtils.CRLF);
        	sb.append("var isTeacher = ${e:userInfo().teacher};" + StringUtils.CRLF);
        	sb.append("var isUnit = ${e:userInfo().unitAdmin};" + StringUtils.CRLF);
        	sb.append("var curPersonId = '${e:userInfo().personId}';" + StringUtils.CRLF);
        	sb.append("$(function() {" + StringUtils.CRLF);
        	sb.append("    checkBusinessFilter(isTeacher);" + StringUtils.CRLF);
        	sb.append("    if (!isTeacher) {" + StringUtils.CRLF);
        	sb.append("        checkBusinessFilter(isUnit);" + StringUtils.CRLF);
        	sb.append("    }" + StringUtils.CRLF);
        	sb.append("    initPersonAutoComplete();" + StringUtils.CRLF);
        	sb.append("});" + StringUtils.CRLF);
        	sb.append("function checkMember() {" + StringUtils.CRLF);
        	sb.append("    var totalRatio = 0;" + StringUtils.CRLF);
        	sb.append("    $(\"input[name^='entity.authors['][name$='workRatio']\").each(function() {" + StringUtils.CRLF);
        	sb.append("        if (this.value) {" + StringUtils.CRLF);
        	sb.append("            totalRatio += parseFloat(this.value);" + StringUtils.CRLF);
        	sb.append("        }" + StringUtils.CRLF);
        	sb.append("    });" + StringUtils.CRLF);
        	sb.append("    if (totalRatio > 100) {" + StringUtils.CRLF);
        	sb.append("        ep.alert(\"贡献率不能大于100！\");" + StringUtils.CRLF);
        	sb.append("        return false;" + StringUtils.CRLF);
        	sb.append("    }" + StringUtils.CRLF);
        	sb.append("    deleteRowNum = 0;" + StringUtils.CRLF);
        	sb.append("    " + StringUtils.CRLF);
        	sb.append("    if (!checkProductAuthor(isTeacher, curPersonId, '" + beanId + "Author', '自己必须在作者信息中且为校内第一作者，请修改！')) {" + StringUtils.CRLF);
        	sb.append("        return false;" + StringUtils.CRLF);
        	sb.append("    }" + StringUtils.CRLF);
        	sb.append("    if (!checkMemberNull()) {" + StringUtils.CRLF);
        	sb.append("        return false;" + StringUtils.CRLF);
        	sb.append("    }" + StringUtils.CRLF);
        	sb.append("    if (!checkWordNumber()) {" + StringUtils.CRLF);
        	sb.append("        return false;" + StringUtils.CRLF);
        	sb.append("    }" + StringUtils.CRLF);
        	sb.append("    //这段代码要放在表单验证的最后，否则可能会造成科研秘书可以选择所属单位" + StringUtils.CRLF);
        	sb.append("    setUnitValid($(\"select[name='entity.unitId']\"));" + StringUtils.CRLF);
        	sb.append("    return true;" + StringUtils.CRLF);
        	sb.append("}" + StringUtils.CRLF);

        } else if ("meeting".equals(template)) {
        	sb.append("$(function() {" + StringUtils.CRLF);
        	sb.append("    if (${e:userInfo().unitAdmin}) {" + StringUtils.CRLF);
        	sb.append("        setUnitDisabled($(\"select[name='entity.unitId']\"));" + StringUtils.CRLF);
        	sb.append("    }" + StringUtils.CRLF);
        	sb.append("});" + StringUtils.CRLF);
        	sb.append("function checkForm() {" + StringUtils.CRLF);
        	sb.append("    //这段代码要放在表单验证的最后，否则可能会造成科研秘书可以选择所属单位" + StringUtils.CRLF);
        	sb.append("    setUnitValid($(\"select[name='entity.unitId']\"));" + StringUtils.CRLF);
        	sb.append("    return true;" + StringUtils.CRLF);
        	sb.append("}" + StringUtils.CRLF);
        }
        sb.append("</script>" + StringUtils.CRLF);
        sb.append("</head>" + StringUtils.CRLF);
        sb.append("<body class=\"ep-nopadding-body popOperate\">" + StringUtils.CRLF);
        sb.append("<form action=\"${actionPath}!do_save.action\" enctype=\"multipart/form-data\" method=\"post\">" + StringUtils.CRLF);
        if ("paper".equals(template)) {
        	sb.append("    <s:include value=\"" + beanId + "Top.jsp\">" + StringUtils.CRLF);
        	sb.append("        <s:param name=\"module\">baseInfo</s:param>" + StringUtils.CRLF);
        	sb.append("    </s:include>" + StringUtils.CRLF);
        } else if ("meeting".equals(template) || "honor".equals(template)) {
        	
        }
        sb.append("    <e:box id=\"danger\" title=\"验证失败!\" innerHTML=\"请检查填写数据的正确性!\" visible=\"false\"/>" + StringUtils.CRLF);
        sb.append("    <e:form pageType=\"add\" value=\"${entity}\" hasZoning=\"true\"/>" + StringUtils.CRLF);
        if ("paper".equals(template) || "honor".equals(template)) {
        	sb.append("    <div eprole=\"fieldset\" legend=\"作者信息\">" + StringUtils.CRLF);
        	sb.append("        <a class=\"a-legend\">作者信息" + StringUtils.CRLF);
        	sb.append("        <button type=\"button\" id=\"editableGridTag_" + beanId + "Author_add\" class=\"btn btn-default btn-sm floatRight\" onclick=\"ep.addRow(this, 'editableGridTag_" + beanId + "Author', null);addPersonAutoComplete('editableGridTag_" + beanId + "Author');\">添加</button>" + StringUtils.CRLF);
        	sb.append("        </a>" + StringUtils.CRLF);
        	sb.append("        <e:editableGrid beanId=\"" + beanId + "Author\" cls=\"editableGridDiv\" value=\"${entity.authors}\" needAdd=\"false\"  propertyName=\"entity.authors\" orderColumn=\"orderId\">" + StringUtils.CRLF);
        	sb.append("            <e:delete type=\"a\" label=\"删除\"/>" + StringUtils.CRLF);
        	sb.append("        </e:editableGrid>" + StringUtils.CRLF);
        	sb.append("    </div>" + StringUtils.CRLF);
        } else if ("meeting".equals(template)) {
        	
        }
        sb.append("    <e:foot layerFooter=\"true\">" + StringUtils.CRLF);
        sb.append("            <div class=\"ep-footer\">" + StringUtils.CRLF);
        if ("paper".equals(template)) {
        	sb.append("                <e:save changePageSubmit=\"${e:userInfo().servModel}\" icon4Save=\"\" icon4Submit=\"\" cls4Submit=\"btn btn-primary btn-sm\" cls4Save=\"btn btn-primary btn-sm\" visible=\"${pageType=='EDIT'}\" onSubmit=\"checkMember\"/>" + StringUtils.CRLF);
        	sb.append("                <e:btn label=\"下一步\" onclick=\"ep.saveForm(this,'',true,true,checkMember,null,true,'');\" cls=\"btn btn-primary btn-sm\" test=\"${pageType=='ADD'}\"/>" + StringUtils.CRLF);
        	sb.append("                <e:btn label=\"关闭\" onclick=\"closeThis(${e:userInfo().servModel},${pageType == 'ADD'},true,null)\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
        } else if ("honor".equals(template)) {
        	sb.append("                <e:save changePageSubmit=\"${e:userInfo().servModel}\" icon4Save=\"\" icon4Submit=\"\" cls4Submit=\"btn btn-primary btn-sm\" cls4Save=\"btn btn-primary btn-sm\" onSubmit=\"checkMember\"/>" + StringUtils.CRLF);
        	sb.append("                <e:btn label=\"关闭\" onclick=\"closeThis(${e:userInfo().servModel},${pageType == 'ADD'},true,null)\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
        } else if ("meeting".equals(template)) {
        	sb.append("                <e:save icon4Save=\"\" icon4Submit=\"\" cls4Submit=\"btn btn-primary btn-sm\" cls4Save=\"btn btn-primary btn-sm\" onSubmit=\"checkForm\"/>" + StringUtils.CRLF);
        	sb.append("                <e:btn label=\"关闭\" onclick=\"ep.closeLayer();\" cls=\"btn btn-primary btn-sm\"/>" + StringUtils.CRLF);
        }
        sb.append("            </div>" + StringUtils.CRLF);
        sb.append("    </e:foot>" + StringUtils.CRLF);
        sb.append("</form>" + StringUtils.CRLF);
        sb.append("</body>" + StringUtils.CRLF);
        sb.append("</html>");
		return sb.toString();
	}
}
