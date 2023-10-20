package com.eplugger.onekey.addCategoryEntry;

import java.io.File;

import top.tobak.common.io.FileUtils;
import com.eplugger.onekey.addCategoryEntry.utils.ProduceCategorySqlCodeFactory;

/**
 * 新增字典
 * @author minghui
 */
public class AddCategoryEntry {
	public static void createCategorySqlFile(String categoryName, String bizName, String bizType, String version, String[] keyArray, String[] valueArray) {
		String content = ProduceCategorySqlCodeFactory.getInstance().createCategoryStr(keyArray, valueArray, categoryName, bizName, bizType, version);
		String fileName = bizName + "字典配置SQL.sql";
		FileUtils.write(FileUtils.getUserHomeDirectory() + "Category\\" + fileName, content);
		FileUtils.openTaskBar(new File(FileUtils.getUserHomeDirectory() + "Category\\"));
	}
}
