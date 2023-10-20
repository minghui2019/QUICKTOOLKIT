package com.eplugger.onekey.entity;

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
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "常量名")
    private String name;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务名称")
    private String note;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "业务类型")
    private String type;
    @Dom4JField(type = Dom4JFieldType.NONE, comment = "内容")
    private String value;

    @Override
    public String toString() {
        return "Category{" +
            "name='" + name + '\'' +
            ", note='" + note + '\'' +
            ", type='" + type + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
