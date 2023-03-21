package com.eplugger.onekey.utils.entityMeta;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityMetaField {
	private String name;
	private String beanId;
	private String dataType;

	@Override
	public String toString() {
		return "EntityMetaField{" + "name='" + name + '\'' + ", beanId='" + beanId + '\'' + ", dataType='" + dataType + '\'' + '}';
	}
}