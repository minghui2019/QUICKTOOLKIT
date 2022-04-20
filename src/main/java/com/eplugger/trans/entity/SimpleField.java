package com.eplugger.trans.entity;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Dom4JTag("Field")
public class SimpleField {
	@Dom4JField(comment = "javaBean属性名")
	private String fieldId;
	@Dom4JField(comment = "javaBean中文名")
	private String fieldName;
	@Dom4JField(comment = "javaBean数据类型")
	private String dataType;
	@Dom4JField(comment = "精度")
	private Integer precision;
	
	@Override
	public String toString() {
		return "\nSimpleField [fieldId=" + fieldId + ", fieldName=" + fieldName + ", dataType=" + dataType
				+ ", precision=" + precision + "]";
	}
}