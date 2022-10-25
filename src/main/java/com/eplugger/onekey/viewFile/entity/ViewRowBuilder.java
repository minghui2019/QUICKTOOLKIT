package com.eplugger.onekey.viewFile.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

public class ViewRowBuilder {
    private ViewRow viewRow;

    public ViewRowBuilder() {
        viewRow = new ViewRow();
    }

    public ViewRowBuilder(ViewRow viewRow) {
        this.viewRow = viewRow;
    }

    public ViewRowBuilder setLieMing(Cell lieMing) {
        if (lieMing != null) {
            viewRow.setLieMing(lieMing.toString());
        }
        return this;
    }

    public ViewRowBuilder setBieMing(Cell bieMing) {
        if (bieMing != null) {
            viewRow.setBieMing(bieMing.toString());
        }
        return this;
    }

    public ViewRowBuilder setNullValue(Cell nullValue) {
        if (nullValue != null) {
            viewRow.setNullValue(nullValue.toString());
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
        private String lieMing;
        /** 别名 */
        private String bieMing;
        /** 是否为空值 */
        private String nullValue;
        /** 自定义字典 */
        private String ownDictionary;
        /** 关联表加查询列 */
        private String joinTable = null;
        /** 注释 */
        private String note;
    }
}


