package com.eplugger.onekey.addModule;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.tobak.common.io.FileUtils;
import com.eplugger.enums.DataType;
import com.eplugger.onekey.entity.AppendSearch;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Constants2 {
	private Constants2() {}
	
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static Map<String, String> fullClassNameMap = Maps.newHashMap();
	public static String getFullClassNameMap(String key) {
		if (fullClassNameMap.isEmpty()) {
			String jsonStr = FileUtils.readFile4String("src/main/resource/other/fullClassNames.json");
			Type type = new TypeToken<HashMap<Integer, String>>() {}.getType();
			fullClassNameMap = gson.fromJson(jsonStr, type);
		}
		return fullClassNameMap.get(key);
	}
	public static String putFullClassNameMap(String key, String value) {
		return fullClassNameMap.put(key, value);
	}
	
	private static final Map<String, String> ASSOCIATION_MAP = Maps.newHashMap();
	public static final String MANY_TO_ONE = "ManyToOne";
	public static final String ONE_TO_MANY = "OneToMany";
	public static String getAssociationMap(String key) {
		if (ASSOCIATION_MAP.isEmpty()) {
			ASSOCIATION_MAP.put(ONE_TO_MANY, "@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)");
			ASSOCIATION_MAP.put(MANY_TO_ONE, "@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)");
		}
		return ASSOCIATION_MAP.get(key);
	}
	
	private static Map<String, List<Field>> superClassFieldMap = Maps.newHashMap();
	public static List<Field> getSuperClassFieldMap(String key) {
		if (superClassFieldMap.isEmpty()) {
			String jsonStr = FileUtils.readFile4String("src/main/resource/other/superClassFields.json");
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
			Constants2.initSuperClassFieldMap(jsonObject);
		}
		return superClassFieldMap.get(key);
	}

	private static void initSuperClassFieldMap(JsonObject jsonObject) {
		initApplyInfoClassFieldMap(jsonObject);
		initApplyBookClassFieldMap();
		initProductSuperClassFieldMap();
		initProductAuthorSuperClassFieldMap();
		initCheckBusinessEntitySuperClassFieldMap();
		initBizEntitySuperClassFieldMap();
	}

	private static void initApplyBookClassFieldMap() {
		List<Field> applyBookClassFields = Lists.newArrayList();
		applyBookClassFields.add(new Field("applierId", "申请人编号", DataType.STRING.java, 32));
		applyBookClassFields.add(new Field("applierName", "申请人姓名", DataType.STRING.java, 50));
		applyBookClassFields.add(new Field("applierEduDegreeId", "申请人学位", DataType.STRING.java, "EDU_DEGREE", 32));
		applyBookClassFields.add(new Field("applierTitleId", "申请人职称", DataType.STRING.java, "TITLE_SELECT", 32));
		applyBookClassFields.add(new Field("divisionId", "所属教研室", DataType.STRING.java, "DIVISION", 32));
		applyBookClassFields.add(new Field("unitId", "所属单位", DataType.STRING.java, "PERSON_KY_UNIT", 32));
		applyBookClassFields.add(new Field("applyDate", "申请日期", DataType.DATE.java));
		applyBookClassFields.add(new Field("subjectClassId", "学科门类", DataType.STRING.java, "STA_CLASS", 32));
		applyBookClassFields.add(new Field("subjectId", "一级学科", DataType.STRING.java, "SUBJECT1", 32));
		applyBookClassFields.add(new Field("projectSourceId", "项目来源", DataType.STRING.java, "PROJECT_SOURCE", 32));
		applyBookClassFields.add(new Field("productMode", "成果形式", DataType.STRING.java, 500));
		applyBookClassFields.add(new Field("note", "备注", DataType.STRING.java, 2000));
		applyBookClassFields.add(new Field("fileIds", "附件", DataType.STRING.java, 1000));
		applyBookClassFields.add(new Field("reviewResult", "评审结果", DataType.STRING.java, "REVIEW_RESULT", 64));
		applyBookClassFields.add(new Field("reviewInfo", "评审建议", DataType.STRING.java, 1000));
		applyBookClassFields.add(new Field("reviewMark", "评审分值", DataType.DOUBLE.java));
		applyBookClassFields.add(new Field("reviewStatus", "评审状态", DataType.STRING.java, "REVIEW_STATUS", 32));
		applyBookClassFields.add(new Field("expertReviewId", "专家评审id", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("reviewerName", "评审专家姓名", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("subjectExpert", "学科专家", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("expertJsonBySubjectId", "系统学科对应的专家信息", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("reviewExpert", "评审专家", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("schedule", "评审进度", DataType.DOUBLE.java, true));
		applyBookClassFields.add(new Field("scheduleMessage", "评审进度显示信息", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("memberNames", "成员姓名", DataType.STRING.java, true));
		applyBookClassFields.add(new Field("memberPersonIds", "成员personId", DataType.STRING.java, true));
		superClassFieldMap.put("ApplyBook", applyBookClassFields);
	}

	private static void initApplyInfoClassFieldMap(JsonObject jsonObject) {
		JsonArray jsonArray = jsonObject.get(SuperClassName.ApplyInfo.toString()).getAsJsonArray();
		Type type = new TypeToken<List<Field>>() {}.getType();
		List<Field> applyInfoClassFields = gson.fromJson(jsonArray, type);
		
//		applyInfoClassFields.add(new Field("name", "申报计划名称", DataType.STRING.java, 500));
//		applyInfoClassFields.add(new Field("beginDate", "申报开始日期", DataType.DATE.java));
//		applyInfoClassFields.add(new Field("endDate", "申报结束日期", DataType.DATE.java));
//		applyInfoClassFields.add(new Field("applyState", "申报状态", DataType.STRING.java, "PROJECT_APPLY_STATE", 32));
//		applyInfoClassFields.add(new Field("isAutoEnd", "是否自动截止", DataType.STRING.java, "SF", 32));
//		applyInfoClassFields.add(new Field("note", "备注", DataType.STRING.java, 2000));
//		applyInfoClassFields.add(new Field("fileId", "申报指南", DataType.STRING.java, 500));
//		applyInfoClassFields.add(new Field("isReview", "是否可转入评审", DataType.STRING.java, "SF", 32));
//		applyInfoClassFields.add(new Field("hasApplyed", "个人是否申请过该计划", DataType.STRING.java, true));
//		applyInfoClassFields.add(new Field("applyBookState", "个人申请材料的审核状态", DataType.STRING.java, true));
		superClassFieldMap.put("ApplyInfo", applyInfoClassFields);
	}

	private static void initBizEntitySuperClassFieldMap() {
		List<Field> bizEntityClassFields = Lists.newArrayList();
		bizEntityClassFields.add(new Field("createUserID", "创建用户编号", DataType.STRING.java, 100, "CREATEUSERID"));
		bizEntityClassFields.add(new Field("createUserName", "创建用户名", DataType.STRING.java, 100, "CREATEUSERNAME"));
		bizEntityClassFields.add(new Field("createDate", "创建时间", OtherUtils.TPYE_TIMESTAMP, 0, "CREATEDATE"));
		bizEntityClassFields.add(new Field("lastEditUserID", "最后编辑用户编号", DataType.STRING.java, 100, "LASTEDITUSERID"));
		bizEntityClassFields.add(new Field("lastEditUserName", "最后编辑用户名", DataType.STRING.java, 100, "LASTEDITUSERNAME"));
		bizEntityClassFields.add(new Field("lastEditDate", "最后编辑日期", OtherUtils.TPYE_TIMESTAMP, 0, "LASTEDITDATE"));
		superClassFieldMap.put("BizEntity", bizEntityClassFields);
	}

	private static void initCheckBusinessEntitySuperClassFieldMap() {
		List<Field> checkBusinessEntityClassFields = Lists.newArrayList();
		checkBusinessEntityClassFields.add(new Field("checkStatus", "审核状态", DataType.STRING.java, "CHECKSTATUS", "DM_CHECK_CHECKSTATUS", 64, "no"));
		checkBusinessEntityClassFields.add(new Field("checkDate", "审核时间", DataType.STRING.java, 64, "CHECKDATE"));
		checkBusinessEntityClassFields.add(new Field("checker", "审核人", DataType.STRING.java, 80, "CHECKER"));
		superClassFieldMap.put("CheckBusinessEntity", checkBusinessEntityClassFields);
	}

	private static void initProductAuthorSuperClassFieldMap() {
		List<Field> productAuthorClassFields = Lists.newArrayList();
		productAuthorClassFields.add(new Field("authorType", "作者类型", DataType.STRING.java, "AUTHOR_TYPE", 32));
		productAuthorClassFields.add(new Field("personId", "人员Id", DataType.STRING.java, 32));
		productAuthorClassFields.add(new Field("authorAccount", "职工号", DataType.STRING.java, 64));
		productAuthorClassFields.add(new Field("authorName", "作者姓名", DataType.STRING.java, 64));
		productAuthorClassFields.add(new Field("sexId", "性别", DataType.STRING.java, "RY_XM_NN", 32));
		productAuthorClassFields.add(new Field("eduLevelId", "学历", DataType.STRING.java, "EDU_LEVEL", 32));
		productAuthorClassFields.add(new Field("titleId", "职称", DataType.STRING.java, "TITLE_SELECT", 32));
		productAuthorClassFields.add(new Field("subjectId", "学科", DataType.STRING.java, "SUBJECT1", 32));
		productAuthorClassFields.add(new Field("authorUnit", "工作单位", DataType.STRING.java, 32));
		productAuthorClassFields.add(new Field("authorUnitId", "工作单位id", DataType.STRING.java, 32));
		productAuthorClassFields.add(new Field("eduDegreeId", "学位", DataType.STRING.java, "EDU_DEGREE", 32));
		productAuthorClassFields.add(new Field("workRatio", "贡献率", OtherUtils.TPYE_DOUBLE));
		productAuthorClassFields.add(new Field("orderId", "署名顺序", OtherUtils.TPYE_INTEGER));
		if ("V8.5.3".equals(DBUtils.getEadpDataType())) {
			productAuthorClassFields.add(new Field("srScore", "成果科研分", DataType.DOUBLE.java));
		}
		superClassFieldMap.put("ProductAuthor", productAuthorClassFields);
	}

	private static void initProductSuperClassFieldMap() {
		List<Field> productClassFields = Lists.newArrayList();
		productClassFields.add(new Field("name", "名称", DataType.STRING.java, 512));
		productClassFields.add(new Field("unitId", "所属单位", DataType.STRING.java, "PERSON_KY_UNIT", 32));
		productClassFields.add(new Field("divisionId", "所属教研室", DataType.STRING.java, 32));
		productClassFields.add(new Field("authorNumber", "作者数", OtherUtils.TPYE_INTEGER));
		productClassFields.add(new Field("note", "备注", DataType.STRING.java, 2000));
		productClassFields.add(new Field("firstAuthorId", "第一作者id", DataType.STRING.java, 32));
		productClassFields.add(new Field("firstAuthorName", "第一作者姓名", DataType.STRING.java, 64));
		productClassFields.add(new Field("firstAuthorAccount", "第一作者职工号", DataType.STRING.java, 64));
		productClassFields.add(new Field("firstAuthorTitleId", "第一作者职称", DataType.STRING.java, "TITLE_SELECT", 32));
		productClassFields.add(new Field("firstAuthorSexId", "第一作者性别", DataType.STRING.java, "FIRST_AUTHOR_SEXID", "RY_XM_NN", 32, "no"));
		productClassFields.add(new Field("firstAuthorEduLevelId", "第一作者学历", DataType.STRING.java, "EDU_LEVEL", 32));
		productClassFields.add(new Field("firstAuthorEduDeGreeId", "第一作者学位", DataType.STRING.java, "FIRST_AUTHOR_EDU_DEGREE_ID", "EDU_DEGREE", 32, "no"));
		productClassFields.add(new Field("fileIds", "附件", DataType.STRING.java, 500));
		productClassFields.add(new Field("authorPIds", "成员personId合集", DataType.STRING.java, 2000, "AUTHORPIDS"));
		productClassFields.add(new Field("authorUnitIds", "成员unitId合集", DataType.STRING.java, 2000, "AUTHORUNITIDS"));
		productClassFields.add(new Field("completeDataStatus", "数据完善状态", DataType.STRING.java, 40, "COMPLETEDATASTATUS"));
		if ("V8.5.3".equals(DBUtils.getEadpDataType())) {
			productClassFields.add(new Field("subjectClassId", "学科门类", DataType.STRING.java, "STA_CLASS", 32));
			productClassFields.add(new Field("subjectId", "一级学科", DataType.STRING.java, "SUBJECT1", 32));
			productClassFields.add(new Field("schoolSign", "学校署名", DataType.STRING.java, "SCHOOL_SIGN", 32));
		}
		superClassFieldMap.put("Product", productClassFields);
	}

	private static void initProjectSuperClassFieldMap() {
		List<Field> projectClassFields = Lists.newArrayList();
		projectClassFields.add(new Field("code", "项目编号", DataType.STRING.java, 64));
		projectClassFields.add(new Field("name", "项目名称", DataType.STRING.java, 512));
		projectClassFields.add(new Field("unitId", "所属单位", DataType.STRING.java, "PERSON_KY_UNIT", 32, "userinfo"));
		projectClassFields.add(new Field("divisionId", "所属教研室", DataType.STRING.java, 32));
		projectClassFields.add(new Field("beginDate", "开始日期", DataType.DATE.java));
		projectClassFields.add(new Field("authorizeDate", "立项日期", DataType.DATE.java));
		projectClassFields.add(new Field("planEndDate", "计划完成日期", DataType.DATE.java));
		projectClassFields.add(new Field("completeState", "结项状态", DataType.STRING.java, 32));
		projectClassFields.add(new Field("projectStatusId", "项目状态", DataType.STRING.java, "PROJECT_STATE", 32));
		projectClassFields.add(new Field("chargerType", "负责人类型", DataType.STRING.java, "AUTHOR_TYPE", 32));
		projectClassFields.add(new Field("chargerCode", "负责人编号", DataType.STRING.java, 32, "CHARGER_CODE", "userinfo"));
		projectClassFields.add(new Field("chargerName", "负责人姓名", DataType.STRING.java, 50));
		projectClassFields.add(new Field("chargerNameView", "负责人姓名", DataType.STRING.java, true));
		projectClassFields.add(new Field("chargerPhone", "负责人电话", DataType.STRING.java, 50));
		projectClassFields.add(new Field("chargerEmail", "负责人邮箱", DataType.STRING.java, 50));
		projectClassFields.add(new Field("transactor", "经办人", DataType.STRING.java, 50));
		projectClassFields.add(new Field("transactorPhone", "经办人电话", DataType.STRING.java, 50));
		projectClassFields.add(new Field("transactorEmail", "经办人邮箱", DataType.STRING.java, 50));
		projectClassFields.add(new Field("feeAuthorize", "批准经费", DataType.DOUBLE.java));
		projectClassFields.add(new Field("feeCode", "经费卡号", DataType.STRING.java, 50));
		projectClassFields.add(new Field("subjectClassId", "教育部统计归属", DataType.STRING.java, "STA_CLASS", 32));
		projectClassFields.add(new Field("subjectId", "一级学科", DataType.STRING.java, "SUBJECT1", 32));
		projectClassFields.add(new Field("note", "备注", DataType.STRING.java, 2000));

		projectClassFields.add(new Field("standby1", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby2", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby3", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby4", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby5", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby6", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby7", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby8", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby9", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));
		projectClassFields.add(new Field("standby10", "备用字段", DataType.STRING.java, 400).setUseState("unuse"));

		projectClassFields.add(new Field("incomeFee", "已到金额", DataType.DOUBLE.java, true));
		projectClassFields.add(new Field("totalFee", "已借票金额", DataType.DOUBLE.java, true));
		projectClassFields.add(new Field("payoutFee", "已报销金额", DataType.DOUBLE.java, true));
		projectClassFields.add(new Field("isOutFee", "已拨金额", DataType.DOUBLE.java, true));
		projectClassFields.add(new Field("transferFee", "结转金额", DataType.DOUBLE.java, true));

		projectClassFields.add(new Field("authorPIds", "成员personId合集", DataType.STRING.java, 1000, "AUTHORPIDS", "userinfo"));
		projectClassFields.add(new Field("authorUnitIds", "成员unitId合集", DataType.STRING.java, 1000, "AUTHORUNITIDS", "userinfo"));

		projectClassFields.add(new Field("buyerContractCount", "出账合同数", DataType.INTEGER.java, true));

		projectClassFields.add(new Field("completeDataStatus", "数据完善状态", DataType.STRING.java, 40, "COMPLETEDATASTATUS"));

		projectClassFields.add(new Field("unCheckedIncomeFee", "待审核到账金额", DataType.DOUBLE.java, true));
		projectClassFields.add(new Field("outFeeInCheck", "待审核外拨金额", DataType.DOUBLE.java, true));

		projectClassFields.add(new Field("isBudgetControl", "是否管控预算", DataType.STRING.java, "SF", 32));
		projectClassFields.add(new Field("budgetStandardId", "预算标准id", DataType.STRING.java,32));
		projectClassFields.add(new Field("budgetCount", "项目预算", DataType.INTEGER.java, true));
		projectClassFields.add(new Field("allowMiddleCheckDate", "允许中检时间", DataType.STRING.java, true));
		projectClassFields.add(new Field("allowCompleteDate", "允许结项日期", DataType.STRING.java, true));
		projectClassFields.add(new Field("budgetState", "调账状态", DataType.STRING.java, true));

		Field field = new Field("memberNames", "项目成员", "String", true);
		AppendSearch appendSearch = new AppendSearch();
		field.setAppendSearch(appendSearch);
		appendSearch.setValue("SELECT ddd, concat(PERSON_NAME, (CASE WHEN MEMBER_TYPE='2' THEN '（学）' WHEN MEMBER_TYPE='3' THEN '（外）' ELSE '' END)) val FROM ddd WHERE ddd IN (^{id}) ORDER BY ORDER_ID");
		appendSearch.setRelativeField("ID");
		appendSearch.setSymbol("；");
		appendSearch.setMergeMultiVals(true);
		projectClassFields.add(field);

		superClassFieldMap.put("Project", projectClassFields);
	}


	public static void main(String[] args) throws IOException {
//		String jsonStr = FileUtils.readFile4String("src/main/resource/other/superClassFields.json");
//		JsonParser jsonParser = new JsonParser();
//		JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
//		JsonArray jsonArray = jsonObject.get(SuperClassName.ApplyInfo.toString()).getAsJsonArray();
//
//		Type type = new TypeToken<List<Field>>() {}.getType();
//		List<Field> applyInfoClassFields = gson.fromJson(jsonArray, type);
//		System.out.println(applyInfoClassFields);

		DBUtils.schoolInfo = SchoolInfo.广东工业大学;
		initProjectSuperClassFieldMap();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(superClassFieldMap);
		System.out.println(json);
//		FileUtils.write("src/main/resource/other/superClassFields.json", json);
	}

	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @return
	 */
	public static List<Field> addSuperClassFields(String superClass) {
		return addSuperClassFields(Enum.valueOf(SuperClassName.class, superClass));
	}
	
	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @return
	 */
	public static List<Field> addSuperClassFields(SuperClassName superClass) {
		List<Field> fields = Lists.newArrayList();
		int step = -1;
		switch (superClass) {
		case ApplyInfo: // 6
			if (step == -1 || step == 6) {
				fields.addAll(Constants2.getSuperClassFieldMap(SuperClassName.ApplyInfo.toString()));
				step = 1;
			}
		case ApplyBook: // 5
			if (step == -1 || step == 5) {
				fields.addAll(Constants2.getSuperClassFieldMap(SuperClassName.ApplyBook.toString()));
				step = 2;
			}
		case Product: // 4
			if (step == -1 || step == 4) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.Product.toString()));
				step = 2;
			}
		case ProductAuthor: // 3
			if (step == -1 || step == 3) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.ProductAuthor.toString()));
				step = 0;
			}
		case CheckBusinessEntity: // 2
			if (step == -1 || step == 2) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.CheckBusinessEntity.toString()));
				step = 1;
			}
		case BusinessEntity:
		case BizEntity: // 1
			if (step == -1 || step == 1) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.BizEntity.toString()));
				step = 0;
			}
			break;
		default: // 0
			break;
		}
		return fields;
	}
}
