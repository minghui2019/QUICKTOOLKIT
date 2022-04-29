package com.eplugger.xml.dom4j.utils.parsers;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;

import com.eplugger.common.io.FileUtils;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.xml.dom4j.simple.DocType;
import com.eplugger.xml.dom4j.simple.Dom4JParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleFieldParser extends AbstractParser<SimpleFields> {
	@Override
	public Document fromBean(SimpleFields data, String path) {
		File file = new File(path);
		try {
			FileUtils.backupSrcFile(file);
		} catch (IOException e) {
			log.error("文件备份失败，查看文件路径[path=" + path + "]");
		}
		Dom4JParser testXml = new Dom4JParser(path, new DocType("Fields", null, "../dtd/Field.dtd"));
		Document document = testXml.fromBean(data, true);
		return document;
	}
}
