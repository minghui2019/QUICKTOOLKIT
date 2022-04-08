package com.eplugger.commons.beanutils.converters;

import java.lang.reflect.Array;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;

import com.eplugger.commons.beanutils.Converter;

public abstract class AbstractConverter implements Converter {
	private static final String PACKAGE = "org.apache.commons.beanutils.converters.";
	private boolean useDefault = false;
	private Object defaultValue = null;
	public AbstractConverter() {
    }
	
	public AbstractConverter(Object defaultValue) {
        setDefaultValue(defaultValue);
    }
	
	protected void setDefaultValue(Object defaultValue) {
        useDefault = false;
        if (defaultValue == null) {
           this.defaultValue  = null;
        } else {
           this.defaultValue  = convert(getDefaultType(), defaultValue);
        }
        useDefault = true;
    }
	
	protected abstract Class getDefaultType();
	
	@Override
	public Object convert(Class type, Object value) {
        Class sourceType  = value == null ? null : value.getClass();
        Class targetType  = primitive(type  == null ? getDefaultType() : type);

        value = convertArray(value);

        // Missing Value
        if (value == null) {
            return handleMissing(targetType);
        }

        sourceType = value.getClass();

        try {
            // Convert --> String
            if (targetType.equals(String.class)) {
                return convertToString(value);
            // No conversion necessary
            } else if (targetType.equals(sourceType)) {
                return value;
            // Convert --> Type
            } else {
                Object result = convertToType(targetType, value);
                return result;
            }
        } catch (Throwable t) {
            return handleError(targetType, value, t);
        }

    }
	
	Class primitive(Class type) {
        if (type == null || !type.isPrimitive()) {
            return type;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        } else if (type == Double.TYPE) {
            return Double.class;
        } else if (type == Long.TYPE) {
            return Long.class;
        } else if (type == Boolean.TYPE) {
            return Boolean.class;
        } else if (type == Float.TYPE) {
            return Float.class;
        } else if (type == Short.TYPE) {
            return Short.class;
        } else if (type == Byte.TYPE) {
            return Byte.class;
        } else if (type == Character.TYPE) {
            return Character.class;
        } else {
            return type;
        }
    }
	
	protected Object convertArray(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            if (Array.getLength(value) > 0) {
                return Array.get(value, 0);
            } else {
                return null;
            }
        }
        if (value instanceof Collection) {
            Collection collection = (Collection)value;
            if (collection.size() > 0) {
                return collection.iterator().next();
            } else {
                return null;
            }
        }
        return value;
    }
	
	protected Object handleMissing(Class type) {
        if (useDefault || type.equals(String.class)) {
            Object value = getDefault(type);
            if (useDefault && value != null && !(type.equals(value.getClass()))) {
                try {
                    value = convertToType(type, defaultValue);
                } catch (Throwable t) {
                }
            }
            return value;
        }

        ConversionException cex =  new ConversionException("No value specified for '" + toString(type) + "'");
        throw cex;
    }
	
	protected Object getDefault(Class type) {
        if (type.equals(String.class)) {
            return null;
        } else {
            return defaultValue;
        }
    }
	
	protected abstract Object convertToType(Class type, Object value) throws Throwable;
	
	protected String convertToString(Object value) throws Throwable {
        return value.toString();
    }
	
	protected Object handleError(Class type, Object value, Throwable cause) {
        if (useDefault) {
            return handleMissing(type);
        }

        ConversionException cex = null;
        if (cause instanceof ConversionException) {
            cex = (ConversionException)cause;
        } else {
            String msg = "Error converting from '" + toString(value.getClass()) + "' to '" + toString(type) + "' " + cause.getMessage();
            cex = new ConversionException(msg, cause);
            BeanUtils.initCause(cex, cause);
        }
        throw cex;
    }
	
	public String toString() {
        return toString(getClass()) + "[UseDefault=" + useDefault + "]";
    }
	
	String toString(Class type) {
        String typeName = null;
        if (type == null) {
            typeName = "null";
        } else if (type.isArray()) {
            Class elementType = type.getComponentType();
            int count = 1;
            while (elementType.isArray()) {
                elementType = elementType .getComponentType();
                count++;
            }
            typeName = elementType.getName();
            for (int i = 0; i < count; i++) {
                typeName += "[]";
            }
        } else {
            typeName = type.getName();
        }
        if (typeName.startsWith("java.lang.") ||
            typeName.startsWith("java.util.") ||
            typeName.startsWith("java.math.")) {
            typeName = typeName.substring("java.lang.".length());
        } else if (typeName.startsWith(PACKAGE)) {
            typeName = typeName.substring(PACKAGE.length());
        }
        return typeName;
    }
}
