package com.eplugger.xml.dom4j.entity;

import java.util.ArrayList;
import java.util.List;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Fields {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<Field> fields = new ArrayList<Field>();

	@Override
	public String toString() {
		return "Fields [" + fields.toString() + "]";
	}
}