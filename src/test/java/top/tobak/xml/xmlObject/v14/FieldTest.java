package top.tobak.xml.xmlObject.v14;

import java.io.File;

import com.eplugger.onekey.entity.Fields;
import org.junit.Test;
import top.tobak.xml.xmlObject.XMLObject;
import top.tobak.xml.xmlObject.XMLParser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
