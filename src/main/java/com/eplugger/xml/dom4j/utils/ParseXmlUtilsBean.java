package com.eplugger.xml.dom4j.utils;

import java.util.Map;

import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.onekey.entity.Modules;
import com.eplugger.onekey.viewFile.entity.ModuleViews;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.xml.dom4j.utils.parsers.FieldParser;
import com.eplugger.xml.dom4j.utils.parsers.ModuleParser;
import com.eplugger.xml.dom4j.utils.parsers.ModuleTableParser;
import com.eplugger.xml.dom4j.utils.parsers.ModuleViewParser;
import com.eplugger.xml.dom4j.utils.parsers.SimpleFieldParser;
import com.google.common.collect.Maps;
import org.dom4j.Document;

/**
 * 新导出的Parser需要手动注册于{@link #register(Class, ParserXml)}
 * 使用前一定要注册
 * @author minghui
 *
 */
public class ParseXmlUtilsBean {
	private Map<Class<?>, ParserXml<?>> parsers = Maps.newHashMap();

	protected static ParseXmlUtilsBean getInstance() {
		return BeanUtilsBean.getInstance().getParseUtils();
	}

	public ParseXmlUtilsBean() {
		deregister();
	}
	
	/**
	 * 调用{@link com.eplugger.xml.dom4j.utils.ParserXml#toBean(Class, String) Parser.toBean}进行解析
	 * @param path
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T toBean(String path, Class<T> clazz) throws Exception {
		ParserXml<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.toBean(clazz, path);
	}
	
	/**
	 * 调用{@link com.eplugger.xml.dom4j.utils.ParserXml#fromBean(Object, String, boolean) Parser.fromBean}进行解析
	 * @param outPath
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public <T> Document fromBean(String outPath, T data, boolean isAutoWrite2File) throws Exception {
		Class<? extends Object> clazz = data.getClass();
		ParserXml<T> parser = lookup(clazz);
		if (parser == null) {
			throw new Exception("尚未注册该解析器" + clazz.getSimpleName());
		}
		return parser.fromBean(data, outPath, isAutoWrite2File);
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
		register(Modules.class, new ModuleParser());
		register(ModuleViews.class, new ModuleViewParser());
	}
	
	private void register(Class<?> clazz, ParserXml<?> parser) {
		register(new ParserXmlFacade<>(parser), clazz);
	}

	/**
	 * 公有注册器
	 * @param parser
	 * @param clazz
	 */
	public void register(ParserXml<?> parser, Class<?> clazz) {
		parsers.put(clazz, parser);
	}
	
	@SuppressWarnings("unchecked")
	public <T> ParserXml<T> lookup(Class<?> clazz) {
		return (ParserXml<T>) parsers.get(clazz);
	}
}
