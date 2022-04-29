package com.eplugger.xml.dom4j.utils;

import java.util.Map;

import org.dom4j.Document;

import com.eplugger.onekey.addField.entity.Fields;
import com.eplugger.onekey.addField.entity.ModuleTables;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.xml.dom4j.utils.parsers.FieldParser;
import com.eplugger.xml.dom4j.utils.parsers.ModuleTableParser;
import com.eplugger.xml.dom4j.utils.parsers.SimpleFieldParser;
import com.google.common.collect.Maps;

/**
 * 新导出的Parser需要手动注册于{@link #register(Class, Parser)}
 * 使用前一定要注册
 * @author Admin
 *
 */
public class ParseUtilsBean {
	private Map<Class<?>, Parser<?>> parsers = Maps.newHashMap();

	protected static ParseUtilsBean getInstance() {
		return BeanUtilsBean.getInstance().getParseUtils();
	}

	public ParseUtilsBean() {
		deregister();
	}
	
	/**
	 * 调用{@link com.eplugger.xml.dom4j.utils.Parser#toBean(Class, Parser) Parser.toBean}进行解析
	 * @param path
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T toBean(String path, Class<T> clazz) throws Exception {
		Parser<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.toBean(clazz, path);
	}
	
	/**
	 * 调用{@link com.eplugger.xml.dom4j.utils.Parser#fromBean(T, Parser) Parser.fromBean}进行解析
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * 私有注册器
	 */
	private void registerPrimitives() {
		register(ModuleTables.class, new ModuleTableParser());
		register(Fields.class, new FieldParser());
		register(SimpleFields.class, new SimpleFieldParser());
	}
	
	private void register(Class<?> clazz, Parser<?> parser) {
		register(new ParserFacade<>(parser), clazz);
	}

	/**
	 * 公有注册器
	 * @param parser
	 * @param clazz
	 */
	public void register(Parser<?> parser, Class<?> clazz) {
		parsers.put(clazz, parser);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Parser<T> lookup(Class<?> clazz) {
		return (Parser<T>) parsers.get(clazz);
	}
}
