package com.eplugger.xml.dom4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * dom4j解析 - 实体类映射
 * 指明实体类可以被映射为XML文档描述
 * {@link #value() value = "Clazz"} 对应XML文档的节点名&lt;Clazz&gt;&lt;/Clazz&gt;
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Dom4JTag {
    /**
     * <pre>
     * 当指定当前值时在执行反序列化时必须检测目标标签和当前值是否完全一致
     * 如果不一致需要抛出异常
     * </pre>
     * 
     * @return 指定需要映射的标签名称
     */
    String value() default "";
}
