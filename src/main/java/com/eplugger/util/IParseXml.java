package com.eplugger.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * 具体内容解析工具
 * @author Admin
 *
 * @param <T>
 */
public interface IParseXml<T> {
	public static Map<String, Boolean> BooleanerMap = new HashMap<>();
	
	/**
	 * 获取有效的xml内容
	 * @param list
	 * @return
	 */
	List<T> parseAllList(List<T> list);
	
	/**
	 * 获取有效的xml内容
	 * @param list
	 * @return
	 */
	List<T> parseValidList(List<T> list);

	/**
	 * 按模块解析xml
	 * @param rootElement
	 * @return List<T>
	 */
	List<T> parseXml4Element2List(Element rootElement);
	
	/**
	 * 按模块解析xml
	 * @param rootElement
	 * @return T
	 */
	T parseXml4Element2Entity(Element rootElement);
}
