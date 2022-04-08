package com.eplugger.commons.beanutils;

public interface Converter {
	Object convert(Class<?> type, Object value);
}
