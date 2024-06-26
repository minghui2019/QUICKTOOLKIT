package com.eplugger.onekey.entity;

import java.util.List;
import java.util.stream.Collectors;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.tobak.xml.entity.IXmlEntity;

/**
 * <p>模块信息</p>
 * @author minghui
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Modules implements IXmlEntity {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<Module> moduleList = Lists.newArrayList();

	@Override
	public String toString() {
		return "Modules [\n" + moduleList.toString() + "\n]";
	}
	
	/**
	 * 获取有效的xml内容
	 * @return
	 */
	public List<Module> getValidList() {
		if (this == null || this.moduleList.isEmpty()) {
			return Lists.newArrayList();
		}
		return this.moduleList.stream()
				.filter(module -> !module.isIgnore())
				.collect(Collectors.toList());
	}
	
	/**
	 * 利用XmlParse进行xml解析
	 * @return
	 */
	public Module getValidModule() {
		try {
			List<Module> list = getValidList();
			return list.get(0);
		} catch (Exception e) {
			return new Module();
		}
	}

	public List<Category> getCategories() {
		List<Module> fieldList = getValidList();
		List<Category> categories = Lists.newArrayList();
		fieldList.forEach(module -> {
			categories.addAll(module.getCategories());
		});
		return categories;
	}
}
