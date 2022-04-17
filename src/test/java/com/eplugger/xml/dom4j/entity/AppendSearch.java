package com.eplugger.xml.dom4j.entity;

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
	private Boolean mergeMultiVals = false;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE)
	private String symbol = ",";
	
	public AppendSearch(String value, String relativeField) {
		super();
		this.value = value;
		this.relativeField = relativeField;
	}
	public AppendSearch(String value, String relativeField, String relativeThisProperty) {
		this(value, relativeField);
		this.relativeThisProperty = relativeThisProperty;
	}
	@Override
	public String toString() {
		return "AppendSearch [value=" + value + ", relativeField=" + relativeField + ", relativeThisProperty="
				+ relativeThisProperty + ", searchValLabel=" + searchValLabel + ", mergeMultiVals=" + mergeMultiVals
				+ ", symbol=" + symbol + "]";
	}
}
