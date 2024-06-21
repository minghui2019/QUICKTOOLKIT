package com.eplugger.onekey.addField;

import com.eplugger.enums.BizType;
import com.eplugger.onekey.addCategoryEntry.AddCategoryEntry;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.onekey.utils.entityMeta.EntityMetaField;
import com.eplugger.trans.TextTrans;
import com.eplugger.utils.DBUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class AddFieldTest {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.梧州职业学院;
	}

	@Test
	public void testHasModuleTable() throws Exception {
		TextTrans.hasModuleTables(new ModuleTable("ptProject"));
	}

	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("参与单位；颁发机构；获奖组；赛道；参赛组别；参赛类别；团队名称；班级",
								 "honor");
	}

	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}

	@Test
	public void testTransText2En() {
		String dst = com.baidu.translate.service.TransService.transTextZh2En("项目父类编号");
		System.out.println(dst);
	}

	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "PRODUCT_MODE"; //常量名
		String bizName = "成果形式"; //业务名称
		String bizType = BizType.项目.name(); //业务类型
		String[] keyArray = "1,2,3,4,5,6,7,8,9".split(",");//字典代码
		String[] valueArray = "专著、编著或教材、工具书或参考书、皮书/发展报告、科普读物、古籍整理著作、译著、译文、电子出版物、论文、研究报告、咨询报告、专利、著作权、新品种、新工艺、标准、集成电路布图、转化成果、其他".split("、");
		//		String[] valueArray = {"技术开发", "技术服务", };//字典值
		AddCategoryEntry.createCategorySqlFile(categoryName, bizName, bizType, DBUtils.schoolInfo.version, null, valueArray);
	}

	@Test
	public void testUpdateSceneEntityMetas() {
		AddFieldFun.createUpdateSceneEntityMetas(
			new EntityMetaField("payoutFee", "projectPayoutDetail", AddFieldFun.ITEM_TYPE_MONEY),
			new EntityMetaField("debitAmount", "projectPayoutDetail", AddFieldFun.ITEM_TYPE_MONEY),
			new EntityMetaField("creditAmount", "projectPayoutDetail", AddFieldFun.ITEM_TYPE_MONEY)
		);
	}
}
