//package com.eplugger.onekey.addModule.entity;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.eplugger.onekey.addField.entity.Field;
//
///**
// * <p>模块信息</p>
// * @author Admin
// *
// */
//public class ModuleInfo1 {
//	/** 继承的超类 */
//	private Map<String, String> superClassMap = new HashMap<String, String>();
//	/** 模块名 */
//	private String moduleName;
//	/** 模块中文名 */
//	private String moduleZHName;
//	/** 数据库表名 */
//	private String tableName;
//	/** 默认同模块名 */
//	private String beanId;
//	/** 字段列表 */
//	List<Field> fieldList = new ArrayList<Field>();
//	List<Field> fields = new ArrayList<Field>();
//	List<Field> authorFieldList = new ArrayList<Field>();
//	List<Field> authorFields = new ArrayList<Field>();
//	/** 实现的接口 */
//	private String[] interfaces = null;
//	/** xml配置文件是否配置忽略 */
//	private Boolean ignore;
//	
//	public Map<String, String> getSuperClassMap() {
//		return superClassMap;
//	}
//	public void setSuperClassMap(Map<String, String> superClassMap) {
//		this.superClassMap = superClassMap;
//	}
//	public String getModuleName() {
//		return moduleName;
//	}
//	public void setModuleName(String moduleName) {
//		this.moduleName = moduleName;
//		this.beanId = moduleName;
//	}
//	public String getModuleZHName() {
//		return moduleZHName;
//	}
//	public void setModuleZHName(String moduleZHName) {
//		this.moduleZHName = moduleZHName;
//	}
//	public String getTableName() {
//		return tableName;
//	}
//	public void setTableName(String tableName) {
//		this.tableName = tableName;
//	}
//	public String getBeanId() {
//		return beanId == null ? getModuleName() : beanId;
//	}
//	public void setBeanId(String beanId) {
//		this.beanId = beanId;
//	}
//	public String[] getInterfaces() {
//		return interfaces;
//	}
//	public void setInterfaces(String[] interfaces) {
//		this.interfaces = interfaces;
//	}
//	public List<Field> getFieldList() {
//		return fieldList;
//	}
//	public void setFieldList(List<Field> fieldList) {
//		this.fieldList = fieldList;
//	}
//	public List<Field> getFields() {
//		return fields;
//	}
//	public void setFields(List<Field> fields) {
//		this.fields = fields;
//	}
//	public List<Field> getAuthorFields() {
//		return authorFields;
//	}
//	public void setAuthorFields(List<Field> authorFields) {
//		this.authorFields = authorFields;
//	}
//	public Boolean getIgnore() {
//		return ignore ;
//	}
//	public void setIgnore(Boolean ignore) {
//		this.ignore = ignore;
//	}
//	
//	public ModuleInfo1(Map<String, String> superClassMap, String moduleName, String moduleZHName, String tableName,
//			String beanId, List<Field> fields, 
//			List<Field> authorFields, String[] interfaces, Boolean ignore) {
//		super();
//		this.superClassMap = superClassMap;
//		this.moduleName = moduleName;
//		this.moduleZHName = moduleZHName;
//		this.tableName = tableName;
//		this.beanId = beanId;
//		this.fields = fields;
//		this.authorFields = authorFields;
//		this.interfaces = interfaces;
//		this.ignore = ignore;
//	}
//	public ModuleInfo1(Map<String, String> superClassMap, String moduleName, String moduleZHName, String tableName,
//			String beanId, List<Field> fieldList, List<Field> authorFieldList, String[] interfaces) {
//		super();
//		this.superClassMap = superClassMap;
//		this.moduleName = moduleName;
//		this.moduleZHName = moduleZHName;
//		this.tableName = tableName;
//		this.beanId = beanId;
//		this.fieldList = fieldList;
//		this.authorFieldList = authorFieldList;
//		this.interfaces = interfaces;
//	}
//	public List<Field> getAuthorFieldList() {
//		return authorFieldList;
//	}
//	public void setAuthorFieldList(List<Field> authorFieldList) {
//		this.authorFieldList = authorFieldList;
//	}
//	public ModuleInfo1(Map<String, String> superClassMap, String moduleName, String moduleZHName, String tableName, String beanId, List<Field> fieldList, String[] interfaces) {
//		super();
//		this.superClassMap = superClassMap;
//		this.moduleName = moduleName;
//		this.moduleZHName = moduleZHName;
//		this.tableName = tableName;
//		this.beanId = beanId;
//		this.fieldList = fieldList;
//		this.interfaces = interfaces;
//	}
//	public ModuleInfo1() {
//		super();
//	}
//	@Override
//	public String toString() {
//		return "ModuleInfo [superClassMap=" + superClassMap + ", moduleName=" + moduleName + ", moduleZHName="
//				+ moduleZHName + ", tableName=" + tableName + ", beanId=" + beanId + ", fields=" + fields
//				+ ", authorFields=" + authorFields + ", interfaces=" + Arrays.toString(interfaces) + ", ignore="
//				+ ignore + "]";
//	}
//}
