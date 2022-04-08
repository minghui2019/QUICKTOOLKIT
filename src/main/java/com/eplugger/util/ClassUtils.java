package com.eplugger.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class ClassUtils {
	private static Log log = LogFactory.get();
	
	/**
	 * 利用setter方法设置对象属性值
	 * @param newInstance 对象
	 * @param fieldName 属性名
	 * @param fieldValue 属性值
	 * @param parameterTypes 参数类型
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static final <T> void setProperty(T newInstance, String fieldName, Object fieldValue, Class<?>... parameterTypes) {
		Method method = ClassUtils.getDeclaredMethod(newInstance, "set" + StringUtils.firstCharUpperCase(fieldName), parameterTypes);
		if (method == null) {
			return;
		}
		try {
			method.invoke(newInstance, fieldValue);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			log.error("参数类型不匹配({}), 属性: {} 参数: {} 类型: {}", e.getMessage(), fieldName, fieldValue, parameterTypes);
		}
	}
	
	public static final <T> Object getProperty(T newInstance, String fieldName) {
		Method method = ClassUtils.getDeclaredMethod(newInstance, "get" + StringUtils.firstCharUpperCase(fieldName));
		if (method == null) {
			return null;
		}
		try {
			return method.invoke(newInstance);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static <T> Method getDeclaredMethod(T object, String methodName, Class<?>... parameterTypes) {
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				return clazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				log.error("{}类找不到{}方法, 参数{}", clazz.getSimpleName(), methodName, parameterTypes);
			}
		}
		return null;
	}
}
