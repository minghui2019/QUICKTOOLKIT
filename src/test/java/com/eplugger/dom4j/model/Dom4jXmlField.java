package com.eplugger.dom4j.model;

import com.eplugger.dom4j.annotation.Dom4jFieldXml;

import lombok.Data;

@Data
public class Dom4jXmlField {
	@Dom4jFieldXml(name = "huanghe")
	private String hh;
	private String sh;
}
