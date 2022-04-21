package com.eplugger.onekey.addModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.utils.OtherUtils;
import com.google.common.collect.Maps;

public class Constants {
	private static List<Field> fieldList = new ArrayList<Field>();
	private static List<Field> authorfieldList = new ArrayList<Field>();
	
	private Constants() {
		System.out.println("Constants.Constants()");
	}
	
	public static List<Field> getFieldList() {
		return fieldList;
	}

	public static List<Field> getAuthorfieldList() {
		return authorfieldList;
	}

	private static List<Field> singleFieldList = new ArrayList<Field>();
	public static List<Field> getSingleFieldList() {
		singleFieldList.add(new Field("orderId", "序号", "Integer"));
		singleFieldList.add(new Field("startDate", "开始时间", "Date"));
		singleFieldList.add(new Field("endDate", "终止时间", "Date"));
		singleFieldList.add(new Field("projectName", "项目名称", OtherUtils.TPYE_STRING));
		singleFieldList.add(new Field("awardsUnit", "获奖情况/颁发单位", OtherUtils.TPYE_STRING));
		Field field1 = new Field("applyBookId", "项目ID", OtherUtils.TPYE_STRING, 32);
		field1.setUpdateInsert(false);
		singleFieldList.add(field1);
		Field field = new Field("projectApplyBook", null, "ProjectApplyBook", null, "APPLY_BOOK_ID", null);
		field.setAssociation(Constants.MANY_TO_ONE);
		field.setIgnoreImport(true);
		singleFieldList.add(field);
		return singleFieldList;
	}
	
	static {
//		fieldList.add(new Field("name", "会议名称", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("meetingTheme", "会议主题", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("operateUnit", "主办单位", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("assistanceUnit", "协办单位", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("unitId", "承办部门", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("divisionId", "教研室", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("subjectClassId", "学科门类", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("subjectId", "一级学科", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("meetingPlace", "会议地点", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("meetingTypeId", "会议类型", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("beginDate", "开始日期", "Date"));
//		fieldList.add(new Field("endDate", "开始日期", "Date"));
//		fieldList.add(new Field("paperNumber", "论文数量", "Integer"));
//		fieldList.add(new Field("personNumber", "代表人数", "Integer"));
//		fieldList.add(new Field("foreignDeputyNumber", "国外代表数量", "Integer"));
//		fieldList.add(new Field("meetingContacts", "会议联系人", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("meetingTel", "会议电话", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("meetingEmail", "会议电邮", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("isFormReport", "是否形成综合报告或建议", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("feeTotal", "会议经费", "Double"));
//		fieldList.add(new Field("feeSource", "经费来源", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("intro", "简介", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("fileIds", "附件", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("completeDataStatus", "数据完善状态", OtherUtils.TPYE_STRING));
		
//		fieldList.add(new Field("code", "编号", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("typeId", "著作类别", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("modeId", "著作形式", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("publishUnit", "出版单位", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("isbn", "ISBN号", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("publishDate", "出版时间", "Date"));
//		fieldList.add(new Field("publishAddressId", "出版地", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("isTranslated", "是否翻译为外文", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("languageId", "语种", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("wordNumber", "总字数", "Double"));
//		fieldList.add(new Field("assessValue", "考核分值", "Double"));
//		fieldList.add(new Field("publishLevel", "出版社级别", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("cip", "CIP号", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("isRecension", "是否为修订版", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("subjectClassId", "学科门类", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("subjectId", "一级学科", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("projectSourceId", "项目来源", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("schoolSign", "学校署名", OtherUtils.TPYE_STRING));
//		Field authorfield = new Field("authors", "著作作者", "List", null, "BOOK_ID", "BookTestAuthor");
//		authorfield.setAssociation("OneToMany");
//		authorfield.setOrderBy("orderId asc");
//		authorfield.setFetch("FetchMode.SELECT");
//		fieldList.add(authorfield);
//		fieldList.add(new Field("results", "考核总分", "List", null, "ENTITY_ID", "EntityAssessResult", "OneToMany"));
//		fieldList.add(new Field("allAuthor", "所有作者", OtherUtils.TPYE_STRING, "BOOK_ID", true,
//				"SELECT BOOK_ID, concat(AUTHOR_NAME, (CASE WHEN AUTHOR_TYPE='2' THEN '（学）' WHEN AUTHOR_TYPE='3' THEN '（外）' ELSE concat('（',AUTHOR_ACCOUNT,'）') END)) val FROM BIZ_BOOK_AUTHOR WHERE BOOK_ID IN (^{id}) ORDER BY ORDER_ID"));
//		fieldList.add(new Field("allAuthorId", "所有作者ID", OtherUtils.TPYE_STRING, "BOOK_ID", true,
//				"SELECT BOOK_ID, concat(PERSON_ID, (CASE WHEN AUTHOR_TYPE='2' THEN '' WHEN AUTHOR_TYPE='3' THEN '' ELSE '' END)) val FROM BIZ_BOOK_AUTHOR WHERE BOOK_ID IN (^{id}) ORDER BY ORDER_ID"));
		
		
		
//		fieldList.add(new Field("orderId", "序号", "Integer"));
//		fieldList.add(new Field("changeScore", "变更分值", OtherUtils.TPYE_STRING, 32, "CHANGE_SCORE"));
//		fieldList.add(new Field("bonusValue", "分值", "Double"));
//		fieldList.add(new Field("bonusReason", "原因", OtherUtils.TPYE_STRING, 500));
//		Field field1 = new Field("personAssessId", "考核人员Id", OtherUtils.TPYE_STRING, 32);
//		field1.setUpdateInsert(false);
//		fieldList.add(field1);
//		Field field = new Field("personAssess", null, "PersonAssess", null, "PERSON_ASSESS_ID", null);
//		field.setAssociation("ManyToOne");
//		fieldList.add(field);
		
//		authorfieldList.add(new Field("bookId", "著作id", OtherUtils.TPYE_STRING, 32));
//		authorfieldList.add(new Field("wordNumber", "参编字数", "Double"));
//		authorfieldList.add(new Field("bearTypeId", "承担角色", OtherUtils.TPYE_STRING));
//		Field field = new Field("bookTest", null, "BookTest", null, "BOOK_ID", null);
//		field.setAssociation("ManyToOne");
//		authorfieldList.add(field);
		
		
		
//		fieldList.add(new Field("", "", OtherUtils.TPYE_STRING));
//		fieldList.add(new Field("", "", "Date"));
//		fieldList.add(new Field("", "", "Double"));
//		fieldList.add(new Field("", "", "Integer"));
	}
	
