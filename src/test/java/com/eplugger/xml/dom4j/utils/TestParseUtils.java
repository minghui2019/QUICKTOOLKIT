package com.eplugger.xml.dom4j.utils;

import org.dom4j.Document;
import org.junit.Test;

import com.eplugger.onekey.addField.entity.ModuleTables;

public class TestParseUtils {
	@Test
	public void testToBean() throws Exception {
		ModuleTables moduleTables = ParseUtils.toBean("src/main/resource/field/ModuleTable.xml", ModuleTables.class);
		System.out.println(moduleTables);
	}
	
	@Test
	public void testFromBean() throws Exception {
		Document document = ParseUtils.fromBean("src/main/resource/field/ModuleTable.xml", ModuleTables.class);
		System.out.println(document.asXML());
	}
}
