package com.eplugger.onekey.addField;

import java.io.File;

import com.eplugger.onekey.entity.Modules;
import org.junit.Test;
import top.tobak.xml.dom4j.XMLObject;
import top.tobak.xml.dom4j.XMLParser;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParseModulesTest {
	private XMLParser xmlParser;
	private String xmlPath;

    private void before(String path) {
        String xmlPath = ParseModulesTest.class.getResource(path).getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());
        xmlParser = new XMLParser(xmlPath);
    }
    
    private void before1(String path) {
    	xmlPath = ParseModulesTest.class.getResource(path).getFile();
    	assertNotNull(xmlPath);
    	
    	File file = new File(xmlPath);
    	assertTrue(file.exists());
    }
    
    @Test
    public void testParseModules() throws Exception {
    	before("/module/Module.xml");
    	XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	
    	Modules modules = root.toBean(Modules.class);
    	assertNotNull(modules);
    	System.out.println(modules);
    }
    
    @Test
    public void testParseModules1() throws Exception {
    	before1("/module/Module.xml");
    	Modules modules = XmlParseUtils.toBean(xmlPath, Modules.class);
    	assertNotNull(modules);
    	System.out.println(modules);
    }
}