	private static Map<String, String> fullClassNameMap = new HashMap<String, String>();
	private static final Map<String, String> ASSOCIATION_MAP = new HashMap<String, String>();
	public static final String MANY_TO_ONE = "ManyToOne";
	public static final String ONE_TO_MANY = "OneToMany";
	
	public static String getFullClassNameMap(String key) {
		return fullClassNameMap.get(key);
	}
	
	public static String putFullClassNameMap(String key, String value) {
		return fullClassNameMap.put(key, value);
	}
	
	public static String getAssociationMap(String key) {
		return ASSOCIATION_MAP.get(key);
	}
	
	private static final Map<String, List<Field>> SUPER_CLASS_FIELD_MAP = Maps.newHashMap();
	public static List<Field> getSuperClassFieldMap(String key) {
		if (SUPER_CLASS_FIELD_MAP.isEmpty()) {
			Constants.initSuperClassFieldMap();
		}
		return SUPER_CLASS_FIELD_MAP.get(key);
	}

	private static void initSuperClassFieldMap() {
		initProductSuperClassFieldMap();
		initProductAuthorSuperClassFieldMap();
		initCheckBusinessEntitySuperClassFieldMap();
		initBizEntitySuperClassFieldMap();
	}

	private static void initBizEntitySuperClassFieldMap() {
		List<Field> bizEntityClassFields = new ArrayList<Field>();
		bizEntityClassFields.add(new Field("createUserID", "创建用户编号", OtherUtils.TPYE_STRING, "CREATEUSERID", 100));
		bizEntityClassFields.add(new Field("createUserName", "创建用户名", OtherUtils.TPYE_STRING, "CREATEUSERNAME", 100));
		bizEntityClassFields.add(new Field("createDate", "创建时间", OtherUtils.TPYE_TIMESTAMP, "CREATEDATE", 0));
		bizEntityClassFields.add(new Field("lastEditUserID", "最后编辑用户编号", OtherUtils.TPYE_STRING, "LASTEDITUSERID", 100));
		bizEntityClassFields.add(new Field("lastEditUserName", "最后编辑用户名", OtherUtils.TPYE_STRING, "LASTEDITUSERNAME", 100));
		bizEntityClassFields.add(new Field("lastEditDate", "最后编辑日期", OtherUtils.TPYE_TIMESTAMP, "LASTEDITDATE", 0));
		SUPER_CLASS_FIELD_MAP.put("BizEntity", bizEntityClassFields);
	}

	private static void initCheckBusinessEntitySuperClassFieldMap() {
		List<Field> checkBusinessEntityClassFields = new ArrayList<Field>();
		checkBusinessEntityClassFields.add(new Field("checkStatus", "审核状态", OtherUtils.TPYE_STRING, "CHECKSTATUS", 64));
		checkBusinessEntityClassFields.add(new Field("checkDate", "审核时间", OtherUtils.TPYE_STRING, "CHECKDATE", 64));
		checkBusinessEntityClassFields.add(new Field("checker", "审核人", OtherUtils.TPYE_STRING, "CHECKER", 80));
		SUPER_CLASS_FIELD_MAP.put("CheckBusinessEntity", checkBusinessEntityClassFields);
	}

