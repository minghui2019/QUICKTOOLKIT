package com.eplugger.enums;

public enum FromBiz {
    业务表(1), 自定义(0), JAVA代码(2);

    Integer code;

    FromBiz(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}