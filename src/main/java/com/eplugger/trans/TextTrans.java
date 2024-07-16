package com.eplugger.trans;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baidu.translate.service.TransService;
import com.eplugger.onekey.addField.AddFieldFun;
import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.trans.entity.SimpleField;
import com.eplugger.trans.entity.SimpleFields;
import com.eplugger.utils.OtherUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import top.tobak.common.lang.StringUtils;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

@Slf4j
public class TextTrans {
	public static void main(String[] args) throws Exception {
//		Patent source, transferor, country, contact person of agency, contact number of agency
//		String dst = "Patent source, transferor, country, contact person of agency";

		createFieldXml(new String[] { "财务系统编号", "项目ID", "下拨合作单位", "科研支出", "人员支出", "公共管理费", "采购冻结", "冻结人员费", "暑期工资", "冻结管理费", "结余" });
	}
	
	/**
	 * <pre>
	 * 翻译src原文，并生成Field.xml文件
	 * </pre>
	 * @param srcs 翻译原文
	 * @throws Exception
	 */
	public static void createFieldXml(String[] srcs) throws Exception {
		List<String> dsts = TransService.transTextZh2En(srcs);
		String[] result = transText2En(dsts.toArray(new String[0]));
		log.debug(Arrays.toString(result));
		SimpleFields fields = TextTrans.buildFields(srcs, result);
		
		Document document = XmlParseUtils.fromBean(fields, AddFieldFun.FILE_OUT_PATH_FIELD, true);
		log.debug("\n" + document.asXML());
//		FileUtils.openTaskBar(new File(AddFieldFun.FILE_OUT_PATH_FIELD).getParentFile());
	}
	
	public static SimpleFields buildFields(String[] srcs, String[] result) {
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

	public static void createFieldXml(String[] src, String... moduleTables) throws Exception {
		createFieldXml(src, Stream.of(moduleTables).map(moduleTable -> new ModuleTable(moduleTable)).collect(Collectors.toList()));
	}

	public static void createFieldXml(String[] src, ModuleTable... moduleTables) throws Exception {
		createFieldXml(src, Lists.newArrayList(moduleTables));
	}

	/**
	 * 字段翻译并生成常规的Field.xml文件方法
	 * @param src 待翻译中文，全角顿号（、）隔开
	 * @param moduleTableList {@link com.eplugger.onekey.entity.ModuleTable ModuleTable}列表
	 * @throws Exception
	 */
	public static void createFieldXml(String[] src, List<ModuleTable> moduleTableList) throws Exception {
		if (moduleTableList == null || moduleTableList.isEmpty()) {
			return;
		}
		Set<String> set = moduleTableList.stream().map(ModuleTable::getModuleName).collect(Collectors.toSet());
		ModuleTables moduleTables = XmlParseUtils.toBean(ModuleTables.class, AddFieldFun.FILE_OUT_PATH_MODULETABLE);
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
		XmlParseUtils.fromBean(moduleTables, AddFieldFun.FILE_OUT_PATH_MODULETABLE, true);
		createFieldXml(src);
	}

	public static void hasModuleTables(ModuleTable... moduleTables) throws Exception {
		hasModuleTables(Lists.newArrayList(moduleTables));
	}

	public static void hasModuleTables(List<ModuleTable> moduleTableList) throws Exception {
		if (moduleTableList == null || moduleTableList.isEmpty()) {
			return;
		}
		Set<String> set = moduleTableList.stream().map(ModuleTable::getModuleName).collect(Collectors.toSet());
		ModuleTables moduleTables = XmlParseUtils.toBean(ModuleTables.class, AddFieldFun.FILE_OUT_PATH_MODULETABLE);
		Map<String, String> moduleTableMap = moduleTables.getModuleTableMap();
		JsonObject jsonObject = new JsonObject();
		for (String key : set) {
			if (moduleTableMap.containsKey(key)) {
				jsonObject.addProperty(key, true);
			} else {
				jsonObject.addProperty(key, false);
			}
		}
		log.debug("模块检查信息如下\n" + jsonObject.toString());
	}
}
