package com.eplugger.onekey.jsonFile;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;
import top.tobak.utils.ExcelUtils;

@SuppressWarnings("all")
public class AutoJsonFile {
	public static LinkedHashMap<String, String> tableList = new LinkedHashMap() {// key:表名,value:视图名
		{
			put("BIZ_" + "BOOK", "JSON_" + "BOOK");
			put("BIZ_" + "BOOK_AUTHOR", "JSON_" + "BOOK_AUTHOR");
//			put("BIZ_" + "BREED", "VIEW_" + "BREED");
//			put("BIZ_" + "BREED_AUTHOR", "VIEW_" + "BREED_AUTHOR");
//			put("BIZ_" + "COPYRIGHT", "VIEW_" + "COPYRIGHT");
//			put("BIZ_" + "COPYRIGHT_AUTHOR", "VIEW_" + "COPYRIGHT_AUTHOR");
//			put("BIZ_" + "HONOR", "VIEW_" + "HONOR");
//			put("BIZ_" + "HONOR_AUTHOR", "VIEW_" + "HONOR_AUTHOR");
//			put("BIZ_" + "PAPER", "VIEW_" + "PAPER");
//			put("BIZ_" + "PAPER_AUTHOR", "VIEW_" + "PAPER_AUTHOR");
//			put("BIZ_" + "PATENT", "VIEW_" + "PATENT");
//			put("BIZ_" + "PATENT_AUTHOR", "VIEW_" + "PATENT_AUTHOR");
			put("BIZ_" + "XJ_PROJECT", "JSON_" + "XJ_PROJECT");
			put("BIZ_" + "XJ_PROJECT_MEMBER", "JSON_" + "XJ_PROJECT_MEMBER");
//			put("BIZ_" + "YF_CONTRACT", "VIEW_" + "YF_CONTRACT");
//			put("BIZ_" + "YF_CONTRACT_MEMBER", "VIEW_" + "YF_CONTRACT_MEMBER");
			put("BIZ_" + "ZX_PROJECT", "JSON_" + "ZX_PROJECT");
			put("BIZ_" + "ZX_PROJECT_MEMBER", "JSON_" + "ZX_PROJECT_MEMBER");
		}
	};
	public static void main(String[] args) {
		// 1、根据表名生成Excel;tableList:需要生成视图的有序map集合
		generationTabelExcel(tableList);
		// 2、自动生成视图语句
//		generationViewSql();
	}

	private static String filePath = "C:/Users/minghui/Desktop/易数汇";
	private static String xlsName = "易数汇配置Json.xls";
	private static String JSON_NAME = null;// 视图名
	private static String TABEL_NAME = null;// 表名
	/** 属性名 */
	public static Map<String, String> labelMap = new HashMap();
	/** 属性表字段名 */
	public static Map<String, String> nameMap = new HashMap();
	/** 数据类型 */
	public static Map<String, String> typeMap = new HashMap();
	/**	字典id */
	public static Map<String, String> dictionaryMap = new HashMap();

