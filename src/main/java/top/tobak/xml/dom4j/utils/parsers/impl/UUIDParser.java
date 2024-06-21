package top.tobak.xml.dom4j.utils.parsers.impl;

import com.eplugger.uuid.entity.UUIDS;
import lombok.extern.slf4j.Slf4j;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

@Slf4j
public class UUIDParser extends AbstractXmlParser<UUIDS> {
	@Override
	protected void beforeFromBean(String path) {
		super.beforeFromBean(path);
		this.setDocType("uuids", null, "../dtd/UUID.dtd");
	}

	@Override
	public void register() {
		XmlParseUtils.registerParser(UUIDS.class, this);
	}
}
