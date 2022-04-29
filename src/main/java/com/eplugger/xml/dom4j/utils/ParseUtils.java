package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

public class ParseUtils {
	public static <T> T toBean(String path, Class<T> clazz) throws Exception {
		return ParseUtilsBean.getInstance().toBean(path, clazz);
	}
	
	protected static <T> Document fromBean(String path, T data) throws Exception {
		return ParseUtilsBean.getInstance().fromBean(path, data);
	}
}
