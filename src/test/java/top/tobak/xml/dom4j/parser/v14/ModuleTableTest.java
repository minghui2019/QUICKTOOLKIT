package top.tobak.xml.dom4j.parser.v14;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.entity.ModuleTables;
import com.google.common.base.Strings;
import org.junit.Test;
import top.tobak.xml.dom4j.XMLObject;
import top.tobak.xml.dom4j.XMLParser;
import top.tobak.xml.dom4j.parse.FieldValueParserFactory;
import top.tobak.xml.dom4j.parse.SimpleValueParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ModuleTableTest {
    private XMLParser xmlParser;
    private void before(String path) {
        String xmlPath = ModuleTableTest.class.getResource(path).getFile();
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
    public void testTransfer1() throws IOException {
        ModuleTables moduleTables2 = new ModuleTables();
        List<ModuleTable> moduleTables = moduleTables2.getModuleTableList();
        ModuleTable moduleTable = new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员");
        moduleTables.add(moduleTable);
        moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
        moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
        moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));

        // Bean 转化为 XMLObject
        XMLObject root = XMLObject.of(moduleTables2);
        root.setRootElement(true).setDocumentType("Modules", null, "../dtd/Module.dtd");
        Map<String, List<XMLObject>> childTags = root.getChildTags();
        assertEquals(1, childTags.size());

        String path = ModuleTableTest.class.getResource("/").getPath() + "/xml-test-transfer2-retract1.xml";
        File retractFile = new File(path);
        System.out.println(retractFile);
        XMLParser.transfer(root, retractFile, false);
    }
}
