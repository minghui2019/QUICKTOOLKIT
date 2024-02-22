package com.eplugger.onekey.viewFile.entity;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;
import com.google.common.base.Strings;
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
	@Dom4JField
	private String sheetName;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private boolean ignore = true;
	@Dom4JField
	private String described;
	@Dom4JField
	private String viewName;
	@Dom4JField
	private String checkStatus;

	@Override
	public String toString() {
		return "\nModuleTable [beanId=" + beanId + ", tableName='" + tableName + ", sheetName='" + sheetName + ", viewName='" + viewName + ", checkStatus='" + checkStatus
				+ ", ignore=" + ignore + ", described=" + described + "]";
	}

	public String getSheetName() {
		if (Strings.isNullOrEmpty(sheetName)) {
			return tableName;
		}
		return sheetName;
	}

}
