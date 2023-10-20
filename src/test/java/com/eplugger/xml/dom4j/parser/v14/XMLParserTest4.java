package com.eplugger.xml.dom4j.parser.v14;

import java.io.File;
import java.util.List;

import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.entity.ModuleTables;
import com.google.common.base.Strings;
import org.junit.Test;
import top.tobak.xml.dom4j.XMLObject;
import top.tobak.xml.dom4j.XMLParser;
import top.tobak.xml.dom4j.parse.FieldValueParserFactory;
import top.tobak.xml.dom4j.parse.SimpleValueParser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XMLParserTest4 {

    private XMLParser xmlParser;

    private void before(String path) {
        String xmlPath = XMLParserTest4.class.getResource(path).getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());
        xmlParser = new XMLParser(xmlPath);
    }
    
    @Test
    public void testParse() throws Exception {
    	before("/module/ModuleTable.xml");
        XMLObject root = xmlParser.parse();
        assertNotNull(root);
        
        FieldValueParserFactory.reg(new SimpleValueParser<Boolean>() {
        	@Override
        	public Class<Boolean> getPreciseType() {
        		return boolean.class;
        	}
        	@Override
        	public Boolean fromXml(Class<?> type, String value) {
        		value = Strings.emptyToNull(value);
        		if (Strings.isNullOrEmpty(value)) {
        			return true;
        		}
        		return Boolean.valueOf(value);
        	}
		});

        ModuleTables moduleTables = root.toBean(ModuleTables.class);
    	assertNotNull(moduleTables);
    	System.out.println(moduleTables);
    	List<ModuleTable> validList = moduleTables.getValidList();
    	System.out.println(validList);
    }
    
    @Test
    public void testParse1() throws Exception {
    	before("/field/Field.xml");
    	XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	
    	Fields fields = root.toBean(Fields.class);
    	assertNotNull(fields);
    	System.out.println(fields);
    }
}
