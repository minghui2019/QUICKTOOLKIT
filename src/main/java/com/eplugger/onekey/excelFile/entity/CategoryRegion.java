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
public class CategoryRegion implements ISqlBizEntity<CategoryRegion> {
    @ExcelProperty(value = "编码", colIndex = 1)
    private String code;
    @ExcelProperty(value = "名称", colIndex = 2)
    private String name;
    @ExcelProperty(value = "关联列", colIndex = 3)
    private String parentCode;
    private String nameLocal;
    private String oldCode;

    public void setCode(String code) {
        this.oldCode = this.code;
        this.code = code;
    }

    @Override
    public void convertCode() {
        if (this.oldCode == null) {
            this.oldCode = this.code;
        }
        this.code = (Strings.isNullOrEmpty(this.parentCode) ? "" : Strings.padStart(Strings.nullToEmpty(this.parentCode), 2, '0'))
            + Strings.padStart(Strings.nullToEmpty(this.code), 2, '0')
            + (Strings.isNullOrEmpty(this.parentCode) ? "00" : "");
        if (this.parentCode != null) {
            this.parentCode = Strings.padStart(Strings.nullToEmpty(this.parentCode), 2, '0') + "00";
        }
    }

    @Override
    public String sql() {
        String sqlTemplate = "INSERT INTO DM_REGION(REGION_ID,REGION_NAME,UP_REGION_ID,REGION_NAME_LOCAL) VALUES(%s,%s,%s,%s);";
        return String.format(sqlTemplate, "'" + this.code + "'", "'" + this.name + "'", this.parentCode == null ? "NULL" : "'" + this.parentCode + "'", "'" + this.nameLocal + "'");
    }

    @Override
    public String updateSql() {
        if (this.parentCode == null) {
            return String.format("update BIZ_ZX_PROJECT set REGION1=%s where REGION1=%s;", "'" + this.code + "'", "'" + this.oldCode + "'");
        }
        return String.format("update BIZ_ZX_PROJECT set REGION2=%s where REGION2=%s;", "'" + this.code + "'", "'" + this.oldCode + "'");
    }

    @Override
    public int compareTo(CategoryRegion o) {
        return this.code.compareTo(o.code);
    }
}
