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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Fields implements ICategory {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<Field> fieldList = Lists.newArrayList();

	@Override
	public String toString() {
		return "Fields [" + fieldList.toString() + "]";
	}

	@Override
	public List<Category> getCategories() {
		return fieldList.stream().filter(field -> field.getCategory() != null).map(field -> {
				field.setCategoryName(field.getCategory().getCategoryName());
				return field.getCategory();
			}).collect(Collectors.toList());
//		List<Category> categories = Lists.newArrayList();
//		fieldList.forEach(field -> {
//			if (field.getCategory() != null) {
//				field.setCategoryName(field.getCategory().getCategoryName());
//				categories.add(field.getCategory());
//			}
//		});
//		return categories;
	}
}