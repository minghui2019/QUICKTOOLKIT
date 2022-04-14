package com.eplugger.xml.dom4j.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eplugger.onekey.addField.entity.ModuleTable;
import com.eplugger.xml.dom4j.utils.parsers.ModuleTableParser;

public class ParseUtilsBean {
	private Map<Class<?>, Parser<?>> parsers = new HashMap<>();

	protected static ParseUtilsBean getInstance() {
		return BeanUtilsBean.getInstance().getParseUtils();
	}

	public ParseUtilsBean() {
		deregister();
	}
	
	public <T> List<T> parse(String path, Class<T> clazz) throws Exception {
		Parser<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.parse(path);
	}
	
	public <T> List<T> parseValidList(String path, Class<T> clazz) throws Exception {
		Parser<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.parseValidList(parser.parse(path));
	}

	public void deregister() {
		parsers.clear();
		registerPrimitives();
	}
	
	private void registerPrimitives() {
		register(ModuleTable.class, new ModuleTableParser());
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
