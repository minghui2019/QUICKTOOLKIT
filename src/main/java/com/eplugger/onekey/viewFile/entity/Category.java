package com.eplugger.onekey.viewFile.entity;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Category {
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务名称")
    private String categoryName;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "常量名")
    private String bizName;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务类型")
    private String bizType;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "是否来源于业务表")
    private String fromBiz;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务表名")
    private String tableName;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "代码")
    private String codeColumn;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "内容")
    private String valueColumn;

    @Override
    public String toString() {
        return "Category{" + "categoryName='" + categoryName + '\'' + ", bizName='" + bizName + '\'' + ", bizType='" + bizType + '\'' + ", fromBiz='" + fromBiz + '\'' + ", tableName='" + tableName + '\'' + ", codeColumn='"
            + codeColumn + '\'' + ", valueColumn='" + valueColumn + '\'' + '}';
    }
}
