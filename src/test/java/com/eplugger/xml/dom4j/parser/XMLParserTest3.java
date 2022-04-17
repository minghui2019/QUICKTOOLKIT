package com.eplugger.xml.dom4j.parser;

import com.alibaba.fastjson.JSON;
import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;
import com.eplugger.xml.dom4j.entity.TourLine;
import com.eplugger.xml.dom4j.entity.TourStation;
import com.eplugger.xml.dom4j.entity.TourTask;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

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

        // PASS
        // 多属性
        XMLObject tourTaskXml = root.getChildTag("TourTask", 0);
        TourTask tourTask = tourTaskXml.toBean(TourTask.class);
        assertNotNull(tourTask);
        System.out.println(tourTask);

        // PASS
        // 单属性
        XMLObject tourLineXml = root.getChildTag("TourLine", 0);
        TourLine tourLine = tourLineXml.toBean(TourLine.class);
        assertNotNull(tourLine);
        System.out.println(tourLine);

        // PASS
        // 子控件
        XMLObject stationListXml = root.getChildTag("TourStationList", 0);
        List<TourStation> stationList = stationListXml.toBeans("TourStation", TourStation.class);
        assertEquals(2, stationList.size());
        System.out.println(stationList.get(0));
        System.out.println(JSON.toJSONString(stationList.get(0)));
    }

}
