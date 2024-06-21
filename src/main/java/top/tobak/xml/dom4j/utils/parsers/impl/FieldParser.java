package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.onekey.entity.Fields;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

public class FieldParser extends AbstractXmlParser<Fields> {

    @Override
    public void register() {
        XmlParseUtils.registerParser(Fields.class, this);
    }
}
