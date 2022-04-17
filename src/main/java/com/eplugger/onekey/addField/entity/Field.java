package com.eplugger.onekey.addField.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import com.eplugger.annotation.Booleaner;
import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.util.OtherUtils;

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
	/** 精度 */
	private Integer precision;
	/**  */
	private String joinColumn;
	/** 虚拟字段 */
	@Booleaner
	private Boolean tranSient = false;
	/**  */
	private AppendSearch appendSearch;
	/** List的泛型 */
	private String genericity;
	/**  */
	private String orderBy;
	/** 关联关系：多对一，一对多 */
	private String association;
	/** 查询关联集合注解 */
	private String fetch;
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
		this(fieldId, fieldName, dataType, SqlUtils.lowerCamelCase2UnderScoreCase(fieldId), null, null, null, null, null, null, null);
	}
	public Field(String fieldId, String fieldName, String dataType, Integer precision) {
		this(fieldId, fieldName, dataType, SqlUtils.lowerCamelCase2UnderScoreCase(fieldId), precision);
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
		this(fieldId, fieldName, dataType, SqlUtils.lowerCamelCase2UnderScoreCase(fieldId), categoryName, null, joinColumn, null, null, genericity, null);
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