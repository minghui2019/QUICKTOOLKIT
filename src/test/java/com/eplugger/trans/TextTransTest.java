package com.eplugger.trans;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.eplugger.trans.entity.SimpleField;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.utils.OtherUtils;
import org.junit.Test;
import top.tobak.xml.dom4j.XMLObject;
import top.tobak.xml.dom4j.XMLParser;

import static org.junit.Assert.assertTrue;

public class TextTransTest {
	@Test
	public void testCreateXml() throws Exception {
		String src = "主项目、项目类型、项目介绍、分管副院长";
		String dst = "Patent source, transferor, country, contact person of agency";
		String[] dsts = dst.split(",");
		String[] result = TextTrans.transText2En(dsts);
		System.out.println(Arrays.toString(result));
		String[] srcs = src.split("、");
		SimpleFields fields = new SimpleFields();
		List<SimpleField> fieldList = fields.getFieldList();
		for (int i = 0; i < srcs.length; i++) {
			fieldList.add(new SimpleField(result[i], srcs[i], OtherUtils.TPYE_STRING, 500));
		}
		fieldList.add(new SimpleField(result[0] + "ID", srcs[0] + "Id", OtherUtils.TPYE_STRING, 32));
		XMLObject root = XMLObject.of(fields).setRootElement(Boolean.TRUE).setDocumentType("Fields", null, "../dtd/Field.dtd");
		String path = TextTransTest.class.getResource("/").getPath() + "/Field_out.xml";
		boolean transfer = XMLParser.transfer(root, new File(path), false);
		assertTrue(transfer);
	}
}
