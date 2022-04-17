package com.eplugger.xml.dom4j.parser.v14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;
import com.eplugger.xml.dom4j.entity.ModuleTable;
import com.eplugger.xml.dom4j.entity.ModuleTables;
import com.eplugger.xml.dom4j.entity.SnapshotPosition;
import com.eplugger.xml.dom4j.entity.TourAction;
import com.eplugger.xml.dom4j.entity.TourLine;
import com.eplugger.xml.dom4j.entity.TourReport;
import com.eplugger.xml.dom4j.entity.TourStation;
import com.eplugger.xml.dom4j.entity.TourStatus;
import com.eplugger.xml.dom4j.entity.TourTask;
import com.eplugger.xml.dom4j.parse.FieldValueParserFactory;
import com.eplugger.xml.dom4j.parse.SimpleValueParser;
import com.eplugger.xml.dom4j.parse.parsers.DefaultSimpleValueParser;

public class XMLParserTest3 {

    private XMLParser xmlParser;

    @Before
    public void before() {
        String xmlPath = XMLParserTest3.class.getResource("/test1_20200902_145714.xml").getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());
        //xmlParser = new XMLParser(xmlPath, "GB2312");
        xmlParser = new XMLParser(xmlPath);
        //xmlParser = new XMLParser(xmlPath, StandardCharsets.ISO_8859_1);
    }

    @Test
    public void testParse() throws Exception {
        XMLObject root = xmlParser.parse();
        assertNotNull(root);

        FieldValueParserFactory.reg(new SimpleValueParser<String>() {
            @Override
            public Class<String> getPreciseType() {
                return String.class;
            }

            @Override
            public String fromXml(Class<?> type, String value) {
                return StringUtils.trimToEmpty(value);
//                return StringUtil.trimToEmpty(value) + "_ValueParser<String>";
            }
        });

        // PASS
        // 多属性
        XMLObject tourTaskXml = root.getChildTag("TourTask", 0);
        TourTask tourTask = tourTaskXml.toBean(TourTask.class);
        assertNotNull(tourTask);
        System.out.println(tourTask);

        // 单属性
        XMLObject tourLineXml = root.getChildTag("TourLine", 0);
        TourLine tourLine = tourLineXml.toBean(TourLine.class);
        assertNotNull(tourLine);
        System.out.println(tourLine);

        // 子控件
        XMLObject stationListXml = root.getChildTag("TourStationList", 0);
        List<TourStation> stationList = stationListXml.toBeans("TourStation", TourStation.class);
        assertEquals(2, stationList.size());
        System.out.println(stationList.get(0));
        System.out.println(JSON.toJSONString(stationList.get(0)));
    }
    
    @Test
    public void testParse1() throws Exception {
    	XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	FieldValueParserFactory.reg(new SimpleValueParser<String>() {
    		@Override
    		public Class<String> getPreciseType() {
    			return String.class;
    		}
    		
    		@Override
    		public String fromXml(Class<?> type, String value) {
    			return StringUtils.trimToEmpty(value);
    		}
    	});
    	
    	// PASS
    	XMLObject tourStatusXml = root.getChildTag("TourStatus", 0);
    	TourStatus tourStatus = tourStatusXml.toBean(TourStatus.class);
    	assertNotNull(tourStatus);
    	System.out.println(tourStatus);
    	
    	TourReport tourReport = root.toBean(TourReport.class);
    	assertNotNull(tourReport);
    	System.out.println(tourReport);
    }

    @Test
    public void testTransfer() throws IOException {
        SnapshotPosition sp = new SnapshotPosition(1, 2, 3, 4, 5, 6, 7, 8, null);
        TourAction tourAction = new TourAction(1, "Foo名称", 2, 1234567, sp);

        // Bean 转化为 XMLObject
        XMLObject root = XMLObject.of(tourAction);
        root.setRootElement(true);
        Map<String, List<XMLObject>> childTags = root.getChildTags();
        assertEquals(1, childTags.size());

        String path = XMLParserTest3.class.getResource("/").getPath() + "/xml-test-transfer2-compact.xml";
        File compactFile = new File(path);
        System.out.println(compactFile);
        XMLParser.transfer(root, compactFile, true);

        path = XMLParserTest3.class.getResource("/").getPath() + "/xml-test-transfer2-retract.xml";
        File retractFile = new File(path);
        System.out.println(retractFile);
        XMLParser.transfer(root, retractFile, false);
    }
    
    @Test
    public void testTransfer1() throws IOException {
    	ModuleTables moduleTables2 = new ModuleTables();
		List<ModuleTable> moduleTables = moduleTables2.getModuleTables();
		ModuleTable moduleTable = new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员");
		moduleTables.add(moduleTable);
		moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
    	
    	// Bean 转化为 XMLObject
    	XMLObject root = XMLObject.of(moduleTables2);
    	root.setRootElement(true);
    	Map<String, List<XMLObject>> childTags = root.getChildTags();
    	assertEquals(1, childTags.size());
    	
    	String path = XMLParserTest3.class.getResource("/").getPath() + "/xml-test-transfer2-retract1.xml";
    	File retractFile = new File(path);
    	System.out.println(retractFile);
    	XMLParser.transfer(root, retractFile, false);
    }

    @Test
    public void testOther() {

        SimpleValueParser<?> parser = new DefaultSimpleValueParser();
        Object o = parser.fromXml(Date.class, new Date().getTime() + "");
        System.out.println(o);

        Class<?> cls = int.class;
        int i = JSON.parseObject("1", (Type) cls);
        System.out.println(i);
    }

}
