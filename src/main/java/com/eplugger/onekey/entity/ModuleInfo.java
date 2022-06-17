package com.eplugger.onekey.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>模块信息</p>
 * @author Admin
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class ModuleInfo {
	@Dom4JField(type = Dom4JFieldType.TAG, name = "superClassMap", comment = "继承的超类")
	private Map<String, String> superClassMap = Maps.newHashMap();
	@Dom4JField(comment = "模块名")
	private String moduleName;
	@Dom4JField(comment = "模块中文名")
	private String moduleZHName;
	@Dom4JField(comment = "数据库表名")
	private String tableName;
	@Dom4JField(comment = "beanId, 默认同模块名")
	private String beanId;
	@Dom4JField(type = Dom4JFieldType.TAG, comment = "字段列表")
	Fields fields;
//	List<Field> fields = Lists.newArrayList();
	@Dom4JField(type = Dom4JFieldType.TAG, name = "interfaces", comment = "实现的接口")
	private String[] interfaces = null;
	@Dom4JField(comment = "包名")
	private String packageName;
	List<Field> fieldList = Lists.newArrayList();
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ModuleInfo [");
		if (superClassMap.size() != 0) {
			sb.append("superClassMap=").append(superClassMap).append(", \n");
		}
		if (StringUtils.isNotBlank(moduleName)) {
			sb.append("moduleName=").append(moduleName).append(", \n");
		}
		if (StringUtils.isNotBlank(moduleZHName)) {
			sb.append("moduleZHName=").append(moduleZHName).append(", \n");
		}
		if (StringUtils.isNotBlank(tableName)) {
			sb.append("tableName=").append(tableName).append(", \n");
		}
		if (StringUtils.isNotBlank(beanId)) {
			sb.append("beanId=").append(beanId).append(", \n");
		}
		if (fields != null) {
			sb.append("fields=").append(fields.getFieldList()).append(", \n");
		}
		if (interfaces != null) {
			sb.append("interfaces=").append(Arrays.toString(interfaces)).append(", \n");
		}
		if (StringUtils.isNotBlank(packageName)) {
			sb.append("packageName=").append(packageName).append(", \n");
		}
		return sb.substring(0, sb.length() - 3) + "]";
	}


	public List<Field> getFieldList() {
		return fields.getFieldList();
	}
}
