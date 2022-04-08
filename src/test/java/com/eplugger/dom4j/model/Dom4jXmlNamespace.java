package com.eplugger.dom4j.model;

import com.eplugger.dom4j.annotation.Dom4jFieldXml;

import lombok.Data;

@Data
public class Dom4jXmlNamespace {
	@Dom4jFieldXml()
	private String a;
	@Dom4jFieldXml(name = "x")
	private String b;
}
