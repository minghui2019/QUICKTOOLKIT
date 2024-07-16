package com.eplugger.onekey.entity;

import com.eplugger.common.lang.CustomStringBuilder;
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
public class CategoryEntry implements ISqlEntity {
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "字典代码")
    private String code;
    @Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "常量值")
    private String name;

    private String id;
    private String categoryId;
    private String eadpDataType;
    private Integer orders;
    private String canCfg = "0"; // 用户可配置
    private String nameLocal;
    private String parentCode;
    private String cascadeCode;

    @Override
    public String toString() {
        return "CategoryEntry{"
                   + "orders=" + (orders == null ? "" : orders)
                   + ", code='" + Strings.nullToEmpty(code) + '\''
                   + ", name=\'" + Strings.nullToEmpty(name) + "\'" + '}';
    }

    @Override
    public String sql() {
        CustomStringBuilder sql = new CustomStringBuilder();
        sql.append("insert into CFG_CATEGORY_ENTRY(ID,CODE,REMARK,CATEGORYID,PARENT_CODE,EADPDATATYPE,ORDERS,CANCFG,CASCADECODE,NAME,NAME_LOCAL) values(")
            .append(filterSqlNull(this.id)).append(",")
            .append(filterSqlNull(this.code)).append(",NULL,")
            .append(filterSqlNull(this.categoryId)).append(",")
            .append(filterSqlNull(this.parentCode)).append(",")
            .append(filterSqlNull(this.eadpDataType)).append(",NULL,")
            .append(filterSqlNull(this.orders)).append(",")
            .append(filterSqlNull(this.canCfg)).append(",")
            .append(filterSqlNull(this.cascadeCode)).append(",")
            .append(filterSqlNull(this.name)).append(",")
            .append(filterSqlNull(this.nameLocal)).append(");").appendln();
        return sql.toString();
    }
}
