package com.eplugger.dom4j.model;

import com.eplugger.dom4j.annotation.Dom4jFieldXml;

import lombok.Data;

@Data
public class Dom4jXmlAttribute {
	private String ks;
	@Dom4jFieldXml(name = "wo")
	private String ws;
}
