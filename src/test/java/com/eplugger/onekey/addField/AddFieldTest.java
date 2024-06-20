package com.eplugger.onekey.addField;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTables;
import org.junit.Before;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

public class AddFieldTest {
	private String classPath;
	
	@Before
	public void getClassPath() throws Exception {
		URL url = this.getClass().getResource("/");
		Path path = Paths.get(url.toURI());
		classPath = path.getParent().toString() + File.separator  + "classes" + File.separator;
	}
	
	@Test
	public void testParseField() throws Exception {
		List<Field> fieldList = XmlParseUtils.toBean(classPath + "field/Field.xml", Fields.class).getFieldList();
		System.out.println(fieldList);
	}
	
	@Test
	public void testParseModuleTable() throws Exception {
		Map<String, String> map1 = XmlParseUtils.toBean(classPath + "field/ModuleTable.xml", ModuleTables.class).getModuleTableMap();
		Map<String, String> map = XmlParseUtils.toBean(classPath + "field/ModuleTable.xml", ModuleTables.class).getValidModuleTableMap();
		System.out.println(map1);
		System.out.println(map);
	}
}
