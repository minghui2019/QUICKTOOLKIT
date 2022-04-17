package com.eplugger.commons.lang3.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldUtils extends org.apache.commons.lang3.reflect.FieldUtils {
    private static final Set<Class<?>> SIMPLE_TYPES = Sets.newHashSet(Integer.class, Short.class, Long.class,
            Float.class, Double.class, Character.class, Byte.class, Boolean.class, String.class, Date.class);

	public enum CollectionType {
		/**
		 * 数组
		 */
		ARRAY,
		/**
		 * List列表
		 */
		LIST,
		/**
		 * Set集合
		 */
		SET
	}

	/**
	 * 设置值
	 *
	 * @param bean    数据对象
	 * @param field   字段
	 * @param jsonStr json值
	 * @param         <T> 对象类型
	 */
	public static <T> void setJsonValue(T bean, Field field, String jsonStr) throws IllegalAccessException {
		if (!field.isAccessible())
			field.setAccessible(true);

		// 默认为字符串值
		Object val = jsonStr;
		if (null == val)
			return;

		// 非字符串需要JSON解析
		Class<?> type = field.getType();
		if (String.class != type)
			val = JSON.parseObject(jsonStr, type);

		if (null != val)
			field.set(bean, val);
	}

	/**
	 * 校验指定字节码是否为简单类型: int/short/long, float/double, char/byte/boolean, String, Date
	 *
	 * @param cls 字节码
	 * @return true-是简单数据类型, false-复杂数据类型
	 */
	public static boolean isSimpleType(Class<?> cls) {
		return cls.isPrimitive() || SIMPLE_TYPES.contains(cls);
	}

	/**
	 * 校验指定字节码是否为集合类型: List, Set, T[]
	 *
	 * @param cls 字节码
	 * @return 如果是集合类型返回true, 否则返回false
	 */
	public static CollectionType isCollection(Class<?> cls) {
		if (cls.isArray())
			return CollectionType.ARRAY;

		if (List.class.isAssignableFrom(cls))
			return CollectionType.LIST;

		if (Set.class.isAssignableFrom(cls))
			return CollectionType.SET;

		return null;
	}

	/**
	 * 校验指定字段是否静态或者常量
	 *
	 * @param field 目标字段
	 * @return 静态或常量返回true, 否则返回false
	 */
	public static boolean isStaticOrFinal(Field field) {
		int modifiers = field.getModifiers();
		return Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers);
	}

	/**
	 * 获取实体bean的属性field的值
	 * @param bean 实体
	 * @param field 字段
	 * @return
	 */
    public static <T> Object getFieldValue(T bean, Field field) {
        field.setAccessible(true);
        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
            log.error("实体类[" + bean.getClass().getSimpleName() + "]取属性值[" + field.getName() + "]失败");
            throw new RuntimeException("实体类[" + bean.getClass().getSimpleName() + "]取属性值[" + field.getName() + "]失败", e.getCause());
        }
    }

    /**
     * 设置实体bean的属性field的值obj
     * @param bean
     * @param field
     * @param obj
     */
    public static void setFieldValue(Object bean, Field field, Object obj) {
        field.setAccessible(true);
        try {
            field.set(bean, obj);
        } catch (IllegalAccessException e) {
            log.error("实体类[" + bean.getClass().getSimpleName() + "]设置属性值[" + field.getName() + "]失败");
            throw new RuntimeException("实体类[" + bean.getClass().getSimpleName() + "]设置属性值[" + field.getName() + "]失败", e.getCause());
        }
    }
}
