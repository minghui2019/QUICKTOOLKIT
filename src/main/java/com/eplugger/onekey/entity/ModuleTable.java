package com.eplugger.onekey.entity;

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
public class ModuleTable {
	@Dom4JField
	private String moduleName;
	@Dom4JField
	private String tableName;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private boolean ignore = true;
	@Dom4JField
	private String described;
	
	@Override
	public String toString() {
		return "\nModuleTable [moduleName=" + moduleName + ", tableName=" + tableName + ", ignore=" + ignore
				+ ", described=" + described + "]";
	}

	public ModuleTable(String moduleName) {
		this.moduleName = moduleName;
	}

	public ModuleTable(String moduleName, String tableName, String described) {
		this(moduleName, tableName, false, described);
	}
}
