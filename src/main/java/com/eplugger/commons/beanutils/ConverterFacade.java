package com.eplugger.commons.beanutils;

public final class ConverterFacade implements Converter {
    private final Converter converter;

    public ConverterFacade(Converter converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter is missing");
        }
        this.converter = converter;
    }

    @Override
    public Object convert(Class<?> type, Object value) {
        return converter.convert(type, value);
    }

    public String toString() {
        return "ConverterFacade[" + converter.toString() + "]";
    }
}
