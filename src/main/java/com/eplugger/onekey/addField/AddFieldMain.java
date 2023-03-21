package com.eplugger.onekey.addField;

import com.eplugger.enums.BizType;
import com.eplugger.onekey.addCategoryEntry.AddCategoryEntry;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.onekey.utils.entityMeta.EntityMetaField;
import com.eplugger.trans.TextTrans;
import com.eplugger.trans.service.TransService;
import com.eplugger.utils.DBUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class AddFieldMain {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.广州番禺职业技术学院;
	}

	@Test
	public void testHasModuleTable() throws Exception {
		TextTrans.hasModuleTables(new ModuleTable("expertReview"));
	}

	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("是否同意申请",
			new ModuleTable("expertReview")
		);
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = TransService.transText2En("同意");
		System.out.println(dst);
	}
	
	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "REVIEW_RESULT_BOOK"; //常量名
		String bizName = "评审结果(著作)"; //业务名称
		String bizType = BizType.评审.name(); //业务类型
		String[] keyArray = "1,2,0".split(",");//字典代码
		String[] valueArray = "达到要求、基本达到要求、未达到要求".split("、");
//		String[] valueArray = {"技术开发", "技术服务", };//字典值
		AddCategoryEntry.createCategorySqlFile(categoryName, bizName, bizType, DBUtils.schoolInfo.version, keyArray, valueArray);
	}

	@Test
	public void testUpdateSceneEntityMetas() {
		AddFieldFun.createUpdateSceneEntityMetas(
			new EntityMetaField("payoutFee", "projectPayoutDetail", AddFieldFun.ITEM_TYPE_MONEY),
			new EntityMetaField("debitAmount", "projectPayoutDetail", AddFieldFun.ITEM_TYPE_MONEY),
			new EntityMetaField("creditAmount", "projectPayoutDetail", AddFieldFun.ITEM_TYPE_MONEY)
		);
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
