package com.eplugger.xml.dom4j.simple;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.dom4j.Document;
import org.junit.Test;

import com.eplugger.uuid.UUIDFun;
import com.eplugger.uuid.entity.Uuids;
import com.eplugger.xml.dom4j.entity.ModuleTable;
import com.eplugger.xml.dom4j.entity.ModuleTables;

public class TestXmlFromBean {
	@Test
	public void testModuleTable() throws Exception {
		ModuleTables moduleTables = new ModuleTables();
		List<ModuleTable> moduleTableList = moduleTables.getModuleTableList();
		moduleTableList.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTableList.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTableList.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTableList.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		String xmlPath = TestXmlFromBean.class.getResource("/").getPath() + "/test_ModuleTable.xml";
		XmlFromBean testXml = new XmlFromBean(xmlPath);
		Document document = testXml.fromBean(moduleTables, true);
		assertNotNull(document);
	}
	
	@Test
	public void testUUID() throws Exception {
		Uuids uuids = new Uuids();
		uuids.setUuidList(UUIDFun.getInstance().buildUuids(30));
		String xmlPath = TestXmlFromBean.class.getResource("/").getPath() + "/test_uuid.xml";
		XmlFromBean testXml = new XmlFromBean(xmlPath);
		Document document = testXml.fromBean(uuids, true);
		assertNotNull(document);
	}
}
