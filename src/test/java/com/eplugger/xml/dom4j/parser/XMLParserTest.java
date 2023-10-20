package com.eplugger.xml.dom4j.parser;

import java.io.File;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import top.tobak.xml.dom4j.XMLObject;
import top.tobak.xml.dom4j.XMLParser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * XML解析器测试
 *
 * @author Huang.Yong
 * @version 0.1
 */
public class XMLParserTest {

    private XMLParser xmlParser;

    private XMLObject root;

    @Before
    public void before() throws Exception {
        String xmlPath = XMLParserTest.class.getResource("/xml-test.xml").getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());

        xmlParser = new XMLParser(xmlPath);

        // 首次解析
        testParse();
    }

    @Test
    public void testParse() throws Exception {
        root = xmlParser.parse();
        assertNotNull(root);
    }

    @Test
    public void testJsonParse() {
        Integer v = JSON.parseObject("1", Integer.class);
        System.out.println(v);

        String s = JSON.parseObject("1", String.class);
        System.out.println(s);
    }
}
