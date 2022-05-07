package com.eplugger.onekey.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eplugger.utils.DBUtils;

public class SqlUtils {
	
	/**
	 * 小驼峰命名法转化为下划线命名法<br>
	 * @param field
	 * @return
	 */
	public static String lowerCamelCase2UnderScoreCase(String field) {
		Pattern p = Pattern.compile("[A-Z]");
		Matcher m = p.matcher(field);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "_" + m.group());
		}
		m.appendTail(sb);
		return sb.toString().toUpperCase();
	}
	
	/**
	 * 小驼峰命名法转化为下划线命名法<br>
	 * 举例： paperId --> PAPER_ID<br>
	 * @param fields
	 * @return
	 */
	public static String[] lowerCamelCase2UnderScoreCase(String[] fields) {
		List<String> list = new ArrayList<String>();
		for (String field : fields) {
			list.add(lowerCamelCase2UnderScoreCase(field));
		}
		return list.toArray(new String[] {});
	}
	
	/**
	 * 根据javabean数据类型获取数据库数据类型<br>
	 * 由DBUtil.isSqlServer()提供数据库类型判断<br>
	 * @param dataType
	 * @param precision
	 * @return
	 */
	public static String getDatabaseDataType(String dataType, Integer precision) {
		String result = "";
		switch (dataType) {
		case "String":
			result = DBUtils.isSqlServer() ? "varchar" : "VARCHAR2";
			if (precision == null) {
				result += "(255)";
			} else if (precision >= 2000) {
				result = DBUtils.isSqlServer() ? "text" : "clob";
			} else {
				result += "(" + precision + ")";
			}
			break;
		case "Timestamp":
			result = DBUtils.isSqlServer() ? "datetime" : "DATE";
			break;
		case "Date":
			result = DBUtils.isSqlServer() ? "datetime" : "DATE";
			break;
		case "Double":
			result = DBUtils.isSqlServer() ? "decimal(18,6)" : "NUMBER(18,6)";
			break;
		case "Integer":
			result = DBUtils.isSqlServer() ? "int" : "NUMBER(18,6)";
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 获取当前实体的元数据最后一个编号
	 * @param beanId
	 * @return
	 */
	public static int getMaxOrdersByBeanId(String beanId) {
		String sql = "";
		if (DBUtils.isSqlServer()) {
			sql = "SELECT top 1 ORDERS FROM SYS_ENTITY_META WHERE BEANID='" + beanId + "' ORDER BY ORDERS DESC;";
		} else if (DBUtils.isOracle()) {
			sql = "SELECT ORDERS FROM(SELECT ORDERS FROM SYS_ENTITY_META WHERE BEANID='" + beanId + "' ORDER BY ORDERS DESC) WHERE ROWNUM=1";
		}
		return DBUtils.getOrdersFromEntityMeta(sql);
	}
}
