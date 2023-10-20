package com.eplugger.onekey.addField;

import com.eplugger.enums.BizType;
import com.eplugger.onekey.addCategoryEntry.AddCategoryEntry;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.onekey.utils.entityMeta.EntityMetaField;
import com.eplugger.trans.TextTrans;
import com.eplugger.utils.DBUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class AddFieldMain {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.广东工业大学;
	}

	@Test
	public void testHasModuleTable() throws Exception {
		TextTrans.hasModuleTables(new ModuleTable("ptProject"));
	}

	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("是否为译文；版面",
			new ModuleTable("paper", "BIZ_PT_PROJECT", "平台团队项目"));
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = com.baidu.translate.service.TransService.transTextZh2En("出版社类别");
		System.out.println(dst);
	}
	
	@Test
	public void testCreateCategorySqlFile() throws Exception {
		String categoryName = "PLATFORM_LEVEL"; //常量名
		String bizName = "平台层次"; //业务名称
		String bizType = BizType.项目.name(); //业务类型
		String[] keyArray = "1,2,3,4,5,6".split(",");//字典代码
		String[] valueArray = "1、2、3、4、5、6".split("、");
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
		String fileName = "bookApplyBookTab_的场景SQL";

		try {
			int lastPointIndex = fileName.lastIndexOf(".");
			lastPointIndex = lastPointIndex == -1 ? fileName.length() : lastPointIndex;
			StringBuilder sb = new StringBuilder();
			sb.append(fileName.substring(0, 10))
				.append("...")
				.append(fileName.substring(lastPointIndex - 10, lastPointIndex))
				.append(fileName.substring(lastPointIndex));
			System.out.println(sb);
		} catch (Exception e) {
		}

		int lastIndex = fileName.lastIndexOf(".");
		String ext = StringUtils.isBlank(fileName) || lastIndex == -1 ? "" : fileName.substring(lastIndex);
		System.out.println(ext);

	}
}
