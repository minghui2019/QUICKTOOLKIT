package com.eplugger.xml.dom4j.simple;

import java.util.List;

import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.uuid.UUIDFun;
import com.eplugger.uuid.entity.Uuids;
import org.dom4j.Document;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.ParseXmlUtils;

import static org.junit.Assert.assertNotNull;

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
		Document document = ParseXmlUtils.fromBean(xmlPath, moduleTables, true);
		assertNotNull(document);
	}
	
	@Test
	public void testUUID() throws Exception {
		Uuids uuids = new Uuids();
		uuids.setUuidList(UUIDFun.getInstance().buildUuids(30));
		String xmlPath = TestXmlFromBean.class.getResource("/").getPath() + "/test_uuid.xml";
		Document document = ParseXmlUtils.fromBean(xmlPath, uuids, true);
		assertNotNull(document);
	}
}
