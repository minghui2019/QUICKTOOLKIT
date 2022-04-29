package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

public class ParseUtils {
	/**
	 * 对外提供xmlToBean的解析
	 * @param path
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T toBean(String path, Class<T> clazz) throws Exception {
		return ParseUtilsBean.getInstance().toBean(path, clazz);
	}
	
	/**
	 * 对外提供xmlfromBean的解析
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static <T> Document fromBean(String path, T data) throws Exception {
		return ParseUtilsBean.getInstance().fromBean(path, data);
	}
}
