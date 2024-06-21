package top.tobak.xml.dom4j.utils.parsers.impl;

import java.util.List;

import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.entity.Fields;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.junit.Test;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class FieldParserTest {
    private String path = this.getClass().getResource("/").getPath();
    @Test
    public void testXmlToBean() throws Exception {
        String xmlPath = path + "field/Field.xml";
        Fields fields = XmlParseUtils.toBean(Fields.class, xmlPath);
        List<Category> categories = fields.getCategories();
        assertNotNull(fields);
        assertNotNull(categories);
        log.debug(fields.toString());
        log.debug(categories.toString());
    }

    @Test
    public void testXmlFromBean() throws Exception {
        String xmlPath = path + "field/Field.xml";
        Fields fields = XmlParseUtils.toBean(Fields.class, xmlPath);
        assertNotNull(fields);
        log.debug(fields.toString());

        Document document = XmlParseUtils.fromBean(fields, path + "Field_Test.xml", true);
        log.debug(document.asXML());
    }
}