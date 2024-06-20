package com.eplugger.onekey.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import top.tobak.common.lang.StringUtils;
import com.eplugger.utils.OtherUtils;
import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;
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
	@Dom4JField(comment = "javaBean属性名")
	private String fieldId;
	@Dom4JField(comment = "javaBean中文名")
	private String fieldName;
	@Dom4JField(comment = "javaBean数据类型")
	private String dataType;
	@Dom4JField(comment = "数据表名")
	private String tableFieldId;
	@Dom4JField(comment = "字典名")
	private String categoryName;
	@Dom4JField(type = Dom4JFieldType.TAG, name = "Category", comment = "字典")
	private Category category;
	@Dom4JField(comment = "精度")
	private int precision;
	@Dom4JField(comment = "级联列")
	private String joinColumn;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "虚拟字段")
	private boolean tranSient = false;
	@Dom4JField(type = Dom4JFieldType.TAG, comment = "虚拟字段的查询条件")
	private AppendSearch appendSearch;
	@Dom4JField(comment = "List的泛型")
	private String genericity;
	@Dom4JField(comment = "排序字段")
	private String orderBy;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "关联关系：多对一，一对多")
	private String association;
	@Dom4JField(comment = "查询关联集合注解")
	private String fetch;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "updatable = false, insertable = false")
	private boolean updateInsert = true;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "忽略导入包，默认导入")
	private boolean ignoreImport = false;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "生成java方法，默认不生成")
	private boolean onlyMeta = false;
	@Dom4JField(comment = "业务过滤类型，否-no，用户属性-userinfo，值-value，字典-dictionary")
	private String businessFilterType = "no";
	@Dom4JField(comment = "状态，启用-use，禁用-unuse")
	private String useState = "use";
	private String id;
	private Integer orders;
	private String eadpDataType;
	private String beanId;

	public String getTableFieldId() {
		return Strings.isNullOrEmpty(tableFieldId) ? StringUtils.lowerCamelCase2UnderScoreCase(fieldId) : tableFieldId;
	}
	
	public Field(String fieldId, String fieldName) {
		this(fieldId, fieldName, OtherUtils.TPYE_STRING);
	}
	public Field(String fieldId, String fieldName, String dataType) {
//		this(fieldName, fieldId, dataType, StringUtils.lowerCamelCase2UnderScoreCase(fieldName), null, 0);
		this(fieldId, fieldName, dataType, StringUtils.lowerCamelCase2UnderScoreCase(fieldId), null, 0, "no");
	}
	public Field(String fieldId, String fieldName, String dataType, int precision) {
		this(fieldId, fieldName, dataType, precision, StringUtils.lowerCamelCase2UnderScoreCase(fieldId));
	}
	public Field(String fieldId, String fieldName, String dataType, boolean tranSient) {
		this(fieldId, fieldName, dataType, null, null, 0, null, tranSient, null, null, null, "no");
	}
	public Field(String fieldId, String fieldName, String dataType, int precision, String tableFieldId) {
		this(fieldId, fieldName, dataType, tableFieldId, null, precision, "no");
	}
	public Field(String fieldId, String fieldName, String dataType, int precision, String tableFieldId, String businessFilterType) {
		this(fieldId, fieldName, dataType, tableFieldId, null, precision, businessFilterType);
	}
	public Field(String fieldId, String fieldName, String dataType, String categoryName, int precision) {
		this(fieldId, fieldName, dataType, StringUtils.lowerCamelCase2UnderScoreCase(fieldId), categoryName, precision, "no");
	}
	public Field(String fieldId, String fieldName, String dataType, String categoryName, int precision, String businessFilterType) {
		this(fieldId, fieldName, dataType, StringUtils.lowerCamelCase2UnderScoreCase(fieldId), categoryName, precision, businessFilterType);
	}
	public Field(String fieldId, String fieldName, String dataType, String tableFieldId, String categoryName, int precision, String businessFilterType) {
		this(fieldId, fieldName, dataType, tableFieldId, categoryName, precision, null, false, null, null, null, businessFilterType);
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
		this(fieldId, fieldName, dataType, StringUtils.lowerCamelCase2UnderScoreCase(fieldId), categoryName, 0, joinColumn, false, null, genericity, null, "no");
	}
	
	/**
	 * @param fieldId 字段名
	 * @param fieldName 字段中文名
	 * @param dataType 数据类型
	 * @param tableFieldId 数据库表字段名
	 * @param categoryName 字典名
	 * @param precision varchar的精度
	 * @param joinColumn 连接字段名称，数据库字段
	 * @param tranSient 是否虚拟字段
	 * @param appendSearch 虚拟字段的查询条件
	 * @param genericity List的泛型，非List不需要
	 * @param association 关联关系：多对一，一对多
	 * @param businessFilterType 业务过滤
	 */
	public Field(String fieldId, String fieldName, String dataType, String tableFieldId, String categoryName, int precision, String joinColumn, boolean tranSient, AppendSearch appendSearch, String genericity, String association, String businessFilterType) {
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
		this.businessFilterType = businessFilterType;
	}

	public Field setUseState(String useState) {
		this.useState = useState;
		return this;
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
			sb.append("onlyMeta=").append(onlyMeta).append("]").append(", ");
		}
		if (!Strings.isNullOrEmpty(businessFilterType)) {
			sb.append("businessFilterType=").append(businessFilterType);
		}
		if (sb.lastIndexOf("]") == -1) {
			sb.replace(sb.length() - 2, sb.length(), "");
			sb.append("]");
		}
		return sb.toString();
	}
}