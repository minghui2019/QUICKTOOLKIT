package com.eplugger.onekey.viewFile;

import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.utils.DBUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class AutoViewFile {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.广州番禺职业技术学院;
	}
	
	/**
	 * 1、根据表名生成Excel;tableList:需要生成视图的有序map集合
	 */
	@Test
	public void generationTableExcel() {
		AutoViewFileFun.generationTableExcel();
	}
	
	/**
	 * 2、自动生成视图语句
	 */
	@Test
	public void generationViewSql() {
		AutoViewFileFun.generationViewSql();
	}
}
