package com.eplugger.xml.dom4j.utils.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eplugger.onekey.addField.entity.ModuleTable;
import com.eplugger.onekey.addField.entity.ModuleTables;

public class ModuleTableParser extends AbstractParser<ModuleTables> {
	public List<ModuleTable> getValidList(ModuleTables moduleTables) {
		if (moduleTables == null) {
			return new ArrayList<ModuleTable>();
		}
		return moduleTables.getModuleTableList().stream().filter(e -> !e.isIgnore()).collect(Collectors.toList());
	}
}
