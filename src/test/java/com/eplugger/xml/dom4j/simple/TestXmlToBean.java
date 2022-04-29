package com.eplugger.xml.dom4j.simple;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.eplugger.onekey.addField.entity.Fields;
import com.eplugger.onekey.addField.entity.ModuleTables;
import com.eplugger.uuid.entity.Uuids;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestXmlToBean {
	@Test
	public void testUUID() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/uuid/UUID.xml";
        Dom4JParser xmlToBean = new Dom4JParser(xmlPath);
        Uuids bean = xmlToBean.toBean(Uuids.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
	
	@Test
	public void testField() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/field/Field.xml";
        Dom4JParser xmlToBean = new Dom4JParser(xmlPath);
        Fields bean = xmlToBean.toBean(Fields.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
	
	@Test
	public void testModuleTable() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/module/ModuleTable.xml";
		Dom4JParser xmlToBean = new Dom4JParser(xmlPath);
        ModuleTables bean = xmlToBean.toBean(ModuleTables.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
}
