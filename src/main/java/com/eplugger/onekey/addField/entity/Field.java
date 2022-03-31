package com.eplugger.onekey.addField.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import com.eplugger.annotation.Booleaner;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.util.OtherUtils;
import com.eplugger.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
	/** javaBean属性名 */
	private String fieldId;
	/** javaBean中文名 */
	private String fieldName;
	/** javaBean数据类型 */
	private String dataType;
	/** 数据表名 */
	private String tableFieldId;
	/** 字典名 */
	private String categoryName;
	/**  */
	private String joinColumn;
	/** List的泛型 */
	private String genericity;
	/**  */
	private String orderBy;
	/** 关联关系：多对一，一对多 */
	private String association;
	/** 查询关联集合注解 */
	private String fetch;
	/** 虚拟字段 */
	@Booleaner
	private Boolean tranSient = false;
	/**  */
	private AppendSearch appendSearch;
	/** 精度 */
	private Integer precision;
	/** updatable = false, insertable = false */
	@Booleaner
	private Boolean updateInsert = true;
	/** 忽略导入包，默认导入 */
	@Booleaner
	private Boolean ignoreImport = false;
	/** 生成java方法，默认不生成 */
	@Booleaner
	private Boolean onlyMeta = false;
	
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
		this.tableFieldId = SqlUtils.lowerCamelCase2UnderScoreCase(fieldId);
	}
	
	public Field(String fieldId, String fieldName) {
		this(fieldId, fieldName, OtherUtils.TPYE_STRING);
	}
	public Field(String fieldId, String fieldName, String dataType) {
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.dataType = dataType;
		this.tableFieldId = SqlUtils.lowerCamelCase2UnderScoreCase(fieldId);
	}
	public Field(String fieldId, String fieldName, String dataType, String categoryName) {
		this(fieldId, fieldName, dataType);
		this.categoryName = categoryName;
	}
	public Field(String fieldId, String fieldName, String dataType, Integer precision) {
		this(fieldId, fieldName, dataType);
		this.precision = precision;
	}
	public Field(String fieldId, String fieldName, String dataType, String tableFieldId, Integer precision) {
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.dataType = dataType;
		this.tableFieldId = tableFieldId;
		this.precision = precision;
	}
	
	public Field(String fieldId, String fieldName, String dataType, Integer precision, Boolean tranSient) {
		this(fieldId, fieldName, dataType);
		this.tranSient = tranSient;
		this.precision = precision;
	}
	/**
	 * 
	 * @param fieldId
	 * @param fieldName
	 * @param dataType
	 * @param precision
	 * @param categoryName
	 */
	public Field(String fieldId, String fieldName, String dataType, Integer precision, String categoryName) {
		this(fieldId, fieldName, dataType);
		this.precision = precision;
		this.categoryName = categoryName;
	}
	public Field(String fieldId, String fieldName, String dataType, String joinColumn, Boolean tranSient, AppendSearch appendSearch) {
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.dataType = dataType;
		this.joinColumn = joinColumn;
		this.tranSient = tranSient;
		this.appendSearch = appendSearch;
	}
	/**
	 * 未使用
	 * @param fieldId
	 * @param fieldName
	 * @param dataType
	 * @param joinColumn
	 * @param genericity
	 */
	public Field(String fieldId, String fieldName, String dataType, String joinColumn, String genericity) {
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.dataType = dataType;
		this.joinColumn = joinColumn;
		this.genericity = genericity;
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
		this(fieldId, fieldName, dataType);
		this.categoryName = categoryName;
		this.joinColumn = joinColumn;
		this.genericity = genericity;
	}
	public Field(String fieldId, String fieldName, String dataType, String categoryName, String joinColumn, String genericity, String association) {
		this(fieldId, fieldName, dataType);
		this.categoryName = categoryName;
		this.joinColumn = joinColumn;
		this.genericity = genericity;
		this.association = association;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Field [");
		if (StringUtils.isNotBlank(fieldId)) {
			sb.append("fieldId=").append(fieldId).append(", ");
		}
		if (StringUtils.isNotBlank(fieldName)) {
			sb.append("fieldName=").append(fieldName).append(", ");
		}
		if (StringUtils.isNotBlank(dataType)) {
			sb.append("dataType=").append(dataType).append(", ");
		}
		if (StringUtils.isNotBlank(tableFieldId)) {
			sb.append("tableFieldId=").append(tableFieldId).append(", ");
		}
		if (StringUtils.isNotBlank(categoryName)) {
			sb.append("categoryName=").append(categoryName).append(", ");
		}
		if (StringUtils.isNotBlank(joinColumn)) {
			sb.append("joinColumn=").append(joinColumn).append(", ");
		}
		if (StringUtils.isNotBlank(genericity)) {
			sb.append("genericity=").append(genericity).append(", ");
		}
		if (StringUtils.isNotBlank(orderBy)) {
			sb.append("orderBy=").append(orderBy).append(", ");
		}
		if (StringUtils.isNotBlank(association)) {
			sb.append("association=").append(association).append(", ");
		}
		if (StringUtils.isNotBlank(fetch)) {
			sb.append("fetch=").append(fetch).append(", ");
		}
		sb.append("tranSient=").append(tranSient).append(", ");
		if (appendSearch != null) {
			sb.append("appendSearch=").append(appendSearch).append(", ");
		}
		sb.append("precision=").append(precision).append(", ");
		sb.append("updateInsert=").append(updateInsert).append(", ");
		sb.append("ignoreImport=").append(ignoreImport).append(", ");
		sb.append("onlyMeta=").append(onlyMeta).append("]");
		return sb.toString();
	}
}