package com.eplugger.onekey.entity;

import com.eplugger.common.lang.CustomStringBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

import static top.tobak.common.lang.StringUtils.filterSqlNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class CategoryMapping implements ISqlEntity {
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务表名")
	private String tableName;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "存储代码对应的列名")
	private String codeColumn;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "显示内容对应的列名")
	private String valueColumn;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "显示内容对应的列名翻译")
	private String valueColumnLocal;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "树形关联列")
	private String upCodeColumn;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "级联列")
	private String cascadeColumn;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "排序信息")
	private String orders;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "附加条件")
	private String whereSql;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "关联的表")
	private String relatedTable;
	private String id;
	private String categoryId;
	private String eadpDataType;

	@Override
	public String sql() {
		CustomStringBuilder sql = new CustomStringBuilder();
		sql.append("insert into CFG_CATEGORY_MAPPING(ID,TABLENAME,CODECOLUMN,VALUECOLUMN,UPCODECOLUMN,ORDERS,WHERESQL,CATEGORYID,EADPDATATYPE,RELATEDTABLE,CASCADECOLUMN,VALUE_COLUMN_LOCAL) values(")
			.append(filterSqlNull(this.id)).append(",")
			.append(filterSqlNull(this.tableName)).append(",")
			.append(filterSqlNull(this.codeColumn)).append(",")
			.append(filterSqlNull(this.valueColumn)).append(",")
			.append(filterSqlNull(this.upCodeColumn)).append(",NULL,")
			.append(filterSqlNull(this.whereSql)).append(",")
			.append(filterSqlNull(this.categoryId)).append(",")
			.append(filterSqlNull(this.eadpDataType)).append(",NULL,")
			.append(filterSqlNull(this.cascadeColumn)).append(",")
			.append(filterSqlNull(this.valueColumnLocal)).append(");").appendln();
		return sql.toString();
	}
}