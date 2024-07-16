package com.eplugger.onekey.excelFile.entity;

import com.eplugger.onekey.entity.ISqlBizEntity;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tobak.poi.excel.ExcelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySubType implements ISqlBizEntity<CategorySubType> {
    @ExcelProperty(value = "编码", colIndex = 1)
    private String code;
    @ExcelProperty(value = "名称", colIndex = 2)
    private String name;
    private String nameLocal;
    private String oldCode;

    public void setCode(String code) {
        this.oldCode = this.code;
        this.code = code;
    }

    @Override
    public String getParentCode() {
        return null;
    }

    @Override
    public void convertCode() {
        if (this.oldCode == null) {
            this.oldCode = this.code;
        }
        this.code = Strings.padStart(Strings.nullToEmpty(this.code), 3, '0');
    }

    @Override
    public String sql() {
        String sqlTemplate = "insert into DM_SUB_TYPE(CODE,NAME,NAME_LOCAL) values(%s,%s,%s);";
        return String.format(sqlTemplate, "'" + this.code + "'", "'" + this.name + "'", "'" + this.nameLocal + "'");
    }

    @Override
    public String updateSql() {
        return String.format("update BIZ_ZX_PROJECT set SUB_TYPE=%s where SUB_TYPE=%s;", "'" + this.code + "'", "'" + this.oldCode + "'");
    }

    @Override
    public int compareTo(CategorySubType o) {
        return this.code.compareTo(o.code);
    }
}
