package com.eplugger.onekey.addModule.util;

import java.util.List;

import org.dom4j.Element;

import com.eplugger.onekey.addModule.entity.Module;
import com.eplugger.onekey.addModule.entity.ModuleInfo;
import com.eplugger.util.ClassUtils;
import com.eplugger.util.ParseXmlAbst;
import com.eplugger.util.XmlParseUtils;
import com.util.ArrayList;

public class ModuleParse extends ParseXmlAbst<Module> {
	private static class ModuleParseSingleton {
		private static ModuleParse instance = new ModuleParse();
	}
	
	private ModuleParse() {}
	
	public static ModuleParse getInstance() {
		return ModuleParseSingleton.instance;
	}

	@Override
	public List<Module> parseValidList(List<Module> list) {
		List<Module> moduleTables = new ArrayList<Module>();
		for (Module module : list) {
			if (module.getIgnore()) {
				continue;
			}
			moduleTables.add(module);
		}
		return moduleTables;
	}
	
	/**
	 * 利用XmlParse进行xml解析
	 * @param filePath
	 * @return
	 */
	public Module getValidModule(String filePath) {
		try {
			List<Module> list = getValidList(filePath);
			return list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return new Module();
		}
	}

	@Override
	public Module parseXml4Element2Entity(Element rootElement) {
		Module module = new Module();
        return parseXml4Element2Entity(module, rootElement);
	}

	public List<Module> getValidList(String filePath) {
		XmlParseUtils<Module> parse = new XmlParseUtils<>(ModuleParseSingleton.instance);
		List<Module> moduleTables = parse.getValidList(filePath);
		return moduleTables;
	}

	public List<Module> getAllList(String filePath) {
		XmlParseUtils<Module> parse = new XmlParseUtils<>(ModuleParseSingleton.instance);
		List<Module> moduleTables = parse.getAllList(filePath);
		return moduleTables;
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, Module e) {
		String stuName = stuChild.getName();
		Object fieldValue = stuChild.getStringValue();
		Class<?> type = String.class;
		if ("MainModule".equals(stuName) || "AuthorModule".equals(stuName)) {
			fieldValue = ModuleInfoParse.getInstance().parseXml4Element2Entity(stuChild);
			type = ModuleInfo.class;
		}
		ClassUtils.setProperty(e, stuName, fieldValue, type);
	}
}
