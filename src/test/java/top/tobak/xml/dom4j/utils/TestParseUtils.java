package top.tobak.xml.dom4j.utils;

import com.eplugger.onekey.entity.ModuleTables;
import org.dom4j.Document;
import org.junit.Test;

public class TestParseUtils {
	@Test
	public void testToBean() throws Exception {
		ModuleTables moduleTables = XmlParseUtils.toBean("src/main/resource/field/ModuleTable.xml", ModuleTables.class);
		System.out.println(moduleTables);
	}
	
	@Test
	public void testFromBean() throws Exception {
		Document document = XmlParseUtils.fromBean("src/main/resource/field/ModuleTable.xml", ModuleTables.class, false);
		System.out.println(document.asXML());
	}
}
