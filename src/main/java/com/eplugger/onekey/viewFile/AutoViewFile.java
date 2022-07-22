package com.eplugger.onekey.viewFile;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.ExcelUtils;
import com.eplugger.utils.OtherUtils;

public class AutoViewFile {
	private static final String[] productList = { "PAPER", "BOOK", "RESEARCH_REPORT", "APPRAISAL_PRODUCT",
			"ART_PRODUCT", "PATENT", "COPYRIGHT", "HONOR", "PRODUCT_TRANS", "PATENT_PROPOSAL", "STANDARD", "MEDICINE",
			"BREED" };
//	private static final String[] productList = { "PAPER" };
//	private static final String[] projectList = { };
	private static final String[] projectList = { "XJ_PROJECT", "YF_CONTRACT", "ZX_PROJECT", "SS_PROJECT" };

//	private static final String[] otherList = { };
	private static final String[] otherList = { "PERSON", "EXPERT", "STUDENT", "UNIT", "RESEARCH_UNIT", "AGENCY",
			"JOIN_MEETING", "MEETING", "LECTURE", "PERSONNEL_ACCEPT", "PERSONNEL_DISPATCH", "PROJECT_INCOME" };
	
	public static LinkedHashMap<String, String> tableList = new LinkedHashMap<>(); // key:表名,value:视图名

	static {
		for (int i = 0; i < productList.length; i++) {
			tableList.put("BIZ_" + productList[i], "VIEW_" + productList[i]);
			tableList.put("BIZ_" + productList[i] + "_AUTHOR", "VIEW_" + productList[i] + "_AUTHOR");
		}
		for (int i = 0; i < projectList.length; i++) {
			tableList.put("BIZ_" + projectList[i], "VIEW_" + projectList[i]);
			tableList.put("BIZ_" + projectList[i] + "_MEMBER", "VIEW_" + projectList[i] + "_MEMBER");
		}
		for (int i = 0; i < otherList.length; i++) {
			tableList.put("BIZ_" + otherList[i], "VIEW_" + otherList[i]);
		}
		
//		tableList.put("DM_PAPER_LEVEL", "VIEW_PAPER_LEVEL");
//		tableList.put("DM_MAGAZINE_SOURCE", "VIEW_MAGAZINE_SOURCE");
	}
	
	public static void main(String[] args) {
		// 1、根据表名生成Excel;tableList:需要生成视图的有序map集合
//		generationTabelExcel(tableList);
		// 2、自动生成视图语句
//		generationViewSql();
	}
	
	/**
	 * 1、根据表名生成Excel;tableList:需要生成视图的有序map集合
	 */
	@Test
	public void generationTabelExcel() {
		generationTabelExcel(tableList);
	}
	
	/**
	 * 2、自动生成视图语句
	 */
	@Test
	public void generationViewSql_() {
		generationViewSql();
	}

	private static String filePath = "C:/Users/Admin/Desktop/视图";
	private static String xlsName = "共享视图.xls";
	private static String VIEW_NAME = null;// 视图名
	private static String TABEL_NAME = null;// 表名
	public static Map<String, String> emptyMap = new HashMap<String, String>();// 需要给空值的字段名
	public static Map<String, String> categroyMap = new HashMap<String, String>();// 通过自定义字典获得（key:当前表字段名,value:字典常量名）
	public static Map<String, String> tableMap = new HashMap<String, String>();// 查询关联表得到（key:当前表字段名,value:查询表名{默认id,代码列不是存id，则value后追加逗号代码列(,代码列)}）
	public static Map<String, String> noteMap = new HashMap<String, String>();// 注释
	public static LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// key:表字段名,value:别名,没有别名存空值

