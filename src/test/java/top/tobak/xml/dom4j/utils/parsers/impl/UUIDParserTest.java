package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.uuid.entity.UUIDS;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

@Slf4j
public class UUIDParserTest {
    private String path = this.getClass().getResource("/").getPath();
    @Test
    public void testXmlToBean() throws Exception {
        UUIDS uuids = XmlParseUtils.toBean(UUIDS.class, path + "uuid/UUID.xml");
        log.debug(uuids.toString());
    }

    @Test
    public void testXmlFromBean() throws Exception {
        UUIDS uuids = XmlParseUtils.toBean(UUIDS.class, path + "uuid/UUID.xml");
        log.debug(uuids.toString());

        Document document = XmlParseUtils.fromBean(uuids, path + "UUID_Test.xml", true);
        log.debug(document.asXML());
    }
}