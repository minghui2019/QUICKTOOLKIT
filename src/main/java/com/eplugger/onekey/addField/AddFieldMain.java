package com.eplugger.onekey.addField;

import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.utils.DBUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import com.eplugger.onekey.addCategoryEntry.AddCategoryEntry;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.trans.TextTrans;
import com.eplugger.trans.service.TransService;
import com.google.common.collect.Lists;

@Slf4j
public class AddFieldMain {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.广州番禺职业技术学院;
	}

	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("项目资金来源、项目名称、项目id、项目负责人、项目负责人id、项目分类、财务编号、" +
						"与所学专业关联度、与岗位关联度、与研发项目关联度、是否职务专利、发明专利是否提前公开、申请人地址和邮编、第一发明人工作从事内容",
				Lists.newArrayList(new ModuleTable("patentProposal")));
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = TransService.transText2En("专利申请");
		System.out.println(dst);
	}
	
	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "ASSOCIATION"; //常量名
		String bizName = "关联度（知识产权申请）"; //业务名称
		String bizType = AddCategoryEntry.BIZ_TYPE[2]; //业务类型
//		String version = "V8.5.3"; "V8.5.2"; "V8.5.0"; "V8.3.0"; "V3.1.0";//eadp版本
		String[] keyArray = "1,2,3,4,5".split(",");//字典代码
		String[] valueArray = "密切相关、比较相关、一般相关、部分相关、不相关".split("、");
//		String[] valueArray = {"技术开发", "技术服务", };//字典值
		AddCategoryEntry.createCategorySqlFile(categoryName, bizName, bizType, DBUtils.schoolInfo.version, keyArray, valueArray);
	}
	
	@Test
	public void testName() throws Exception {
		double d1 = 11.36;
		log.debug(Long.toBinaryString(Double.doubleToLongBits(d1)));
		for (int i = 1; i < 10; i++) {
			log.debug(Long.toBinaryString(Float.floatToIntBits(i * 1f)));
		}
		log.debug(Long.toBinaryString(Float.floatToIntBits(4.5f)));
		System.out.println(111);
	}
}
