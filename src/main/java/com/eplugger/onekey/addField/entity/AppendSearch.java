package com.eplugger.onekey.addField.entity;

import com.eplugger.annotation.Booleaner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppendSearch {
	private String value;
	private String relativeField;
	private String relativeThisProperty = "id";
	private String searchValLabel = "val";
	@Booleaner
	private Boolean mergeMultiVals = false;
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
