package com.eplugger.trans;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.AddFieldFun;
import com.eplugger.trans.entity.SimpleField;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.trans.service.TransService;
import com.eplugger.utils.OtherUtils;
import com.eplugger.xml.dom4j.utils.ParseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextTrans {
	public static void main(String[] args) throws Exception {
//		Patent source, transferor, country, contact person of agency, contact number of agency
//		String dst = "Patent source, transferor, country, contact person of agency";
		
		createFieldXml("主项目、项目类型、项目介绍、分管副院长");
	}
	
	/**
	 * <pre>
	 * 翻译src原文，并生成Field.xml文件
	 * </pre>
	 * @param src 翻译原文，每个词组需要以顿号（、）分割
	 * @throws Exception
	 */
	public static void createFieldXml(String src) throws Exception {
		String dst = TransService.transText2En(src);
		String[] dsts = dst.split(",");
		String[] result = transText2En(dsts);
		log.debug(Arrays.toString(result));
		String[] srcs = src.split("、");
		SimpleFields fields = TextTrans.bulidFields(srcs, result);
		
		Document document = ParseUtils.fromBean(AddFieldFun.FILE_OUT_PATH_FIELD, fields);
		log.debug("\n" + document.asXML());
		FileUtils.openTaskBar(new File(AddFieldFun.FILE_OUT_PATH_FIELD).getParentFile());
	}
	
	public static SimpleFields bulidFields(String[] srcs, String[] result) {
		SimpleFields fields = new SimpleFields();
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
