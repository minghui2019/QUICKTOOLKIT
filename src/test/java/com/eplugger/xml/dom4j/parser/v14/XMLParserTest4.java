package com.eplugger.xml.dom4j.parser.v14;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;
import com.eplugger.xml.dom4j.entity.Fields;
import com.eplugger.xml.dom4j.entity.ModuleTables;

public class XMLParserTest4 {

    private XMLParser xmlParser;

    @Before
    public void before() {
        String xmlPath = XMLParserTest4.class.getResource("/Field.xml").getFile();
//        String xmlPath = XMLParserTest4.class.getResource("/ModuleTable.xml").getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());
        xmlParser = new XMLParser(xmlPath);
    }

    @Test
    public void testParse() throws Exception {
        XMLObject root = xmlParser.parse();
        assertNotNull(root);

        ModuleTables tourReport = root.toBean(ModuleTables.class);
    	assertNotNull(tourReport);
    	System.out.println(tourReport);
    }
    
    @Test
    public void testParse1() throws Exception {
    	XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	
    	Fields fields = root.toBean(Fields.class);
    	assertNotNull(fields);
    	System.out.println(fields);
    }
}
