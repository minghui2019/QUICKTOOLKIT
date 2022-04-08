package com.eplugger.commons.beanutils;

import org.apache.commons.beanutils.ContextClassLoaderLocal;

public class BeanUtilsBean {
	private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
		protected Object initialValue() {
			return new BeanUtilsBean();
		}
	};

	public static BeanUtilsBean getInstance() {
		return (BeanUtilsBean) BEANS_BY_CLASSLOADER.get();
	}

	private ConvertUtilsBean convertUtilsBean;

	public BeanUtilsBean() {
		this(new ConvertUtilsBean());
	}

	public BeanUtilsBean(ConvertUtilsBean convertUtilsBean) {
		this.convertUtilsBean = convertUtilsBean;
	}

	public ConvertUtilsBean getConvertUtils() {
		return convertUtilsBean;
	}
}
