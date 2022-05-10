package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

public class ParseXmlUtils {
	/**
	 * 对外提供xmlToBean的解析
	 * @param path
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T toBean(String path, Class<T> clazz) throws Exception {
		return ParseXmlUtilsBean.getInstance().toBean(path, clazz);
	}
	
	/**
	 * 对外提供xmlfromBean的解析
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static <T> Document fromBean(String path, T data, boolean isAutoWrite2File) throws Exception {
		return ParseXmlUtilsBean.getInstance().fromBean(path, data, isAutoWrite2File);
	}
	
	/**
	 * 对外提供xmlfromBean的解析，返回Document，不写出Xml文件
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static <T> Document fromBean(T data) throws Exception {
		return ParseXmlUtilsBean.getInstance().fromBean(null, data, false);
	}
}
