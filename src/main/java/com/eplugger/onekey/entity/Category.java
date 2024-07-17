package com.eplugger.onekey.entity;

import java.util.Iterator;
import java.util.List;

import com.eplugger.common.lang.CustomStringBuilder;
import com.eplugger.enums.FromBiz;
import com.google.common.base.Strings;
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
public class Category implements Iterable<CategoryEntry>, ISqlEntity {
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "常量名")
    private String categoryName;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务名称")
    private String bizName;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务类型")
    private String bizType;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "是否来源于业务表")
    private Integer fromBiz;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务表名")
    private String tableName;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "代码")
    private String codeColumn;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "内容")
    private String valueColumn;
    @Dom4JField(type = Dom4JFieldType.TAG, name = "CategoryEntries", path = "CategoryEntry", comment = "字典常量表")
    private List<CategoryEntry> entries;
    @Dom4JField(type = Dom4JFieldType.TAG, name = "CategoryMapping", comment = "业务常量映射信息")
    private CategoryMapping categoryMapping;
    private String id;
    private String eadpDataType;

    public Category(String categoryName, String bizName, String bizType, Integer fromBiz) {
        this.categoryName = categoryName;
        this.bizName = bizName;
        this.bizType = bizType;
        this.fromBiz = fromBiz;
    }

    @Override
    public String toString() {
        return "Category{"
            + "categoryName='" + categoryName + '\''
            + ", bizName='" + bizName + '\''
            + ", bizType='" + bizType + '\''
            + ", fromBiz='" + fromBiz + '\''
            + (Strings.isNullOrEmpty(tableName) ? "" : ", tableName='" + tableName + '\'')
            + (Strings.isNullOrEmpty(codeColumn) ? "" : ", codeColumn='" + codeColumn + '\'')
            + (Strings.isNullOrEmpty(codeColumn) ? "" : ", valueColumn='" + codeColumn + '\'')
            + ", entries='" + entries + '\''
            + '}';
    }

    @Override
    public Iterator<CategoryEntry> iterator() {
        return entries.iterator();
    }

    @Override
    public String sql() {
        CustomStringBuilder sql = new CustomStringBuilder();
        sql.append("-- 【字典】").append(this.bizName).append(" (").append(this.categoryName).append(")").appendln();
        if (FromBiz.业务表.code() == this.fromBiz) {
            sql.append("delete from CFG_CATEGORY_MAPPING WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY WHERE CATEGORYNAME=").append(filterSqlNull(this.categoryName)).append(");").appendln();
        } else {
            sql.append("delete from CFG_CATEGORY_ENTRY WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY WHERE CATEGORYNAME=").append(filterSqlNull(this.categoryName)).append(");").appendln();
        }
        sql.append("delete from CFG_CATEGORY WHERE CATEGORYNAME=").append(filterSqlNull(this.categoryName)).append(";").appendln();
        sql.appendln("-- 表[CFG_CATEGORY]的数据如下:");
        sql.append("insert into CFG_CATEGORY(ID,BIZNAME,CATEGORYNAME,BIZTYPE,FROMBIZ,CANCFG,EADPDATATYPE,BZ,FROMJAVA) values(")
            .append(filterSqlNull(this.id)).append(",")
            .append(filterSqlNull(this.bizName)).append(",")
            .append(filterSqlNull(this.categoryName)).append(",")
            .append(filterSqlNull(this.bizType)).append(",")
            .append(filterSqlNull(this.fromBiz)).append(",'0',")
            .append(filterSqlNull(this.eadpDataType)).append(",NULL,NULL);").appendln();
        if (FromBiz.业务表.code() == this.fromBiz) {
            sql.appendln("-- 表[CFG_CATEGORY_MAPPING]的数据如下:");
            if (categoryMapping != null) {
                sql.appendln(categoryMapping.sql());
            }
        } else {
            sql.appendln("-- 表[CFG_CATEGORY_ENTRY]的数据如下:");
            for (CategoryEntry entry : entries) {
                sql.append(entry.sql());
            }
        }
        return sql.toString();
    }
}
