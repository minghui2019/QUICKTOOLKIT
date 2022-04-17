package com.eplugger.xml.dom4j.annotation;

/**
 * <pre>
 * dom4j解析 - 字段映射类型
 * <b><i>ATTRIBUTE: </i></b>字段映射为标签属性
 * <b><i>ELEMENT: </i></b>字段为文本内容
 * <b><i>TAG: </i></b>字段映射为子标签
 * </pre>
 */
public enum Dom4JFieldType {
    /**
     * 字段映射为标签属性. 值为复杂属性时属性值为JSON格式
     */
    ATTRIBUTE,

    /**
     * 字段映射为子标签(体)
     */
    TAG,

    /**
     * 字段为文本内容
     */
    ELEMENT
}
