package com.eplugger.uuid.entity;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag("uuid")
public class UUID {
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private int id;
	@Dom4JField(type = Dom4JFieldType.NONE)
	private String text;
	@Override
	public String toString() {
		return "\nUUID [id=" + id + ", text=" + text + "]";
	}
}
