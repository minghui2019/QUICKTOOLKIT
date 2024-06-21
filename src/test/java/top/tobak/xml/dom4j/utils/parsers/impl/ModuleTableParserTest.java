package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.onekey.entity.ModuleTables;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

@Slf4j
public class ModuleTableParserTest {
    private String path = this.getClass().getResource("/").getPath();
    @Test
    public void testXmlToBean() throws Exception {
        ModuleTables moduleTables = XmlParseUtils.toBean(ModuleTables.class, path + "field/ModuleTable.xml");
        log.debug(moduleTables.toString());
    }

    @Test
    public void testXmlFromBean() throws Exception {
        ModuleTables moduleTables = XmlParseUtils.toBean(ModuleTables.class, path + "field/ModuleTable.xml");
        Document document = XmlParseUtils.fromBean(moduleTables, path + "ModuleTable_Test.xml", true);
        log.debug(document.asXML());
    }
}