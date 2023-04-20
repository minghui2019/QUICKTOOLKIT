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
		DBUtils.schoolInfo = SchoolInfo.广州番禺职业技术学院;
	}

	@Test
	public void testHasModuleTable() throws Exception {
		TextTrans.hasModuleTables(new ModuleTable("notify"));
	}

	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("建议推迟人数",
			new ModuleTable("changeLog", "SYS_CHANGE_LOG", "系统更新日志")
		);
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
	
	@Test
	public void testTransText2En() throws Exception {
		String dst = com.baidu.translate.service.TransService.transTextZh2En("经费收支");
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
