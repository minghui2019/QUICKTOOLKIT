package com.eplugger.xml.dom4j.utils;

import org.apache.commons.beanutils.ContextClassLoaderLocal;

public class BeanUtilsBean {
	private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
		@Override
		protected Object initialValue() {
			return new BeanUtilsBean();
		}
	};

	public static BeanUtilsBean getInstance() {
		return (BeanUtilsBean) BEANS_BY_CLASSLOADER.get();
	}
	
	private ParseUtilsBean parseUtilsBean;
	
	public BeanUtilsBean() {
		this(new ParseUtilsBean());
	}

	public BeanUtilsBean(ParseUtilsBean parseUtilsBean) {
		this.parseUtilsBean = parseUtilsBean;
	}

	public ParseUtilsBean getParseUtils() {
		return parseUtilsBean;
	}
}
