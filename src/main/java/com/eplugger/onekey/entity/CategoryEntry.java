package com.eplugger.onekey.entity;

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
public class CategoryEntry {
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "字典代码")
    private String code;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "常量值")
    private String name;

    private String id;
    private String categoryId;
    private String eadpDataType;
    private Integer orders;
    private String canCfg = "0"; // 用户可配置

    @Override
    public String toString() {
        return "CategoryEntry{"
                   + "orders=" + (orders == null ? "" : orders)
                   + ", code='" + Strings.nullToEmpty(code) + '\''
                   + ", name=\'" + Strings.nullToEmpty(name) + "\'" + '}';
    }
}
