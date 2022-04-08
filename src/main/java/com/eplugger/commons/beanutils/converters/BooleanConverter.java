package com.eplugger.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;

public final class BooleanConverter extends AbstractConverter {
    public BooleanConverter() {
        super();
    }
    public BooleanConverter(Object defaultValue) {
        super();
        if (defaultValue != NO_DEFAULT) {
            setDefaultValue(defaultValue);
        }
    }
    
    public BooleanConverter(String[] trueStrings, String[] falseStrings) {
        super();
        this.trueStrings = copyStrings(trueStrings);
        this.falseStrings = copyStrings(falseStrings);
    }

    public BooleanConverter(String[] trueStrings, String[] falseStrings, Object defaultValue) {
        super();
        this.trueStrings = copyStrings(trueStrings);
        this.falseStrings = copyStrings(falseStrings);
        if (defaultValue != NO_DEFAULT) {
            setDefaultValue(defaultValue);
        }
    }

    public static final Object NO_DEFAULT = new Object();

    private String[] trueStrings = {"true", "yes", "y", "on", "1"};

    private String[] falseStrings = {"false", "no", "n", "off", "0"};

    protected Class getDefaultType() {
        return Boolean.class;
    }

    protected Object convertToType(Class type, Object value) throws Throwable {
        String stringValue = value.toString().toLowerCase();

        for(int i=0; i<trueStrings.length; ++i) {
            if (trueStrings[i].equals(stringValue)) {
                return Boolean.TRUE;
            }
        }

        for(int i=0; i<falseStrings.length; ++i) {
            if (falseStrings[i].equals(stringValue)) {
                return Boolean.FALSE;
            }
        }
        
        throw new ConversionException("Cna't convert value '" + value + "' to a Boolean");
    }

    private static String[] copyStrings(String[] src) {
        String[] dst = new String[src.length];
        for(int i=0; i<src.length; ++i) {
            dst[i] = src[i].toLowerCase();
        }
        return dst;
    }
}
