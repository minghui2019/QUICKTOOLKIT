package com.eplugger.xml.dom4j.simple;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.dom4j.Document;
import org.junit.Test;

import com.eplugger.onekey.addField.entity.ModuleTable;
import com.eplugger.onekey.addField.entity.ModuleTables;
import com.eplugger.uuid.UUIDFun;
import com.eplugger.uuid.entity.Uuids;

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
		Dom4JParser testXml = new Dom4JParser(xmlPath);
		Document document = testXml.fromBean(moduleTables, true);
		assertNotNull(document);
	}
	
	@Test
	public void testUUID() throws Exception {
		Uuids uuids = new Uuids();
		uuids.setUuidList(UUIDFun.getInstance().buildUuids(30));
		String xmlPath = TestXmlFromBean.class.getResource("/").getPath() + "/test_uuid.xml";
		Dom4JParser testXml = new Dom4JParser(xmlPath);
		Document document = testXml.fromBean(uuids, true);
		assertNotNull(document);
	}
}
