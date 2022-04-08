package com.eplugger.main;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException {
//		Class<Boolean> type = Boolean.class;
//		String flag = "true";
//		Method method = type.getMethod("valueOf", String.class);
//		Boolean invoke = (Boolean)method.invoke(type, flag);
//		System.out.println(invoke);
//		System.out.println(ConvertUtils.convert("true", Boolean.class));
		String str1 = "ssss";
		String str2 = "Ssss";
		System.out.println(Test.toUpperCaseFirstOne(str1));
		System.out.println(Test.toUpperCaseFirstOne(str2));
		char char1 = 'a';
		char char11 = 'z';
		char char2 = 'A';
		char char22 = 'Z';
		System.out.println((int)char1);
		System.out.println((int)char11);
		System.out.println((int)char2);
		System.out.println((int)char22);
	}

	private static String toUpperCaseFirstOne(String s) {
		// 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
		char[] cs = s.toCharArray();
		if (cs[0] < 97 || cs[0] > 122) {
			return s;
		}
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	/**
	 * 正则表达式替换字符串
	 * 
	 * @param s
	 * @param map
	 * @return
	 */
	static Pattern mPattern = Pattern.compile("\\{(.*?)\\}");

	public static String repeat(String s, Map<String, String> map) {
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			s = s.replace("{" + key + "}", map.get(key));
		}
		return s;
	}

	public static String doubleToString(double num, int key) {
		String result = "0";
		switch (key) {
		case 6:
			DecimalFormat df = new DecimalFormat("#.######");
			result = df.format(num);
			break;
		case 5:
			DecimalFormat df2 = new DecimalFormat("#.#####");
			result = df2.format(num);
			break;
		case 4:
			DecimalFormat df3 = new DecimalFormat("#.####");
			result = df3.format(num);
			break;
		case 2:
			DecimalFormat df4 = new DecimalFormat("#,##0.00");
			result = df4.format(num);
			break;
		default:
			DecimalFormat df5 = new DecimalFormat("#.######");
			result = df5.format(num);
			break;
		}
		return result;
	}

	public void randomDouble() {
		String str1 = "";
		int ii = 3, jj = 8;
		for (int i = 0; i < ii; i++) {
			String str2 = "[";
			for (int j = 0; j < jj; j++) {
				int ma = (int) (Math.random() * 300);
				if (ma < 100) {
					ma += 100;
				}
				str2 += String.valueOf(ma);
				if (j < jj - 1) {
					str2 += ",";
				}
			}
			str2 += "]";
			str1 += str2;
			if (i < ii - 1) {
				str1 += ",";
			}
		}
		System.out.println(str1);
	}
}
