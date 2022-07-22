package com.eplugger.trans;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dom4j.Document;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.AddFieldFun;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.trans.entity.SimpleField;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.trans.service.TransService;
import com.eplugger.utils.OtherUtils;
import com.eplugger.xml.dom4j.utils.ParseXmlUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextTrans {
	public static void main(String[] args) throws Exception {
//		Patent source, transferor, country, contact person of agency, contact number of agency
//		String dst = "Patent source, transferor, country, contact person of agency";
		
		createFieldXml("财务系统编号；项目ID；下拨合作单位；科研支出；人员支出；公共管理费；采购冻结；冻结人员费；暑期工资；冻结管理费；结余");
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
		dst = dst.replaceAll(";", ",");
		String[] dsts = dst.split(",");
		String[] result = transText2En(dsts);
		log.debug(Arrays.toString(result));
		src = src.replaceAll("；", "、");
		String[] srcs = src.split("、");
		SimpleFields fields = TextTrans.bulidFields(srcs, result);
		
		Document document = ParseXmlUtils.fromBean(AddFieldFun.FILE_OUT_PATH_FIELD, fields, true);
		log.debug("\n" + document.asXML());
//		FileUtils.openTaskBar(new File(AddFieldFun.FILE_OUT_PATH_FIELD).getParentFile());
	}
	
	public static SimpleFields bulidFields(String[] srcs, String[] result) {
		SimpleFields fields = new SimpleFields();
		List<SimpleField> fieldList = fields.getFieldList();
		for (int i = 0; i < srcs.length; i++) {
			fieldList.add(new SimpleField(result[i], srcs[i], OtherUtils.TPYE_STRING, 500));
		}
//		fieldList.add(new SimpleField(result[0] + "Id", srcs[0] + "ID", OtherUtils.TPYE_STRING, 32));
//		fieldList.add(new SimpleField(result[srcs.length - 1] + "Id", srcs[srcs.length - 1] + "ID", OtherUtils.TPYE_STRING, 32));
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

	public static void createFieldXml(String src, List<ModuleTable> moduleTableList) throws Exception {
		if (moduleTableList == null || moduleTableList.isEmpty()) {
			return;
		}
		Set<String> set = moduleTableList.stream().map(ModuleTable::getModuleName).collect(Collectors.toSet());
		ModuleTables moduleTables = ParseXmlUtils.toBean(AddFieldFun.FILE_OUT_PATH_MODULETABLE, ModuleTables.class);
		for (ModuleTable moduleTable : moduleTables.getModuleTableList()) {
			if (set.contains(moduleTable.getModuleName())) {
				moduleTable.setIgnore(false);
				set.remove(moduleTable.getModuleName());
			} else {
				moduleTable.setIgnore(true);
			}
		}
		if (set.size() != 0) {
			for (ModuleTable moduleTable : moduleTableList) {
				if (!set.contains(moduleTable.getModuleName())) {
					continue;
				}
				moduleTables.add(moduleTable);
			}
			log.debug("ModuleTable.xml缺少模块" + set.toString() + "的数据，已补充。");
		}
		ParseXmlUtils.fromBean(AddFieldFun.FILE_OUT_PATH_MODULETABLE, moduleTables, true);
		createFieldXml(src);
	}
}