	public static void generationViewSql() {
		Workbook workbook = ExcelUtils.openWorkbook(filePath + File.separator + "xls" + File.separator, xlsName);
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);// 遍历拿每一个sheet
			TABEL_NAME = sheet.getSheetName();
			VIEW_NAME = tableList.get(sheet.getSheetName());
			// 为跳过第一行目录设置count
			OtherUtils.clearMap(map, tableMap, categroyMap, emptyMap, noteMap);
			int count = 0;
			for (Row row : sheet) {
				if (count < 1) {
					count++;
					continue;
				}
				if (StringUtils.isBlank(row.getCell(0).toString())) { // 如果当前行没有数据，跳出循环
					return;
				}
				String lieMing = row.getCell(0).toString();// 列名
				String bieMing = null;
				String isNull = null;
				String ownDictionary = null;
				String joinTable = null;
				String hasNote = row.getCell(5).toString();// 注释
				if (null != row.getCell(1)) {
					bieMing = row.getCell(1).toString();// 别名
				}
				if (null != row.getCell(2)) {
					isNull = row.getCell(2).toString();// 是否全为空值
				}
				if (null != row.getCell(3)) {
					ownDictionary = row.getCell(3).toString();// 自定义字典
				}
				if (null != row.getCell(4)) {
					joinTable = row.getCell(4).toString();// 关联表查询得到
				}
				map.put(lieMing, bieMing);
				if (joinTable != null) {
					tableMap.put(lieMing, joinTable);
				}
				if (ownDictionary != null) {
					categroyMap.put(lieMing, ownDictionary);
				}
				if (isNull != null) {
					emptyMap.put(lieMing, null);
				}
				noteMap.put(lieMing, hasNote);
			}
			generationViewSql(VIEW_NAME);
		}
	}

	/**
	 * 根据所有表名生成对应Excel
	 * @param tableList 所有需要视图的表名集合
	 */
	private static void generationTabelExcel(LinkedHashMap<String, String> tableList) {
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		for (Map.Entry<String, String> entry : tableList.entrySet()) {
			String beanid = OtherUtils.produceBeanid(entry.getKey());
			Map<String, String> nameMap = DBUtils.getEntryNameBySql("select * from " + entry.getKey());
			String sqlToCfg = null;
			List<String[]> cfgList = null;
			// 专利特殊处理(jsp=/business/patent/noDocking/patentAdd.jsp)823项目需要打开
			if (beanid.equals("PATENT")) {
				sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION)='" + beanid + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanid + "/NODOCKING/" + beanid + "ADD.JSP' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
			}
			else if (beanid.equals("PATENTAUTHOR")) {
				sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION) like '%TO_ADD%' and JSP = '/BUSINESS/PATENT/NODOCKING/PATENTADD.JSP' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
			}
			else if (beanid.equals("PROJECTINCOME")) {
				sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE where UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION)='" + beanid + "ACTION!TO_ADD' and JSP = '/BUSINESS/outlay/" + beanid + "/" + beanid + "ADD.JSP' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
			}
			else if (beanid.contains("MEMBER")) {
				sqlToCfg = "SELECT NAME,DATA_TYPE,CATEGORYNAME FROM SYS_CFG_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_TABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(JSP) like '%" + beanid + "MANAGE.JSP%' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
			}
			else if (beanid.contains("AUTHOR")) {
				sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION) like '%TO_ADD%' and ROLE = '4' AND BINDSCENEID IS NOT NULL)";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				if (cfgList.size() == 0) {
					sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION) like '%TO_ADD%' and ROLE = '4')";
					cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				}
			}
			else {
				sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION)='" + beanid + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanid + "/" + beanid + "ADD.JSP' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				if (cfgList.size() == 0) {
					sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION)='" + beanid + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanid + "/" + beanid + "ADD.JSP' and ROLE = '4')";
					cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				}
			}
			String sqlToMeaning = "select NAME,MEANING from SYS_ENTITY_META where UPPER(BEANID)='" + beanid + "'";
			Map<String, String> meaningMap = DBUtils.getMeaningBySql(sqlToMeaning); // 存放表中注释
			
			// 建立新的sheet对象（excel的表单）
			HSSFSheet sheet = wb.createSheet(entry.getKey());
			sheet.setColumnWidth(0, 8000); sheet.setColumnWidth(1, 3000); sheet.setColumnWidth(2, 2000);
			sheet.setColumnWidth(3, 6000); sheet.setColumnWidth(4, 10000); sheet.setColumnWidth(5, 5000);
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			// 添加表头
			ExcelUtils.setCellValues(sheet, 0, 6, new String[]{"列名", "别名", "是否全为空值", "自定义字典", "关联表名及查询值", "注释"});
			ExcelUtils.setCellValues(sheet, 1, 6, new String[]{"ID", "ID", null, null, null, "主键"});
			int rowNum = 1;
			if (entry.getKey().contains("MEMBER")) {
				ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"PERSON_CODE", "ZGH", null, null, null, "职工号"});
				if (entry.getKey().contains("YF_CONTRACT")) {
					ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"CONTRACT_ID", "HTID", null, null, null, "合同id"});
				} else {
					ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"PROJECT_ID", "XMID", null, null, null, "项目id"});
				}
				
			} else if (entry.getKey().contains("AUTHOR")) {
				ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"AUTHOR_ACCOUNT", "ZGH", null, null, null, "职工号"});
				ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{OtherUtils.getMainBeanId(entry.getKey()) + "_ID", "CGID", null, null, null, "成果id"});
			}
			for (int i = 0; i < cfgList.size(); i++) {
				if ("hidden".equals(cfgList.get(i)[1]) || "file".equals(cfgList.get(i)[1])) {
					continue;
				}
				String categroy = null;
				String joinTable = null;
				if (StringUtils.isNotBlank(cfgList.get(i)[2])) {
					if ("unitId".equals(cfgList.get(i)[0])) {
						joinTable = "BIZ_UNIT,ID";
					} else {
						String sql = "SELECT TABLENAME,CODECOLUMN FROM CFG_CATEGORY_MAPPING WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY WHERE CATEGORYNAME = '" + cfgList.get(i)[2] + "')";
						String[] strs = DBUtils.getStrsBySql(sql);
						categroy = (strs[0] == null) ? cfgList.get(i)[2] : null;
						joinTable = (strs[0] == null) ? null : strs[0] + "," + strs[1];
					}
				}
				String nameTemp = cfgList.get(i)[0];
				//个别字段命名不规范问题
				if ("publishLevelId".equals(nameTemp)) {
					nameTemp = "paperLevelId";
				} else if ("honerType".equals(nameTemp)) {
					nameTemp = "honorType";
				} else if ("credentialFileId".equals(nameTemp)) {
					nameTemp = "crednetialFileId";
				} else if ("paperSpace".equals(nameTemp)) {
					nameTemp = "pageSpace";
				} else if ("bearUnitOrder".equals(nameTemp) && !"XJ_PROJECT".equals(beanid)) {
					nameTemp = "undertakingUnitRanking";
				}
				ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{
						nameMap.get(nameTemp.toUpperCase()) == null ? ("*" + nameTemp) : nameMap.get(nameTemp.toUpperCase()),
						StringUtils.getFirstSpell(meaningMap.get(cfgList.get(i)[0])), null, categroy, joinTable,
						meaningMap.get(cfgList.get(i)[0])});
			}
		}
		ExcelUtils.outExcel(wb, filePath + File.separator + "xls" + File.separator, xlsName);
	}
	
	/**
	 * 自动生成视图语句
	 */
	public static void generationViewSql(String viewName) {
		int size = map.size();
		StringBuilder sb = new StringBuilder();
		sb.append("create view " + viewName + StringUtils.CRLF).append("as" + StringUtils.CRLF).append("(SELECT" + StringUtils.CRLF);
		String version = DBUtils.getUrl();
		version = version.split(";")[1];
		version = version.substring(22, 25);
		int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			i++;
			String key = entry.getKey();
			String value = entry.getValue();
			if (categroyMap.containsKey(key)) {// 有字典的属性
				String str = "";
				if ("811".equals(version)) {
					str = "\"VALUE\"";
				} else {
					str = "\"NAME\"";
				}
				sb.append("(SELECT CFG_CATEGORY_ENTRY." + str
						+ " FROM CFG_CATEGORY_ENTRY,CFG_CATEGORY WHERE CFG_CATEGORY.CATEGORYNAME='"
						+ categroyMap.get(key) + "' AND CFG_CATEGORY.ID=CFG_CATEGORY_ENTRY.CATEGORYID AND " + TABEL_NAME
						+ "." + key + "=CFG_CATEGORY_ENTRY.CODE) ");// + " AND " + TABEL_NAME + "." + key + "<>NULL) "
				sb.append((null == value) ? key : value); // 别名为空，用字段名代替
//				sb.append(key);
			} else if (tableMap.containsKey(key)) {
				String[] str = tableMap.get(key).split(",");
				if ("PAPER_LEVEL_ID".equals(key) || "EMBODY_TYPE_ID".equals(key)) {
					if (DBUtils.isOracle()) {
						sb.append("(SELECT listagg(NAME, ',') WITHIN GROUP(ORDER BY ID) FROM " + str[0] + " where INSTR(" + TABEL_NAME + "." + key + ", " + str[0] + "." + str[1] + ")>0");
					} else if (DBUtils.isSqlServer()) {
						sb.append("stuff((SELECT ',' + NAME FROM " + str[0] + " WHERE CHARINDEX(" + str[0] + "." + str[1] + ", " + TABEL_NAME + "." + key + ")>0 for xml path('')),1,1,''");
					}
				} else {
					sb.append("(SELECT NAME FROM " + str[0] + " WHERE " + str[0] + "." + str[1] + "=" + TABEL_NAME + "." + key);
					if (StringUtils.equalsIgnoreCase(key, "PROJECT_SOURCE_ID")) {
						sb.append(" AND DM_PROJECT_STAT_SOURCE.CLASS_ID=" + TABEL_NAME + ".SUBJECT_CLASS_ID");
					}
				}
				sb.append(") ");
				sb.append((null == value) ? key : value); // 别名为空，用字段名代替
//				sb.append(key);
			} else {// 没有字典的属性
				if (emptyMap.containsKey(key)) {// 需要给空值的字段
					sb.append("NULL " + ((null == value) ? key : value)); // 别名为空，用字段名代替
//					sb.append("NULL " + key);
				} else {
					sb.append(key + ((null == value) ? "" : " " + value)); // 别名为空，用字段名代替
//					sb.append(key + " " + key);
				}
			}
			if (size != i) {
				sb.append(",");
			}
			sb.append(" --" + noteMap.get(key) + StringUtils.CRLF);
		}
		sb.append("FROM " + TABEL_NAME + ");" + StringUtils.CRLF + "GO" + StringUtils.CRLF);
		FileUtils.write(filePath + File.separator + "sql" + File.separator + viewName + ".sql", sb);
	}
}
