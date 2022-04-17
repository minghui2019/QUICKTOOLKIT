package com.eplugger.xml.dom4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * dom4j解析 - 属性映射
 * 实体属性可以映射为XML子标签或者属性
 * 默认映射文本值, 且属性名与字段名一致
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dom4JField {
    /**
     * <pre>
     * 获取被标记属性XML映射属性名或子标签名
     * 默认: 字段名
     * </pre>
     */
    String name() default "";

    /**
     * <pre>
     * 字段映射类型
     * 默认: 文本值
     * </pre>
     */
    Dom4JFieldType type() default Dom4JFieldType.ELEMENT;

    /**
     * <pre>
     * 设置路径会在解析 {@link #name() name} 之前跳转到指定子标签
     * </pre>
     */
    String[] path() default {};
}
