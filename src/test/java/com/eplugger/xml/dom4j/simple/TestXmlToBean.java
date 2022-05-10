package com.eplugger.xml.dom4j.simple;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.uuid.entity.Uuids;
import com.eplugger.xml.dom4j.utils.ParseXmlUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestXmlToBean {
	@Test
	public void testUUID() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/uuid/UUID.xml";
		Uuids bean = ParseXmlUtils.toBean(xmlPath, Uuids.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
	
	@Test
	public void testField() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/field/Field.xml";
		Fields bean = ParseXmlUtils.toBean(xmlPath, Fields.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
	
	@Test
	public void testModuleTable() throws Exception {
		String xmlPath = TestXmlToBean.class.getResource("/").getPath() + "../../src/test/resource/module/ModuleTable.xml";
		ModuleTables bean = ParseXmlUtils.toBean(xmlPath, ModuleTables.class);
        assertNotNull(bean);
        log.debug(bean.toString());
	}
}