	private static void initProductAuthorSuperClassFieldMap() {
		List<Field> productAuthorClassFields = new ArrayList<Field>();
		productAuthorClassFields.add(new Field("authorType", "作者类型", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("personId", "人员Id", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("authorAccount", "职工号", OtherUtils.TPYE_STRING, 64));
		productAuthorClassFields.add(new Field("authorName", "作者姓名", OtherUtils.TPYE_STRING, 64));
		productAuthorClassFields.add(new Field("sexId", "性别", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("eduLevelId", "学历", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("titleId", "职称", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("subjectId", "学科", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("authorUnit", "工作单位", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("authorUnitId", "工作单位id", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("eduDegreeId", "学位", OtherUtils.TPYE_STRING, 32));
		productAuthorClassFields.add(new Field("workRatio", "贡献率", OtherUtils.TPYE_DOUBLE));
		productAuthorClassFields.add(new Field("orderId", "署名顺序", OtherUtils.TPYE_INTEGER));
		SUPER_CLASS_FIELD_MAP.put("ProductAuthor", productAuthorClassFields);
	}

	private static void initProductSuperClassFieldMap() {
		List<Field> productClassFields = new ArrayList<Field>();
		productClassFields.add(new Field("name", "名称", OtherUtils.TPYE_STRING, 512));
		productClassFields.add(new Field("unitId", "所属单位", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("divisionId", "所属教研室", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("authorNumber", "作者数", OtherUtils.TPYE_INTEGER));
		productClassFields.add(new Field("note", "备注", OtherUtils.TPYE_STRING, 2000));
		productClassFields.add(new Field("firstAuthorId", "第一作者id", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("firstAuthorName", "第一作者姓名", OtherUtils.TPYE_STRING, 64));
		productClassFields.add(new Field("firstAuthorAccount", "第一作者职工号", OtherUtils.TPYE_STRING, 64));
		productClassFields.add(new Field("firstAuthorTitleId", "第一作者职称", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("firstAuthorSexId", "第一作者性别", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("firstAuthorEduLevelId", "第一作者学历", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("firstAuthorEduDeGreeId", "第一作者学位", OtherUtils.TPYE_STRING, 32));
		productClassFields.add(new Field("fileIds", "附件", OtherUtils.TPYE_STRING, 500));
		productClassFields.add(new Field("authorPIds", "成员personId合集", OtherUtils.TPYE_STRING, "AUTHORPIDS", 2000));
		productClassFields.add(new Field("authorUnitIds", "成员unitId合集", OtherUtils.TPYE_STRING, "AUTHORUNITIDS", 2000));
		productClassFields.add(new Field("completeDataStatus", "数据完善状态", OtherUtils.TPYE_STRING, "COMPLETEDATASTATUS", 40));
		SUPER_CLASS_FIELD_MAP.put("Product", productClassFields);
	}

	static {
		fullClassNameMap.put("Date", "java.sql.Date");
		fullClassNameMap.put("CheckBusinessEntity", "com.eplugger.business.pub.entity.CheckBusinessEntity");
		fullClassNameMap.put("CheckBusinessBO", "com.eplugger.business.pub.bo.CheckBusinessBO");
		fullClassNameMap.put("CheckBusinessAction", "com.eplugger.business.pub.action.CheckBusinessAction");
		fullClassNameMap.put("IUnit", "com.eplugger.system.role.user.entity.IUnit");
		fullClassNameMap.put("ICompleteData", "com.eplugger.service.entity.ICompleteData");
		fullClassNameMap.put("BusinessDAO", "com.eplugger.business.pub.dao.BusinessDAO");
		fullClassNameMap.put("List", "java.util.List");
		fullClassNameMap.put("ArrayList", "java.util.ArrayList");
		fullClassNameMap.put("Product", "com.eplugger.business.product.entity.Product");
		fullClassNameMap.put("ProductBO", "com.eplugger.business.product.bo.ProductBO");
		fullClassNameMap.put("ProductAction", "com.eplugger.business.product.action.ProductAction");
		fullClassNameMap.put("ProductAuthor", "com.eplugger.business.product.entity.ProductAuthor");
		fullClassNameMap.put("EntityAssessResult", "com.eplugger.assess.personAssess.entity.EntityAssessResult");
		fullClassNameMap.put("BizEntity", "com.eplugger.service.entity.BizEntity");
		fullClassNameMap.put("EntityImpl", "com.eplugger.service.entity.EntityImpl");
		fullClassNameMap.put("ZXProject", "com.eplugger.business.project.entity.ZXProject");
		fullClassNameMap.put("VProject", "com.eplugger.business.pub.entity.VProject");
		fullClassNameMap.put("BusinessBO", "com.eplugger.business.pub.bo.BusinessBO");
		fullClassNameMap.put("BusinessAction", "com.eplugger.business.pub.action.BusinessAction");
		fullClassNameMap.put("", "");
		
		ASSOCIATION_MAP.put(ONE_TO_MANY, "@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)");
		ASSOCIATION_MAP.put(MANY_TO_ONE, "@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)");
	}
}
