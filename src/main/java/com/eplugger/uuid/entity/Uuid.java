package com.eplugger.uuid.entity;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag("uuid")
public class Uuid {
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private int id;
	@Dom4JField(type = Dom4JFieldType.NONE)
	private String text;
	@Override
	public String toString() {
		return "\nUuid [id=" + id + ", text=" + text + "]";
	}
}
