package com.eplugger.onekey.addField.util;

import org.dom4j.Element;

import com.eplugger.onekey.addField.entity.AppendSearch;
import com.eplugger.util.ParseXmlAbst;
import com.eplugger.utils.ClassUtils;

public class AppendSearchParse extends ParseXmlAbst<AppendSearch> {
	private static class AppendSearchParseSingleton {
		private static AppendSearchParse instance = new AppendSearchParse();
	}
	
	private AppendSearchParse() { }
	
	public static AppendSearchParse getInstance() {
		return AppendSearchParseSingleton.instance;
	}
	
	@Override
	public AppendSearch parseXml4Element2Entity(Element rootElement) {
		AppendSearch field = new AppendSearch();
        return parseXml4Element2Entity(field, rootElement);
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, AppendSearch e) {
		ClassUtils.setProperty(e, stuChild.getName(), stuChild.getStringValue(), String.class);
	}
}
