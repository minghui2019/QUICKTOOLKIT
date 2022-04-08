package com.eplugger.commons.beanutils;


public class ConvertUtils {
	public static Object convert(String value, Class<?> clazz) {
		return ConvertUtilsBean.getInstance().convert(value, clazz);
	}
}
