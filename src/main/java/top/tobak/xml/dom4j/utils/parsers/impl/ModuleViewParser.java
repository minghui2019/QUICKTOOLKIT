package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.onekey.viewFile.entity.ModuleViews;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

public class ModuleViewParser extends AbstractXmlParser<ModuleViews> {

    @Override
    public void register() {
        XmlParseUtils.registerParser(ModuleViews.class, this);
    }
}
