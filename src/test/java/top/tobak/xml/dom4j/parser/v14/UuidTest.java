package top.tobak.xml.dom4j.parser.v14;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.eplugger.uuid.entity.UUIDS;
import com.eplugger.uuid.utils.UUIDUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import top.tobak.xml.dom4j.XMLObject;
import top.tobak.xml.dom4j.XMLParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UuidTest {
	private XMLParser xmlParser;
	private XMLObject xmlObject;

	@Before
    public void before() {
        String xmlPath = UuidTest.class.getResource("/uuid/UUID.xml").getFile();
        assertNotNull(xmlPath);

        File file = new File(xmlPath);
        assertTrue(file.exists());
        xmlParser = new XMLParser(xmlPath);
    }
    
	@Test
    public void testParse() throws Exception {
    	XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	UUIDS uuids = root.toBean(UUIDS.class);
    	assertNotNull(uuids);
    	System.out.println(uuids);
    }
	
	@Test
    public void testTransfer() throws IOException {
		UUIDS uuids = new UUIDS();
		uuids.addAll(UUIDUtils.generateUUID(30));
    	
    	// Bean 转化为 XMLObject
    	XMLObject root = XMLObject.of(uuids);
        root.setRootElement(true);
    	Map<String, List<XMLObject>> childTags = root.getChildTags();
    	assertEquals(1, childTags.size());
    	xmlObject = root;
    }
	
	@After
	public void after() throws IOException {
		if (xmlObject == null)
			return;
		String path = UuidTest.class.getResource("/").getPath() + "/xml-test-uuid.xml";
    	File retractFile = new File(path);
    	System.out.println(retractFile);
    	XMLParser.transfer(xmlObject, retractFile, false);
	}
}
