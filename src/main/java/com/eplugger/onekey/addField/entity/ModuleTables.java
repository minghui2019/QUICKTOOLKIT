package com.eplugger.onekey.addField.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import lombok.Data;

@Data
@Dom4JTag
public class ModuleTables {
	@Dom4JField(type = Dom4JFieldType.TAG)
	List<ModuleTable> moduleTableList = new ArrayList<ModuleTable>();

	@Override
	public String toString() {
		return "ModuleTables [\n" + moduleTableList.toString() + "\n]";
	}
	
	/**
	 * 获取有效的xml内容
	 * @return
	 */
	public List<ModuleTable> getValidList() {
		if (this == null || this.moduleTableList.isEmpty()) {
			return Lists.newArrayList();
		}
		return this.moduleTableList.stream()
				.filter(module -> !module.isIgnore())
				.collect(Collectors.toList());
	}
	
	/**
	 * 业务需求，对解析的list去重，转化为map
	 * @param filePath
	 * @return
	 */
	public Map<String, String> getValidModuleTableMap() {
		List<ModuleTable> moduleTables = this.getValidList();
		return moduleTables.stream().filter(m -> {
			return !(Strings.isNullOrEmpty(m.getModuleName()) || Strings.isNullOrEmpty(m.getTableName()));
		}).collect(Collectors.toMap(ModuleTable::getModuleName, ModuleTable::getTableName));
	}
}
