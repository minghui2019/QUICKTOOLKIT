package com.eplugger.onekey.viewFile.entity;

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
public class ModuleView {
	@Dom4JField
	private String beanId;
	@Dom4JField
	private String tableName;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private boolean ignore = true;
	@Dom4JField
	private String described;
	@Dom4JField
	private String viewName;

	@Override
	public String toString() {
		return "\nModuleTable [beanId=" + beanId + ", tableName='" + tableName + ", viewName='" + viewName
				+ ", ignore=" + ignore + ", described=" + described + "]";
	}

	public ModuleView(String beanId, String tableName, String described, String viewName) {
		this(beanId, tableName, false, described, viewName);
	}
}
