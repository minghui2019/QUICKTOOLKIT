package com.eplugger.util;

import java.util.Map;
import java.util.UUID;

import com.eplugger.commons.lang3.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class OtherUtils {
	public static final String CRLF = "\r\n";
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
	
	public static String[] getUuid(int count) {
		String[] uuids = new String[count];
		for (int i = 0; i < count; i++) {
			uuids[i] = UUID.randomUUID().toString().replace("-", "");
		}
		return uuids;
	}
	
	public static String getUuid() {
		return UUID.randomUUID().toString().replace("-", "");
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
	
	/**
	 * 汉字转拼音，取首字母并大写
	 * @param chinese
	 * @return
	 */
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();
	}
}
