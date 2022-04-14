package com.eplugger.xml.dom4j.utils;

import java.util.List;

/**
 * Dom4j解析器顶层接口
 * @author Admin
 *
 * @param <T>
 */
public interface Parser<T> {
	/**
	 * 解析所有的xml内容
	 * @param list
	 * @return
	 */
	List<T> parse(String path);
	
	/**
	 * 获取有效的xml内容
	 * @param list
	 * @return
	 */
	List<T> parseValidList(List<T> list);
}
