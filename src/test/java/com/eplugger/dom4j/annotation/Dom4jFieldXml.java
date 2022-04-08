package com.eplugger.dom4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>name() default ""
 * <pre>attribute() default void.class
 * @author minghui
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dom4jFieldXml {
	String name() default "";
	Class<?> attribute() default void.class;
}
