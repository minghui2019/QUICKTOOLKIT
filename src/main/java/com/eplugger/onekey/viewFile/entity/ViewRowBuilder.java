package com.eplugger.onekey.viewFile.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class ViewRowBuilder {
    private ViewRow viewRow;

    public ViewRowBuilder() {
        viewRow = new ViewRow();
    }

    public ViewRowBuilder(ViewRow viewRow) {
        this.viewRow = viewRow;
    }

    public ViewRowBuilder setColumnName(Cell columnName) {
        if (columnName != null) {
            viewRow.setColumnName(columnName.toString());
        }
        return this;
    }

    public ViewRowBuilder setAlias(Cell alias) {
        if (alias != null) {
            viewRow.setAlias(alias.toString());
        }
        return this;
    }

    public ViewRowBuilder setNullValue(Cell nullValue) {
        if (nullValue != null) {
            if (CellType.NUMERIC == nullValue.getCellType()) {
                viewRow.setNullValue(Integer.toString(new Double(nullValue.getNumericCellValue()).intValue()));
            } else {
                viewRow.setNullValue(nullValue.toString());
            }
        }
        return this;
    }

    public ViewRowBuilder setOwnDictionary(Cell ownDictionary) {
        if (ownDictionary != null) {
            viewRow.setOwnDictionary(ownDictionary.toString());
        }
        return this;
    }

    public ViewRowBuilder setJoinTable(Cell joinTable) {
        if (joinTable != null) {
            viewRow.setJoinTable(joinTable.toString());
        }
        return this;
    }

    public ViewRowBuilder setMeaning(Cell meaning) {
        if (meaning != null) {
            viewRow.setMeaning(meaning.toString());
        }
        return this;
    }

    public ViewRowBuilder setNote(Cell note) {
        if (note != null) {
            viewRow.setNote(note.toString());
        }
        return this;
    }

    public ViewRow getViewRow() {
        return viewRow;
    }

    @Setter
    @Getter
    public class ViewRow {
        /** 列名 */
        private String columnName;
        /** 别名 */
        private String alias;
        /** 是否为空值 */
        private String nullValue;
        /** 自定义字典 */
        private String ownDictionary;
        /** 关联表加查询列 */
        private String joinTable = null;
        /** 中文简称 */
        private String meaning;
        /** 备注 */
        private String note = null;
    }
}


