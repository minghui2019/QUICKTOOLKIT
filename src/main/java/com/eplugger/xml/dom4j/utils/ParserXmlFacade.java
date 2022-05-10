package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

public final class ParserXmlFacade<T> implements ParserXml<T> {
	private final ParserXml<T> parser;
	
	public ParserXmlFacade(ParserXml<T> parser) {
		if (parser == null) {
            throw new IllegalArgumentException("Parser is missing");
        }
		this.parser = parser;
	}

	@Override
	public T toBean(Class<T> cls, String path) {
		return parser.toBean(cls, path);
	}
	
	@Override
	public Document fromBean(T data, String outPath, boolean isAutoWrite2File) {
		return parser.fromBean(data, outPath, isAutoWrite2File);
	}
	
	@Override
	public String toString() {
		return "ParserFacade [" + parser.toString() + "]";
	}
}