	private static void generationViewSql() {
		Workbook workbook = ExcelUtils.openWorkbook(filePath + File.separator + "xls" + File.separator, xlsName);
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);// 遍历拿每一个sheet
			TABEL_NAME = sheet.getSheetName();
			JSON_NAME = tableList.get(sheet.getSheetName());
			// 为跳过第一行目录设置count
			OtherUtils.clearMap(labelMap, nameMap, typeMap, dictionaryMap);
			int count = 0;
			for (Row row : sheet) {
				if (count < 1) {
					count++;
					continue;
				}
				if (StringUtils.isBlank(row.getCell(0).toString())) { // 如果当前行没有数据，跳出循环
					return;
				}
				String label = row.getCell(0).toString();
				String name = row.getCell(1).toString();
				String type = row.getCell(2).toString();
				String dictionary = null;
				if (null != row.getCell(3)) {
					dictionary = row.getCell(3).toString();
				}
				labelMap.put(name, label);
				nameMap.put(name, name);
				typeMap.put(name, type);
				if (dictionary != null) {
					dictionaryMap.put(name, dictionary);
				}
			}
			generationViewSql(JSON_NAME);
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
			if (beanid.contains("MEMBER")) {
				sqlToCfg = "SELECT NAME,DATA_TYPE,CATEGORYNAME FROM SYS_CFG_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_TABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(JSP) like '%" + beanid + "MANAGE.JSP%' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
			} else if (beanid.contains("AUTHOR")) {
				sqlToCfg = "SELECT NAME,DATA_TYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION) like '%TO_ADD%' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				if (cfgList.size() == 0) {
					sqlToCfg = "SELECT NAME,DATA_TYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION) like '%TO_ADD%' and ROLE = '4')";
					cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				}
			} else {
				sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION)='" + beanid + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanid + "/" + beanid + "ADD.JSP' and ROLE = '4')";
				cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				if (cfgList.size() == 0) {
					sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanid + "' and UPPER(REQACTION)='" + beanid + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanid + "/" + beanid + "ADD.JSP' and ROLE = '4')";
					cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
				}
			}
			String sqlToMeaning = "select NAME,MEANING from SYS_ENTITY_META where UPPER(BEANID)='" + beanid + "'";
			List<String[]> entityMetas = DBUtils.getMeaningBySql(sqlToMeaning);
			Map<String, String> meaningMap = Maps.newHashMap();// 存放表中注释
			for (String[] entityMeta : entityMetas) {
				meaningMap.put(entityMeta[0], entityMeta[1]);
			}

			// 建立新的sheet对象（excel的表单）
			HSSFSheet sheet = wb.createSheet(entry.getKey());
			sheet.setColumnWidth(0, 8000); sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 2000); sheet.setColumnWidth(3, 6000);
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			// 添加表头
			ExcelUtils.setCellValues(sheet, 0, 4, new String[]{"label", "name", "type", "dictionary"});
			int rowNum = 0;
			for (int i = 0; i < cfgList.size(); i++) {
				if ("hidden".equals(cfgList.get(i)[1]) || "ID".equals(cfgList.get(i)[0])){
					continue;
				}
				String type = "string";
				switch (cfgList.get(i)[1]) {
				case "date":
				case "timestamp":
					type = "date"; break;
				case "int":
				case "float":
				case "money-万元":
				case "money":
					type = "number"; break;
				default:
					break;
				}
				String temp = cfgList.get(i)[0];
				String name = nameMap.get(temp.toUpperCase());
				name = (name == null) ? "*" + temp : name;
				ExcelUtils.setCellValues(sheet, ++rowNum, 4, new String[]{meaningMap.get(cfgList.get(i)[0]),
						name, type, cfgList.get(i)[2]});
			}
		}
		ExcelUtils.outExcel(wb, filePath + File.separator + "xls" + File.separator, xlsName);
	}
	
	/**
	 * 自动生成视图语句
	 */
	public static void generationViewSql(String viewName) {
		int size = nameMap.size();
		StringBuilder sb = new StringBuilder();
		sb.append("    {" + StringUtils.CRLF + "      \"category\":\"\"," + StringUtils.CRLF + "      \"hasCheck\":true," + StringUtils.CRLF + "      \"label\":\"\"," + StringUtils.CRLF);
		sb.append("      \"name\":\"" + TABEL_NAME + "\"," + StringUtils.CRLF + "      \"pkName\":\"ID\"," + StringUtils.CRLF + "      \"properties\":[" + StringUtils.CRLF);
		int i = 0;
		for (Map.Entry<String, String> entry : nameMap.entrySet()) {
			i++;
			String key = entry.getKey();
			String value = entry.getValue();
			String dictionary = null;
			if (dictionaryMap.get(key) != null) {// 有字典的属性
				dictionary = dictionaryMap.get(key);
			}
			sb.append("        {" + StringUtils.CRLF + "          \"label\":\"" + labelMap.get(key) + "\"," + StringUtils.CRLF + ""
					+ "          \"name\":\"" + key + "\"," + StringUtils.CRLF + "          \"type\":\"" + typeMap.get(key) + "\"");
			if (dictionary != null) {
				sb.append("," + StringUtils.CRLF + "          \"dictionary\":\"" + dictionary + "\"");
			}
			sb.append(StringUtils.CRLF + "        }");
			if (size != i) {
				sb.append("," + StringUtils.CRLF);
			}
		}
		sb.append(StringUtils.CRLF + "      ]," + StringUtils.CRLF + "      \"rootTable\":true" + StringUtils.CRLF + "    }," + StringUtils.CRLF);
		
		FileUtils.write(filePath + File.separator + "json" + File.separator + viewName + ".json", sb.toString());
	}
}
