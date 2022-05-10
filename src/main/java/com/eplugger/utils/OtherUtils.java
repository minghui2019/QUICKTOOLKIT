package com.eplugger.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.eplugger.common.lang.StringUtils;

public class OtherUtils {
	public static final String SPOT = ".";
	public static final String TAB_FOUR = "    ";
	public static final String TAB_EIGHT = "        ";
	public static final String TAB_TWELVE = "            ";
	public static final String TAB_SIXTEEN = "                ";
	public static final String TPYE_STRING = "String";
	public static final String TPYE_INTEGER = "Integer";
	public static final String TPYE_DOUBLE = "Double";
	public static final String TPYE_DATE = "Date";
	public static final String TPYE_TIMESTAMP = "Timestamp";
	public static final String TPYE_LIST = "List";
	public static final String TPYE_ARRAYLIST = "ArrayList";
	
	public static final Set<String> TYPE_SET = new HashSet<String>() {
		private static final long serialVersionUID = 6382701752284525058L;
		{
			add(TPYE_STRING);
			add(TPYE_INTEGER);
			add(TPYE_DOUBLE);
			add(TPYE_DATE);
			add(TPYE_TIMESTAMP);
		}
	};
	
	public static boolean isSimpleType(String dataType) {
		return OtherUtils.TPYE_STRING.equals(dataType) || OtherUtils.TPYE_INTEGER.equals(dataType)
				|| OtherUtils.TPYE_DOUBLE.equals(dataType) || OtherUtils.TPYE_DATE.equals(dataType)
				|| OtherUtils.TPYE_TIMESTAMP.equals(dataType);
	}
	
	/**
	 * 生成实体类的beanid
	 * @param str
	 * @return
	 */
	public static String produceBeanid(String str) {
		String[] arrs = str.split("_");
		String result = "";
		for (int i = 1; i < arrs.length; i++) {
			result += arrs[i];
		}
		arrs = null;
		if ("APPRAISALPRODUCT".equals(result)) {
			result = "JDPRODUCT";
		} else if ("APPRAISALPRODUCTAUTHOR".equals(result)) {
			result = "JDPRODUCTAUTHOR";
		}
		return result;
	}
	
	/**
	 * BIZ_PAPER_AUTHOR取中间PAPER
	 * @param str
	 * @return
	 */
	public static String getMainBeanId(String str) {
		String[] arrs = str.split("_");
		String result = "";
		for (int i = 1; i < arrs.length - 1; i++) {
			result += arrs[i];
			if (i < arrs.length - 2) {
				result += "_";
			}
		}
		return result;
	}
	
	/**
	 * map清空
	 * @param maps
	 */
	@SafeVarargs
	public static void clearMap(Map<String, String> ... maps) {
		for (Map<String, String> map : maps) {
			map.clear();
		}
	}
	
	/**
	 * 去除下划线
	 * @param str
	 * @return
	 */
	public static String deleteTextDecoration(String str){
		if (StringUtils.isBlank(str)) {
			return str;
		}
		return str.replace("_", "");
	}
	
	public static void main(String[] args) {
		String chiness = "中文";
		System.out.println(StringUtils.getFirstSpell(chiness));
	}
}
