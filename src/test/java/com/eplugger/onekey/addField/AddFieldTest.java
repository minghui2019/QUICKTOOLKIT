package com.eplugger.onekey.addField;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addField.util.FieldParse;
import com.eplugger.onekey.addField.util.ModuleTableParse;
import com.eplugger.uuid.UUIDFun;

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
		List<Field> fieldList = FieldParse.getInstance().getValidList(classPath + "field/Field.xml");
		System.out.println(fieldList);
	}
	
	@Test
	public void testParseModuleTable() throws Exception {
		Map<String, String> map1 = ModuleTableParse.getInstance().getModuleTableMap(classPath + "field/ModuleTable.xml");
		Map<String, String> map = ModuleTableParse.getInstance().getValidModuleTableMap(classPath + "field/ModuleTable.xml");
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
