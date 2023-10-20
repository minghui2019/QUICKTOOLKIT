package com.eplugger.onekey.addField;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.uuid.UUIDFun;
import org.junit.Before;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.ParseXmlUtils;

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
		List<Field> fieldList = ParseXmlUtils.toBean(classPath + "field/Field.xml", Fields.class).getFieldList();
		System.out.println(fieldList);
	}
	
	@Test
	public void testParseModuleTable() throws Exception {
		Map<String, String> map1 = ParseXmlUtils.toBean(classPath + "field/ModuleTable.xml", ModuleTables.class).getModuleTableMap();
		Map<String, String> map = ParseXmlUtils.toBean(classPath + "field/ModuleTable.xml", ModuleTables.class).getValidModuleTableMap();
		System.out.println(map1);
		System.out.println(map);
	}
	
	@Test
	public void testJoinUUID() throws Exception {
		List<String> uuidList = UUIDFun.getInstance().getUuidsList(10);
		String collect = uuidList.stream().map(u -> "'" + u + "'").collect(Collectors.joining(", "));
		System.out.println(collect);
	}
}
