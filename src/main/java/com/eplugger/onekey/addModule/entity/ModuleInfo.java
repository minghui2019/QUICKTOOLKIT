package com.eplugger.onekey.addModule.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.onekey.addField.entity.Field;
import com.util.ArrayList;

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
public class ModuleInfo {
	/** 继承的超类 */
	private Map<String, String> superClassMap = new HashMap<String, String>();
	/** 模块名 */
	private String moduleName;
	/** 模块中文名 */
	private String moduleZHName;
	/** 数据库表名 */
	private String tableName;
	/** 默认同模块名 */
	private String beanId;
	/** 字段列表 */
	List<Field> fields = new ArrayList<Field>();
	/** 实现的接口 */
	private String[] interfaces = null;
	/** 包名 */
	private String packageName;
	
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
		if (fields.size() != 0) {
			sb.append("fields=").append(fields).append(", \n");
		}
		if (interfaces != null) {
			sb.append("interfaces=").append(Arrays.toString(interfaces)).append(", \n");
		}
		if (StringUtils.isNotBlank(packageName)) {
			sb.append("packageName=").append(packageName).append(", \n");
		}
		return sb.substring(0, sb.length() - 3) + "]";
	}
}
