package com.eplugger.onekey.addField.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.eplugger.onekey.addField.entity.ModuleTable;
import com.eplugger.util.ClassUtils;
import com.eplugger.util.ParseXmlAbst;
import com.eplugger.util.XmlParseUtils;

public class ModuleTableParse extends ParseXmlAbst<ModuleTable> {
	private static class ModuleTableParseSingleton {
		private static ModuleTableParse instance = new ModuleTableParse();
	}
	
	private ModuleTableParse() {}
	
	public static ModuleTableParse getInstance() {
		return ModuleTableParseSingleton.instance;
	}

	@Override
	public List<ModuleTable> parseValidList(List<ModuleTable> list) {
		List<ModuleTable> moduleTables = new ArrayList<ModuleTable>();
		for (ModuleTable moduleTable : list) {
			if (moduleTable.getIgnore()) {
				continue;
			}
			moduleTables.add(moduleTable);
		}
		return moduleTables;
	}
	
	/**
	 * 利用XmlParse进行xml解析
	 * @param filePath
	 * @return
	 */
	public List<ModuleTable> getValidList(String filePath) {
		XmlParseUtils<ModuleTable> parse = new XmlParseUtils<>(ModuleTableParseSingleton.instance);
		List<ModuleTable> moduleTables = parse.getValidList(filePath);
		return moduleTables;
	}
	
	public List<ModuleTable> getAllList(String filePath) {
		XmlParseUtils<ModuleTable> parse = new XmlParseUtils<>(ModuleTableParseSingleton.instance);
		List<ModuleTable> moduleTables = parse.getAllList(filePath);
		return moduleTables;
	}
	
	/**
	 * 业务需求，对解析的list去重，转化为map
	 * @param filePath
	 * @return
	 */
	public Map<String, String> getValidModuleTableMap(String filePath) {
		List<ModuleTable> validModuleTableList = getValidList(filePath);
		Map<String, String> map = new HashMap<String, String>();
		for (ModuleTable moduleTable : validModuleTableList) {
			map.put(moduleTable.getModuleName(), moduleTable.getTableName());
		}
		return map;
	}
	
	public Map<String, String> getModuleTableMap(String filePath) {
		List<ModuleTable> moduleTableList = this.getAllList(filePath);
		Map<String, String> map = new HashMap<String, String>();
		for (ModuleTable moduleTable : moduleTableList) {
			map.put(moduleTable.getModuleName(), moduleTable.getTableName());
		}
		return map;
	}

	@Override
	public ModuleTable parseXml4Element2Entity(Element rootElement) {
		ModuleTable moduleTable = new ModuleTable();
        return parseXml4Element2Entity(moduleTable, rootElement);
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, ModuleTable e) {
		ClassUtils.setProperty(e, stuChild.getName(), stuChild.getStringValue(), String.class);
	}
}
