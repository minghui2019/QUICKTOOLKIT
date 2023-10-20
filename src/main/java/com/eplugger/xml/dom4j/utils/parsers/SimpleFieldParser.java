package com.eplugger.xml.dom4j.utils.parsers;

import java.io.File;
import java.io.IOException;

import com.eplugger.trans.entity.SimpleFields;
import lombok.extern.slf4j.Slf4j;
import top.tobak.common.io.FileUtils;

@Slf4j
public class SimpleFieldParser extends AbstractXmlParser<SimpleFields> {
	@Override
	protected void beforeFromBean(String path) {
		super.beforeFromBean(path);
		this.setDocType("Fields", null, "../dtd/Field.dtd");
		File file = new File(getPath());
		try {
			FileUtils.backupSrcFile(file);
		} catch (IOException e) {
			log.error("文件备份失败，查看文件路径[path=" + getPath() + "]");
		}
	}
}
