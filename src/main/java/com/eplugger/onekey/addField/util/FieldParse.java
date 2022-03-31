package com.eplugger.onekey.addField.util;

import java.util.List;

import org.dom4j.Element;

import com.eplugger.onekey.addField.entity.AppendSearch;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.util.ClassUtils;
import com.eplugger.util.ParseXmlAbst;
import com.eplugger.util.XmlParseUtils;

public class FieldParse extends ParseXmlAbst<Field> {
	private static class FieldParseSingleton {
		private static FieldParse instance = new FieldParse();
	}
	
	private FieldParse() {}
	
	public static FieldParse getInstance() {
		return FieldParseSingleton.instance;
	}

	/**
	 * 利用XmlParse进行xml解析
	 * @param filePath
	 * @return
	 */
	public List<Field> getValidList(String filePath) {
		XmlParseUtils<Field> parse = new XmlParseUtils<>(FieldParseSingleton.instance);
		List<Field> moduleTables = parse.getValidList(filePath);
		return moduleTables;
	}
	
	public List<Field> getAllList(String filePath) {
		XmlParseUtils<Field> parse = new XmlParseUtils<>(FieldParseSingleton.instance);
		List<Field> moduleTables = parse.getAllList(filePath);
		return moduleTables;
	}

	@Override
	public Field parseXml4Element2Entity(Element rootElement) {
        return parseXml4Element2Entity(new Field(), rootElement);
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, Field e) {
		String stuName = stuChild.getName();
		Object fieldValue = stuChild.getStringValue();
		Class<?> type = String.class;
		if ("Precision".equals(stuName)) {
			fieldValue = Integer.valueOf(stuChild.getStringValue());
			type = Integer.class;
		} else if ("TranSient".equals(stuName)) {
			fieldValue = Boolean.valueOf(stuChild.getStringValue());
			type = Boolean.class;
		} else if ("AppendSearch".equals(stuName)) {
 			fieldValue = AppendSearchParse.getInstance().parseXml4Element2Entity(stuChild);
 			type = AppendSearch.class;
 		}
		ClassUtils.setProperty(e, stuName, fieldValue, type);
	}
}
