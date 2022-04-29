package com.eplugger.xml.dom4j.utils;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;

import com.eplugger.onekey.addField.entity.Fields;
import com.eplugger.onekey.addField.entity.ModuleTables;
import com.eplugger.xml.dom4j.utils.parsers.FieldParser;
import com.eplugger.xml.dom4j.utils.parsers.ModuleTableParser;

public class ParseUtilsBean {
	private Map<Class<?>, Parser<?>> parsers = new HashMap<>();

	protected static ParseUtilsBean getInstance() {
		return BeanUtilsBean.getInstance().getParseUtils();
	}

	public ParseUtilsBean() {
		deregister();
	}
	
	public <T> T toBean(String path, Class<T> clazz) throws Exception {
		Parser<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.toBean(clazz, path);
	}
	
	public <T> Document fromBean(String path, T data) throws Exception {
		Class<? extends Object> clazz = data.getClass();
		Parser<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.fromBean(data, path);
	}

	public void deregister() {
		parsers.clear();
		registerPrimitives();
	}
	
	private void registerPrimitives() {
		register(ModuleTables.class, new ModuleTableParser());
		register(Fields.class, new FieldParser());
	}
	
	private void register(Class<?> clazz, Parser<?> parser) {
		register(new ParserFacade<>(parser), clazz);
	}

	public void register(Parser<?> parser, Class<?> clazz) {
		parsers.put(clazz, parser);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Parser<T> lookup(Class<?> clazz) {
		return (Parser<T>) parsers.get(clazz);
	}
}
