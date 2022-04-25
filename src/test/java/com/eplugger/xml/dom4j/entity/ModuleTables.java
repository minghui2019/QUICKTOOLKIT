package com.eplugger.xml.dom4j.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.google.common.collect.Lists;

import lombok.Data;

@Data
@Dom4JTag
public class ModuleTables {
	@Dom4JField(type = Dom4JFieldType.TAG)
	List<ModuleTable> moduleTables = new ArrayList<ModuleTable>();

	@Override
	public String toString() {
		return "ModuleTables [\n" + moduleTables.toString() + "\n]";
	}
	
	public List<ModuleTable> getValidList() {
		if (this == null || this.moduleTables.isEmpty()) {
			return Lists.newArrayList();
		}
		return this.moduleTables.stream()
				.filter(module -> !module.isIgnore())
				.collect(Collectors.toList());
	}
}
