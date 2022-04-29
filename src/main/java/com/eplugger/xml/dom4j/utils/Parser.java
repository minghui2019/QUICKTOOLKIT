package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

import com.eplugger.xml.dom4j.simple.Dom4JParser;

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
	default T toBean(Class<T> cls, String path) {
		Dom4JParser xmlToBean = new Dom4JParser(path);
        T bean = xmlToBean.toBean(cls);
        return bean;
	}
	
	/**
	 * 获取有效的xml内容
	 * @param list
	 * @return
	 */
	default Document fromBean(T data, String path) {
		Dom4JParser testXml = new Dom4JParser(path);
		Document document = testXml.fromBean(data, true);
		return document;
	}
}
