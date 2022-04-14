package com.eplugger.xml.dom4j.utils.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Element;

import com.eplugger.onekey.addField.entity.ModuleTable;
import com.eplugger.util.ClassUtils;

public class ModuleTableParser extends AbstractParser<ModuleTable> {
	@Override
	public ModuleTable parseXml4Element2Entity(Element rootElement) {
		ModuleTable moduleTable = new ModuleTable();
        return parseXml4Element2Entity(moduleTable, rootElement);
	}

	@Override
	protected void parseXml4Field2Entity(Element stuChild, ModuleTable e) {
		ClassUtils.setProperty(e, stuChild.getName(), stuChild.getStringValue(), String.class);
	}

	@Override
	public List<ModuleTable> parseValidList(List<ModuleTable> list) {
		if (list == null || list.isEmpty()) {
			return new ArrayList<ModuleTable>();
		}
		return list.stream().filter(e -> !e.getIgnore()).collect(Collectors.toList());
	}
}
