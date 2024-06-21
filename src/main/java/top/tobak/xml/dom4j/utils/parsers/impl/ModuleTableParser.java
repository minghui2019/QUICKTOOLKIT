package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.onekey.entity.ModuleTables;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

public class ModuleTableParser extends AbstractXmlParser<ModuleTables> {

    @Override
    public void register() {
        XmlParseUtils.registerParser(ModuleTables.class, this);
    }
}
