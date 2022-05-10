package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

import com.eplugger.xml.dom4j.annotation.Dom4JTag;

/**
 * Dom4j解析器顶层接口
 * @author Admin
 *
 * @param <T>
 */
public interface ParserXml<T> {
	/**
	 * 映射为实体类
	 * @param cls 实体类的Class对象
	 * @param inPath 输入的文件路径
	 * @return
	 */
	T toBean(Class<T> cls, String inPath);
	
	/**
	 * 指定对象转换为Document对象, 目标对象必须使用{@link Dom4JTag @Dom4JTag}注解
	 * @param data 目标对象 <T> 对象类型
	 * @param outPath 文件的输出路径
	 * @param isAutoWrite2File 是否写出到文件
	 * @return Document实体, 目标对象为null时总是返回null
	 */
	Document fromBean(T data, String outPath, boolean isAutoWrite2File);
}
