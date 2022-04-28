package com.eplugger.xml.dom4j.utils;

import java.util.List;

import org.junit.Test;

import com.eplugger.onekey.addField.entity.ModuleTable;
import com.eplugger.xml.dom4j.utils.ParseUtils;

public class TestParseUtils {
	@Test
	public void testParseAllList() throws Exception {
		List<ModuleTable> list = ParseUtils.parseAllList("src/main/resource/field/ModuleTable.xml", ModuleTable.class);
		System.out.println(list);
	}
	
	@Test
	public void testParseValidList() throws Exception {
		List<ModuleTable> list = ParseUtils.parseValidList("src/main/resource/field/ModuleTable.xml", ModuleTable.class);
		System.out.println(list);
	}
}
