package com.eplugger.xml.dom4j.parser.v14;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;
import com.eplugger.xml.dom4j.entity.Fields;

public class FieldTest {
	private XMLParser xmlParser;

    private void before(String path) {
        String xmlPath = FieldTest.class.getResource(path).getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());
        xmlParser = new XMLParser(xmlPath);
    }
    
    @Test
    public void testParse() throws Exception {
    	before("/field/Field.xml");
    	XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	
    	Fields fields = root.toBean(Fields.class);
    	assertNotNull(fields);
    	System.out.println(fields);
    }
}
