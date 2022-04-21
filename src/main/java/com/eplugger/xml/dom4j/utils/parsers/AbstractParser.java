package com.eplugger.xml.dom4j.utils.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.eplugger.annotation.Booleaner;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.utils.ClassUtils;
import com.eplugger.xml.dom4j.util.XmlFileUtils;
import com.eplugger.xml.dom4j.utils.Parser;

/**
 * Dom4j解析器抽象实现类
 * @author Admin
 *
 * @param <T>
 */
public abstract class AbstractParser<T> implements Parser<T> {
	@Override
	public List<T> parse(String path) {
		Document document;
		try {
			document = XmlFileUtils.readDocument(path);
		} catch (DocumentException e) {
			throw new RuntimeException(e + "文件打不开");
		}
		Element rootElement = document.getRootElement();
		List<T> list = new ArrayList<>();
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) { //遍历子节点
        	list.add(parseXml4Element2Entity(iterator.next()));
        }
        return list;
	}
	
	/**
	 * 子类有需要时重写即可
	 */
	@Override
	public List<T> parseValidList(List<T> list) {
		return list;
	}

	/**
	 * 按模块解析xml
	 * @param element
	 * @return
	 */
	protected abstract T parseXml4Element2Entity(Element element);

	/**
	 * 解析xml节点到实体类
	 * @param e
	 * @param rootElement
	 * @return
	 */
	protected T parseXml4Element2Entity(T e, Element rootElement) {
    	List<Attribute> attributes = rootElement.attributes();
    	for (Attribute attribute : attributes) { // 二级节点获取属性值
    		String attrName = attribute.getName();
    		if (isBooleanerField(e, attrName)) {
    			ClassUtils.setProperty(e, StringUtils.firstCharUpperCase(attrName), Boolean.valueOf(attribute.getValue()), Boolean.class);
    		} else {
    			ClassUtils.setProperty(e, StringUtils.firstCharUpperCase(attrName), attribute.getValue(), String.class);
    		}
    	}
    	for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) { //遍历子节点
    		parseXml4Field2Entity(iterator.next(), e);
    	}
        return e;
	}
	
	/**
	 * 解析字段名并赋值到实体类
	 * @param element
	 * @param e
	 */
	protected abstract void parseXml4Field2Entity(Element element, T e);

	protected boolean isBooleanerField(T entity, String attrName) {
		Boolean flag = false;
		try {
			flag = entity.getClass().getDeclaredField(attrName).isAnnotationPresent(Booleaner.class);
		} catch (NoSuchFieldException | SecurityException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
}
