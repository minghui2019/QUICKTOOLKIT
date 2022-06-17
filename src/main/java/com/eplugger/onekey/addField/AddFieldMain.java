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
		TextTrans.createFieldXml("归档材料附件；财务结算表附件",
				Lists.newArrayList(new ModuleTable("xJProjectMiddleCheck"), new ModuleTable("zXProjectMiddleCheck"),
						new ModuleTable("kFProjectMiddleCheck"), new ModuleTable("yfContractMiddleCheck"),
						new ModuleTable("yfContractComplete"), new ModuleTable("kFProjectComplete"),
						new ModuleTable("xJProjectComplete"), new ModuleTable("zXProjectComplete")
//				,new ModuleTable("zXProject")
				));
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = TransService.transText2En("全职、兼职、实习生");
		System.out.println(dst);
	}
	
	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "JOB_TYPE"; //常量名
		String bizName = "岗位类型"; //业务名称
		String bizType = AddCategoryEntry.BIZ_TYPE[0]; //业务类型
//		String version = "V8.5.3"; "V8.5.2"; "V8.5.0"; "V8.3.0"; "V3.1.0";//eadp版本
		String[] keyArray = {"FullTime, , ", "PartTime", "Intern"};//字典代码
		String[] valueArray = "全职、兼职、实习生".split("、");
//		String[] valueArray = {"技术开发", "技术服务", };//字典值
		AddCategoryEntry.createCategorySqlFile(categoryName, bizName, bizType, "V8.5.2", keyArray, valueArray);
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
