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
	
	private ParseXmlUtilsBean parseUtilsBean;
	
	public BeanUtilsBean() {
		this(new ParseXmlUtilsBean());
	}

	public BeanUtilsBean(ParseXmlUtilsBean parseUtilsBean) {
		this.parseUtilsBean = parseUtilsBean;
	}

	public ParseXmlUtilsBean getParseUtils() {
		return parseUtilsBean;
	}
}
