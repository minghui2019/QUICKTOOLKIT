package com.eplugger.xml.dom4j.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import com.eplugger.onekey.utils.SqlUtils;
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
public class Field {
	/** javaBean属性名 */
	@Dom4JField
	private String fieldId;
	/** javaBean中文名 */
	@Dom4JField
	private String fieldName;
	/** javaBean数据类型 */
	@Dom4JField
	private String dataType;
	/** 数据表名 */
	@Dom4JField
	private String tableFieldId;
	/** 字典名 */
	@Dom4JField
	private String categoryName;
	/**  */
	@Dom4JField
	private String joinColumn;
	/** List的泛型 */
	@Dom4JField
	private String genericity;
	/**  */
	@Dom4JField
	private String orderBy;
	/** 关联关系：多对一，一对多 */
	@Dom4JField
	private String association;
	/** 查询关联集合注解 */
	@Dom4JField
	private String fetch;
	/** 虚拟字段 */
	@Dom4JField
	private Boolean tranSient = false;
	/**  */
	@Dom4JField(type = Dom4JFieldType.TAG)
	private AppendSearch appendSearch;
	/** 精度 */
	@Dom4JField
	private Integer precision;
	/** updatable = false, insertable = false */
	@Dom4JField
	private Boolean updateInsert = true;
	/** 忽略导入包，默认导入 */
	@Dom4JField
	private Boolean ignoreImport = false;
	/** 生成java方法，默认不生成 */
	@Dom4JField
	private Boolean onlyMeta = false;
	
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
		this.tableFieldId = SqlUtils.lowerCamelCase2UnderScoreCase(fieldId);
	}
	
	public Field(String fieldId, String fieldName) {
		this(fieldId, fieldName, "String");
	}
	public Field(String fieldId, String fieldName, String dataType) {
		this(fieldId, fieldName, dataType, "", null, null, null, null, null, null, null);
	}
	public Field(String fieldId, String fieldName, String dataType, Integer precision) {
		this(fieldId, fieldName, dataType, "", precision);
	}
	public Field(String fieldId, String fieldName, String dataType, String tableFieldId, Integer precision) {
		this(fieldId, fieldName, dataType, tableFieldId, null, precision, null, null, null, null, null);
	}
	
	/**
	 * @param fieldId 字段名
	 * @param fieldName 字段中文名
	 * @param dataType 数据类型
	 * @param categoryName 字典名
	 * @param joinColumn 连接字段名称，数据库字段
	 * @param genericity List的泛型，非List不需要
	 */
	public Field(String fieldId, String fieldName, String dataType, String categoryName, String joinColumn, String genericity) {
		this(fieldId, fieldName, dataType, "", categoryName, null, joinColumn, null, null, genericity, null);
	}
	
	/**
	 * @param fieldId 字段名
	 * @param fieldName 字段中文名
	 * @param dataType 数据类型
	 * @param tableFieldId
	 * @param categoryName 字典名
	 * @param precision varchar的精度
	 * @param joinColumn 连接字段名称，数据库字段
	 * @param tranSient 是否虚拟字段
	 * @param appendSearch 虚拟字段的查询条件
	 * @param genericity List的泛型，非List不需要
	 * @param association 关联关系：多对一，一对多
	 */
	public Field(String fieldId, String fieldName, String dataType, String tableFieldId, String categoryName, Integer precision, String joinColumn, Boolean tranSient, AppendSearch appendSearch, String genericity, String association) {
		super();
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.dataType = dataType;
		this.tableFieldId = tableFieldId;
		this.categoryName = categoryName;
		this.precision = precision;
		this.joinColumn = joinColumn;
		this.tranSient = tranSient;
		this.appendSearch = appendSearch;
		this.genericity = genericity;
		this.association = association;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\nField [");
		if (!Strings.isNullOrEmpty(fieldId)) {
			sb.append("fieldId=").append(fieldId).append(", ");
		}
		if (!Strings.isNullOrEmpty(fieldName)) {
			sb.append("fieldName=").append(fieldName).append(", ");
		}
		if (!Strings.isNullOrEmpty(dataType)) {
			sb.append("dataType=").append(dataType).append(", ");
		}
		if (!Strings.isNullOrEmpty(tableFieldId)) {
			sb.append("tableFieldId=").append(tableFieldId).append(", ");
		}
		if (!Strings.isNullOrEmpty(categoryName)) {
			sb.append("categoryName=").append(categoryName).append(", ");
		}
		if (!Strings.isNullOrEmpty(joinColumn)) {
			sb.append("joinColumn=").append(joinColumn).append(", ");
		}
		if (!Strings.isNullOrEmpty(genericity)) {
			sb.append("genericity=").append(genericity).append(", ");
		}
		if (!Strings.isNullOrEmpty(orderBy)) {
			sb.append("orderBy=").append(orderBy).append(", ");
		}
		if (!Strings.isNullOrEmpty(association)) {
			sb.append("association=").append(association).append(", ");
		}
		if (!Strings.isNullOrEmpty(fetch)) {
			sb.append("fetch=").append(fetch).append(", ");
		}
		if (tranSient) {
			sb.append("tranSient=").append(tranSient).append(", ");
		}
		if (appendSearch != null) {
			sb.append(appendSearch).append(", ");
		}
		sb.append("precision=").append(precision).append(", ");
		if (!updateInsert) {
			sb.append("updateInsert=").append(updateInsert).append(", ");
		}
		if (ignoreImport) {
			sb.append("ignoreImport=").append(ignoreImport).append(", ");
		}
		if (onlyMeta) {
			sb.append("onlyMeta=").append(onlyMeta).append("]");
		}
		if (sb.lastIndexOf("]") == -1) {
			sb.replace(sb.length() - 2, sb.length(), "");
			sb.append("]");
		}
		return sb.toString();
	}
}
