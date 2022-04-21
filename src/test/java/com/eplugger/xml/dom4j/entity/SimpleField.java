package com.eplugger.xml.dom4j.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag("Field")
public class SimpleField {
	/** javaBean属性名 */
	@Dom4JField
	private String fieldId;
	/** javaBean中文名 */
	@Dom4JField
	private String fieldName;
	/** javaBean数据类型 */
	@Dom4JField
	private String dataType;
	/** 精度 */
	@Dom4JField
	private Integer precision;
	
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	public SimpleField(String fieldId, String fieldName) {
		this(fieldId, fieldName, "String");
	}
	public SimpleField(String fieldId, String fieldName, String dataType) {
		this(fieldId, fieldName, dataType, null);
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\nSimpleField [");
		if (StringUtils.isNotBlank(fieldId)) {
			sb.append("fieldId=").append(fieldId).append(", ");
		}
		if (StringUtils.isNotBlank(fieldName)) {
			sb.append("fieldName=").append(fieldName).append(", ");
		}
		if (StringUtils.isNotBlank(dataType)) {
			sb.append("dataType=").append(dataType).append(", ");
		}
		sb.append("precision=").append(precision).append(", ");
		return sb.toString();
	}
}