package com.eplugger.onekey.addCategoryEntry;

import java.io.IOException;

import com.eplugger.onekey.addCategoryEntry.utils.ProduceCategorySqlCode;
import com.eplugger.util.FileUtil;

/**
 * 新增字典
 * @author Admin
 */
public class AddCategoryEntry {
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
		String content = ProduceCategorySqlCode.createCategoryStr(keyArray, valueArray, categoryName, bizName, bizType, version);
		String fileName = bizName + "字典配置SQL.sql";
		FileUtil.outFile(content, "C:\\Users\\Admin\\Desktop", fileName, false);
		try {
			Runtime.getRuntime().exec("notepad.exe  C:\\Users\\Admin\\Desktop\\" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
