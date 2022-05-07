package com.eplugger.onekey.addField.entity;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
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
public class AppendSearch {
	@Dom4JField
	private String value;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private String relativeField;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private String relativeThisProperty = "id";
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private String searchValLabel = "val";
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private boolean mergeMultiVals = false;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private String symbol = ",";
	
	public AppendSearch(String value, String relativeField) {
		this.value = value;
		this.relativeField = relativeField;
	}
	public AppendSearch(String value, String relativeField, String relativeThisProperty) {
		this(value, relativeField);
		this.relativeThisProperty = relativeThisProperty;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("@AppendSearch(");
		sb.append("value=\"").append(value).append("\", ");
		sb.append("relativeField=\"").append(relativeField).append("\", ");
		if (!Strings.isNullOrEmpty(relativeThisProperty) && !"id".equals(relativeThisProperty)) {
			sb.append("relativeThisProperty=\"").append(relativeThisProperty).append("\", ");
		}
		if (mergeMultiVals) {
			sb.append("mergeMultiVals=").append(mergeMultiVals).append(", ");
		}
		if (!Strings.isNullOrEmpty(symbol) && !",".equals(symbol)) {
			sb.append("symbol=\"").append(symbol).append("\", ");
		}
		sb.replace(sb.length() - 2, sb.length(), "").append(")");
		return sb.toString();
	}
}
