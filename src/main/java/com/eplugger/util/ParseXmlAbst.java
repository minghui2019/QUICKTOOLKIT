package com.eplugger.util;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.eplugger.annotation.Booleaner;
import com.util.ArrayList;

public abstract class ParseXmlAbst<T> implements IParseXml<T> {

	@Override
	public List<T> parseAllList(List<T> list) {
		return list;
	}
	
	@Override
	public List<T> parseValidList(List<T> list) {
		return list;
	}
	
	@Override
	public List<T> parseXml4Element2List(Element rootElement) {
        List<T> list = new ArrayList<>();
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) { //遍历子节点
        	list.add(parseXml4Element2Entity(iterator.next()));
        }
        return list;
	}
	
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
	 * @param stuChild
	 * @param e
	 */
	protected abstract void parseXml4Field2Entity(Element stuChild, T e);

	protected boolean isBooleanerField(T entity, String attrName) {
		if (BooleanerMap.containsKey(attrName)) {
			return BooleanerMap.get(attrName);
		}
		
		Boolean flag = false;
		try {
			flag = entity.getClass().getDeclaredField(attrName).isAnnotationPresent(Booleaner.class);
		} catch (NoSuchFieldException | SecurityException e) {
			flag = false;
			e.printStackTrace();
		}
		BooleanerMap.put(attrName, flag);
		return flag;
	}
}
