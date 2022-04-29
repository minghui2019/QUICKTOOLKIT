package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;

public final class ParserFacade<T> implements Parser<T> {
	private final Parser<T> parser;
	
	public ParserFacade(Parser<T> parser) {
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
	public Document fromBean(T data, String path) {
		return parser.fromBean(data, path);
	}
	
	@Override
	public String toString() {
		return "ParserFacade [" + parser.toString() + "]";
	}
}
