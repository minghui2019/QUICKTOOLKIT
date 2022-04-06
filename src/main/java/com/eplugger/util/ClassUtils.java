package com.eplugger.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassUtils {
	/**
	 * 利用setter方法设置对象属性值
	 * @param newInstance 对象
	 * @param fieldName 属性名
	 * @param fieldValue 属性值
	 * @param parameterTypes 参数类型
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static final <T> void setProperty(T newInstance, String fieldName, Object fieldValue, Class<?>... parameterTypes) {
		Class<?> clz = newInstance.getClass();
		Method method = null;
		try {
			method = clz.getMethod("set" + StringUtils.firstCharUpperCase(fieldName), parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		try {
			method.invoke(newInstance, fieldValue);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
