package com.eplugger.xml.dom4j.utils;

import java.util.List;

public class ParseUtils {
	public static <T> List<T> parseValidList(String path, Class<T> clazz) throws Exception {
		return ParseUtilsBean.getInstance().parseValidList(path, clazz);
	}
	
	public static <T> List<T> parseAllList(String path, Class<T> clazz) throws Exception {
		return ParseUtils.parse(path, clazz);
	}
	
	protected static <T> List<T> parse(String path, Class<T> clazz) throws Exception {
		return ParseUtilsBean.getInstance().parse(path, clazz);
	}
}
