package com.eplugger.commons.beanutils;

import com.eplugger.commons.beanutils.converters.BooleanConverter;

public class ConvertUtilsBean {
	protected static ConvertUtilsBean getInstance() {
		return BeanUtilsBean.getInstance().getConvertUtils();
	}

	private WeakFastHashMap converters = new WeakFastHashMap();

	public ConvertUtilsBean() {
		converters.setFast(false);
		deregister();
		converters.setFast(true);
	}

	public Object convert(String value, Class<?> clazz) {
		Converter converter = lookup(clazz);
		if (converter == null) {
			converter = lookup(String.class);
		}
		return (converter.convert(clazz, value));
	}

	public void deregister() {
		converters.clear();

		registerPrimitives(false);
		registerStandard(false, false);
	}

	private void registerPrimitives(boolean throwException) {
		register(Boolean.TYPE, throwException ? new BooleanConverter() : new BooleanConverter(Boolean.FALSE));
	}

	private void registerStandard(boolean throwException, boolean defaultNull) {
		Boolean booleanDefault = defaultNull ? null : Boolean.FALSE;
		register(Boolean.class, throwException ? new BooleanConverter() : new BooleanConverter(booleanDefault));
	}

	private void register(Class<?> clazz, Converter converter) {
		register(new ConverterFacade(converter), clazz);
	}

	public Converter lookup(Class<?> clazz) {
		return ((Converter) converters.get(clazz));
	}

	public void register(Converter converter, Class<?> clazz) {
		converters.put(clazz, converter);
	}
}
