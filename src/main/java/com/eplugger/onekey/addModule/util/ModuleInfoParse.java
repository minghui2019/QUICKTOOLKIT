package com.eplugger.onekey.addModule.util;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.eplugger.onekey.addField.util.FieldParse;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.util.ClassUtils;
import com.eplugger.util.ParseXmlAbst;

public class ModuleInfoParse extends ParseXmlAbst<ModuleInfo> {
	private static class ModuleInfoParseSingleton {
		private static ModuleInfoParse instance = new ModuleInfoParse();
	}
	
	private ModuleInfoParse() {}
	
	public static ModuleInfoParse getInstance() {
		return ModuleInfoParseSingleton.instance;
	}
	
	@Override
	public ModuleInfo parseXml4Element2Entity(Element rootElement) {
		ModuleInfo moduleInfo = new ModuleInfo();
        return parseXml4Element2Entity(moduleInfo, rootElement);
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, ModuleInfo e) {
		String stuName = stuChild.getName();
		Object fieldValue = stuChild.getStringValue();
		Class<?> type = String.class;
		if ("Fields".equals(stuName)) {
			fieldValue = FieldParse.getInstance().parseXml4Element2List(stuChild);
			type = List.class;
		} else if ("SuperClassMap".equals(stuName)) {
			fieldValue = MapParse.getInstance().parseXml4Element2Entity(stuChild);
			type = Map.class;
		}
		ClassUtils.setProperty(e, stuName, fieldValue, type);
	}
}
