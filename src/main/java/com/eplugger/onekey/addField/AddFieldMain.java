package com.eplugger.onekey.addField;

import org.junit.Test;

import com.eplugger.onekey.addCategoryEntry.AddCategoryEntry;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.trans.TextTrans;
import com.eplugger.trans.service.TransService;
import com.google.common.collect.Lists;

public class AddFieldMain {
	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("排序号",
				Lists.newArrayList(new ModuleTable("servPoint", "SYS_SERV_POINT", "服务点")));
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = TransService.transText2En("知识产权类型");
		System.out.println(dst);
	}
	
	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "PROJECT_PROPERTIES"; //常量名
		String bizName = "项目属性"; //业务名称
		String bizType = AddCategoryEntry.BIZ_TYPE[1]; //业务类型
//		String version = "V8.5.3"; "V8.5.2"; "V8.5.0"; "V8.3.0"; "V3.1.0";//eadp版本
		String[] keyArray = "1,2,3,4,5,6,7,8".split(",");//字典代码
		String[] valueArray = "独立纵向项目、主持的纵向合作项目、参与的纵向合作项目、学会项目、科研平台、子课题、开放课题、其他".split("、");
//		String[] valueArray = {"技术开发", "技术服务", };//字典值
		AddCategoryEntry.createCategorySqlFile(categoryName, bizName, bizType, "V8.5.3", keyArray, valueArray);
	}
	
	@Test
	public void testName() throws Exception {
		double d1 = 11.36;
		System.out.println(Long.toBinaryString(Double.doubleToLongBits(d1)));
		for (int i = 1; i < 10; i++) {
			System.out.println(Long.toBinaryString(Float.floatToIntBits(i * 1f)));
		}
		System.out.println(Long.toBinaryString(Float.floatToIntBits(4.5f)));
	}
}
