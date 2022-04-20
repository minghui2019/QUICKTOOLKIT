package com.eplugger.trans;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.trans.entity.Fields;
import com.eplugger.trans.entity.SimpleField;
import com.eplugger.trans.service.TransService;
import com.eplugger.util.OtherUtils;
import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;

public class TextTrans {
	
	public static void main(String[] args) throws IOException {
		String src = "主项目、项目类型、项目介绍、分管副院长";
		String dst = TransService.transText2En(src);
//		Patent source, transferor, country, contact person of agency, contact number of agency
//		String dst = "Patent source, transferor, country, contact person of agency";
		String[] dsts = dst.split(",");
		String[] result = transText2En(dsts);
		System.out.println(Arrays.toString(result));
		String[] srcs = src.split("、");
		Fields fields = TextTrans.bulidFields(srcs, result);
		
		XMLObject root = XMLObject.of(fields).setRootElement(Boolean.TRUE).setDocumentType("Fields", null, "../dtd/Field.dtd");
		
		String path = TextTrans.class.getResource("/").getPath() + "/Field_out.xml";
		File file = new File(path);
		XMLParser.transfer(root, file, false);
		System.out.println(file.getCanonicalPath());
	}
	
	public static Fields bulidFields(String[] srcs, String[] result) {
		Fields fields = new Fields();
		List<SimpleField> fieldList = fields.getFieldList();
		for (int i = 0; i < srcs.length; i++) {
			fieldList.add(new SimpleField(result[i], srcs[i], OtherUtils.TPYE_STRING, 500));
		}
		fieldList.add(new SimpleField(result[0] + "Id", srcs[0] + "ID", OtherUtils.TPYE_STRING, 32));
		fieldList.add(new SimpleField(result[srcs.length - 1] + "Id", srcs[srcs.length - 1] + "ID", OtherUtils.TPYE_STRING, 32));
		return fields;
	}
	
	public static String[] transText2En(String[] dsts) {
		String[] dests = new String[dsts.length];
		for (int i = 0; i < dsts.length; i++) {
			String result = CharMatcherHandlerFactory.getFactory().matcherChar(dsts[i].trim());
			String[] destArr = result.split(" ");
			String dest = StringUtils.firstCharLowerCase(destArr[0]);
			for (int j = 1; j < destArr.length; j++) {
				dest += StringUtils.firstCharUpperCase(destArr[j]);
			}
			dests[i] = dest;
		}
		return dests;
	}
}
