package com.eplugger.onekey.viewFile.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Data;

@Data
@Dom4JTag
public class ModuleViews {
	@Dom4JField(type = Dom4JFieldType.TAG)
	List<ModuleView> moduleViewList = Lists.newArrayList();

	@Override
	public String toString() {
		return "ModuleViews [\n" + moduleViewList.toString() + "\n]";
	}
	
	/**
	 * 获取有效的xml内容
	 * @return
	 */
	public List<ModuleView> getValidList() {
		if (this == null || this.moduleViewList.isEmpty()) {
			return Lists.newArrayList();
		}
		return this.moduleViewList.stream()
				.filter(module -> !module.isIgnore())
				.collect(Collectors.toList());
	}
	
	/**
	 * 业务需求，对解析的list去重，转化为map
	 * @return
	 */
	public Map<String, ModuleView> getValidModuleViewMap() {
		List<ModuleView> moduleViews = this.getValidList();
		return moduleViews.stream().filter(m -> {
			return !(Strings.isNullOrEmpty(m.getTableName()) || Strings.isNullOrEmpty(m.getViewName()));
		}).collect(Collectors.toMap(ModuleView::getTableName, m -> m, (m1, m2) -> m1, LinkedHashMap::new));
	}
	
	public Map<String, ModuleView> getModuleTableMap() {
		List<ModuleView> moduleViewList = this.moduleViewList;
		return moduleViewList.stream().filter(m -> {
			return !(Strings.isNullOrEmpty(m.getTableName()) || Strings.isNullOrEmpty(m.getViewName()));
		}).collect(Collectors.toMap(ModuleView::getTableName, m -> m, (m1, m2) -> m1, LinkedHashMap::new));
	}

	public boolean add(ModuleView moduleView) {
		if (moduleView == null) {
			return false;
		}
		if (this.moduleViewList == null) {
			this.moduleViewList = Lists.newArrayList();
		}
		return moduleViewList.add(moduleView);
	}
}
