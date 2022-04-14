package com.eplugger.xml.dom4j.utils;

import java.util.List;

public final class ParserFacade<T> implements Parser<T> {
	private final Parser<T> parser;
	
	public ParserFacade(Parser<T> parser) {
		if (parser == null) {
            throw new IllegalArgumentException("Parser is missing");
        }
		this.parser = parser;
	}

	@Override
	public List<T> parse(String path) {
		return parser.parse(path);
	}
	
	@Override
	public List<T> parseValidList(List<T> list) {
		return parser.parseValidList(list);
	}
	
	@Override
	public String toString() {
		return "ParserFacade [" + parser.toString() + "]";
	}
}
