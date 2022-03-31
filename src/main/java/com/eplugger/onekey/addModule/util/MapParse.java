package com.eplugger.onekey.addModule.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.eplugger.util.ParseXmlAbst;

public class MapParse extends ParseXmlAbst<Map<String, String>> {
	private static class MapParseSingleton {
		private static MapParse instance = new MapParse();
	}
	
	private MapParse() {}
	
	public static MapParse getInstance() {
		return MapParseSingleton.instance;
	}
	
	@Override
	public Map<String, String> parseXml4Element2Entity(Element rootElement) {
		Map<String, String> map = new HashMap<>();
		// Class元素
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) { //遍历子节点
        	Element stu = iterator.next();
        	List<Attribute> attributes = stu.attributes();
        	String[] clazz = new String[2];
        	for (Attribute attribute : attributes) {
        		String key = attribute.getName();
        		String value = attribute.getStringValue();
        		if ("key".equals(key)) {
        			clazz[0] = value;
        		} else if ("value".equals(key)) {
        			clazz[1] = value;
        		}
        	}
        	map.put(clazz[0], clazz[1]);
        }
        return map;
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, Map<String, String> e) {
	}
}
