package com.eplugger.onekey.entity;

public interface ISqlBizEntity<T> extends ISqlEntity, Comparable<T> {
    String getCode();

    void setCode(String code);

    String getName();

    String getParentCode();

    void setNameLocal(String nameLocal);

    void convertCode();

    String updateSql();
}
