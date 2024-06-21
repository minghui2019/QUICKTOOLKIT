package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.onekey.entity.Modules;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

public class ModuleParser extends AbstractXmlParser<Modules> {

    public ModuleParser() {
    }

    @Override
    public void register() {
        XmlParseUtils.registerParser(Modules.class, this);
    }
}
