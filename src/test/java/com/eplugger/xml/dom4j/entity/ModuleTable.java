package com.eplugger.xml.dom4j.entity;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class ModuleTable {
	@Dom4JField
	private String moduleName;
	@Dom4JField
	private String tableName;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private Boolean ignore = true;
	@Dom4JField
	private String described;
	@Override
	public String toString() {
		return "\nModuleTable [moduleName=" + moduleName + ", tableName=" + tableName + ", ignore=" + ignore
				+ ", described=" + described + "]";
	}
}