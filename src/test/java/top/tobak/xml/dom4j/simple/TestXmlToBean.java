package top.tobak.xml.dom4j.simple;

import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.uuid.entity.UUIDS;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestXmlToBean {
	@Test
	public void testUUID() throws Exception {
		XmlParseUtils.registerBean(new AbstractXmlParser<UUIDS>() {}, UUIDS.class);
		String xmlPath = TestXmlToBean.class.getResource("/uuid/UUID.xml").getFile();
		UUIDS bean = XmlParseUtils.toBean(xmlPath, UUIDS.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
	
	@Test
	public void testField() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/field/Field.xml";
		Fields bean = XmlParseUtils.toBean(xmlPath, Fields.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
	
	@Test
	public void testModuleTable() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/module/ModuleTable.xml";
		ModuleTables bean = XmlParseUtils.toBean(xmlPath, ModuleTables.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
}
