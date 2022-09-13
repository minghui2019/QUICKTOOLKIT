package com.eplugger.onekey.addCategoryEntry;

import java.io.File;

import com.eplugger.common.io.FileUtils;
import com.eplugger.onekey.addCategoryEntry.utils.ProduceCategorySqlCodeFactory;

/**
 * 新增字典
 * @author minghui
 */
public class AddCategoryEntry {
	/** "人员", "项目", "成果" */
	public static final String[] BIZ_TYPE = {"人员", "项目", "成果", "系统", "平台", "系统图表", "学术交流", "评审", "经费", "合同", "考核", "系统参数", "个人空间", "财务同步", "编号生成"};
	public static void main(String[] args) {
		String categoryName = "UNIT_TYPE_YF"; //常量名
		String bizName = "合作方类型"; //业务名称
		String bizType = "项目"; //业务类型
		String version = "V8.5.2";//eadp版本
//		String version = "V8.5.0";//eadp版本
//		String version = "V8.3.0";//eadp版本
//		String version = "V3.1.0";//eadp版本
		String[] keyArray = {"1", "2", "3"};//字典代码
		String[] valueArray = "企业、事业单位、政府单位".split("、");
//		String[] valueArray = {"技术开发", "技术服务", };//字典值
		createCategorySqlFile(categoryName, bizName, bizType, version, keyArray, valueArray);
	}

	public static void createCategorySqlFile(String categoryName, String bizName, String bizType, String version, String[] keyArray, String[] valueArray) {
		String content = ProduceCategorySqlCodeFactory.getInstance().createCategoryStr(keyArray, valueArray, categoryName, bizName, bizType, version);
		String fileName = bizName + "字典配置SQL.sql";
		FileUtils.write(FileUtils.getUserHomeDirectory() + "Category\\" + fileName, content);
		FileUtils.openTaskBar(new File(FileUtils.getUserHomeDirectory() + "Category\\"));
	}
}
