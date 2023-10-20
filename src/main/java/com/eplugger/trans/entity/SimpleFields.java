package com.eplugger.trans.entity;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag("Fields")
public class SimpleFields {
	@Dom4JField(type = Dom4JFieldType.TAG, comment = "字段列表")
	private List<SimpleField> fieldList = Lists.newArrayList();

	@Override
	public String toString() {
		return "Fields [" + fieldList.toString() + "]";
	}
}