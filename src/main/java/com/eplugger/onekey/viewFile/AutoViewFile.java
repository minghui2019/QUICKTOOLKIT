package com.eplugger.onekey.viewFile;

import java.io.IOException;

import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.onekey.entity.Categories;
import com.eplugger.onekey.viewFile.entity.ModuleViews;
import com.eplugger.utils.DBUtils;
import top.tobak.xml.dom4j.utils.parsers.CategoryParser;
import top.tobak.xml.dom4j.utils.parsers.ModuleViewParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

@Slf4j
public class AutoViewFile {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.惠州学院;
		XmlParseUtils.registerBean(new ModuleViewParser(), ModuleViews.class);
		XmlParseUtils.registerBean(new CategoryParser(), Categories.class);
	}
	
	/**
	 * 1、根据表名生成Excel;tableList:需要生成视图的有序map集合
	 */
	@Test
	public void generationTableExcel() throws Exception {
		AutoViewFileFun.generationTableExcel1();
	}
	
	/**
	 * 2、自动生成视图语句
	 */
	@Test
	public void generationViewSql() {
		AutoViewFileFun.generationViewSql1();
	}

	@Test
	public void testMergeFiles() throws IOException {
		AutoViewFileFun.mergeFiles();
	}

}
