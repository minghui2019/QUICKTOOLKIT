package com.eplugger.onekey.cfgCategoryEntry;

import java.io.IOException;

import com.eplugger.util.FileUtil;
import com.eplugger.util.OtherUtils;

/**
 * 新增字典
 * @author Admin
 */
public class ConfigCategoryEntry {
	public static void main(String[] args) {
		String categoryName = "SCHOOL_SIGN_BOOK"; //常量名
		String bizName = "学校署名(著作)"; //业务名称
		String bizType = "成果"; //业务类型
		String version = "V8.5.0";//eadp版本
//		String version = "V8.5.0";//eadp版本
//		String version = "V8.3.0";//eadp版本
//		String version = "V3.1.0";//eadp版本
		String[] keyArray = {"1", "2", "3", "4", "5", "0",};//字典代码
		String[] valueArray = {"第一单位", "第二单位", "第三单位", "第四单位", "第五单位", "其他", };//字典值
		String content = createCategoryStr(keyArray, valueArray, categoryName, bizName, bizType, version);
		String fileName = bizName + "字典配置SQL.sql";
		FileUtil.outFile(content, "C:\\Users\\Admin\\Desktop", fileName);
		try {
			Runtime.getRuntime().exec("notepad.exe  C:\\Users\\Admin\\Desktop\\" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String createCategoryStr(String[] keyArray, String[] valueArray, String categoryName, String bizName, String bizType, String version) {
		StringBuffer sb = new StringBuffer();
		sb.append(DELETE_FROM_CFG_CATEGORY_ENTRY.replace("{categoryName}", categoryName));
		sb.append(DELETE_FROM_CFG_CATEGORY.replace("{categoryName}", categoryName));
		sb.append(OtherUtils.CRLF + OtherUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY]的数据如下:" + OtherUtils.CRLF);
		String cfgCategoryId = OtherUtils.getUuid();
		sb.append(INSERT_INTO_CFG_CATEGORY.replace("{cfgCategoryId}", cfgCategoryId).replace("{bizName}", bizName)
				.replace("{categoryName}", categoryName).replace("{bizType}", bizType).replace("{version}", version));
		sb.append(OtherUtils.CRLF + OtherUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY_ENTRY]的数据如下:" + OtherUtils.CRLF);
		System.out.println(keyArray == null);
		for (int i = 0; i < valueArray.length; i++) {
			if (valueArray[i] == null) {
				break;
			}
			sb.append(INSERT_INTO_CFG_CATEGORY_ENTRY.replace("{uuid}",
					OtherUtils.getUuid()).replace("{key}", (keyArray == null ? OtherUtils.getUuid() : keyArray[i]))
				.replace("{cfgCategoryId}", cfgCategoryId).replace("{version}", version).replace("{orderId}", String.valueOf(i + 1))
				.replace("{value}", valueArray[i]));
		}
		sb.append(OtherUtils.CRLF + OtherUtils.CRLF);
		return sb.toString();
	}
	
	private static final String DELETE_FROM_CFG_CATEGORY_ENTRY = "DELETE FROM CFG_CATEGORY_ENTRY WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY "
			+ "WHERE CATEGORYNAME='{categoryName}');" + OtherUtils.CRLF;
	private static final String DELETE_FROM_CFG_CATEGORY = "delete from CFG_CATEGORY WHERE CATEGORYNAME='{categoryName}';"
			+ OtherUtils.CRLF;
	private static final String INSERT_INTO_CFG_CATEGORY = "insert into \"CFG_CATEGORY\"(\"ID\",\"BIZNAME\",\"CATEGORYNAME\",\"BIZTYPE\",\"FROMBIZ\","
			+ "\"CANCFG\",\"EADPDATATYPE\",\"BZ\",\"FROMJAVA\") values('{cfgCategoryId}','{bizName}','{categoryName}','{bizType}',0,'0','{version}',NULL,NULL);"
			+ OtherUtils.CRLF;
	private static final String INSERT_INTO_CFG_CATEGORY_ENTRY = "insert into \"CFG_CATEGORY_ENTRY\"(\"ID\",\"CODE\",\"REMARK\",\"CATEGORYID\","
			+ "\"PARENT_CODE\",\"EADPDATATYPE\",\"ORDERS\",\"CANCFG\",\"CASCADECODE\",\"NAME\",\"NAME_LOCAL\")"
			+ " values('{uuid}','{key}',NULL,'{cfgCategoryId}',NULL,'{version}',{orderId},'0',NULL,'{value}',NULL);" + OtherUtils.CRLF;
}
