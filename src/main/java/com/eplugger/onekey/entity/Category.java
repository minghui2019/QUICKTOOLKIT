package com.eplugger.onekey.entity;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Category implements Iterable<CategoryEntry> {
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
    private String id;
    private String eadpDataType;

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
}
