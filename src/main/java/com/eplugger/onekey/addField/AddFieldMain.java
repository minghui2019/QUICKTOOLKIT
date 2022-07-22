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
		TextTrans.createFieldXml("科研总分",
				Lists.newArrayList(
						new ModuleTable("patentProposal","BIZ_PATENT_PROPOSAL","专利提案")
						,new ModuleTable("molectronLayout","BIZ_MOLECTRON_LAYOUT","电路布图")
						,new ModuleTable("honorApplyBook","BIZ_HONOR_APPLY_BOOK","获奖申请材料")
						,new ModuleTable("paper")
						,new ModuleTable("book")
						,new ModuleTable("researchReport")
						,new ModuleTable("jdProduct")
						,new ModuleTable("artProduct")
						,new ModuleTable("patent")
						,new ModuleTable("copyRight")
						,new ModuleTable("standard")
						,new ModuleTable("medicine")
						,new ModuleTable("breed")
						,new ModuleTable("honor")
						
				));
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = TransService.transText2En("初级；中级；高级");
		System.out.println(dst);
	}
	
	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "HONOR_TITLE"; //常量名
		String bizName = "荣誉称号"; //业务名称
		String bizType = AddCategoryEntry.BIZ_TYPE[0]; //业务类型
//		String version = "V8.5.3"; "V8.5.2"; "V8.5.0"; "V8.3.0"; "V3.1.0";//eadp版本
		String[] keyArray = "1,2,3,4,5,6,7,8,9,10,11,12,13,14".split(",");//字典代码
		String[] valueArray = "院士、百千万人才工程国家级人选、突出青年、长江学者、国家“千人计划”人才、国家“万人计划”人才、省“万人计划”人才、长江学者国家级教学名师、省级教学名师、国务院特殊津贴专家、全国技术能手、珠江学者（同级）、其他".split("、");
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
