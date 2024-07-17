package com.eplugger.onekey.addCategoryEntry.utils;

import java.io.File;
import java.util.List;

import com.eplugger.common.lang.CustomStringBuilder;
import com.eplugger.onekey.entity.CategoryEntry;
import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;
import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.entity.Module;
import com.eplugger.onekey.factory.AbstractProduceCodeFactory;
import com.eplugger.utils.DBUtils;
import com.eplugger.uuid.UUIDFactory;
import com.google.common.base.Strings;

public class ProduceCategorySqlCodeFactory extends AbstractProduceCodeFactory {

	private static class ProduceCategorySqlCodeFactorySingleton {

		private static final ProduceCategorySqlCodeFactory FACTORY = new ProduceCategorySqlCodeFactory();
	}
	public static ProduceCategorySqlCodeFactory getInstance() {
		return ProduceCategorySqlCodeFactorySingleton.FACTORY;
	}

	private ProduceCategorySqlCodeFactory() { }

	public void produceSqlFiles(List<Module> validList) {
		StringBuilder sb = new StringBuilder();
		for (Module module : validList) {
			sb.append(createCategoryStr(module));
		}
		FileUtils.write(FileUtils.getUserHomeDirectory() + "AddModule\\sql" + File.separator + "字典配置SQL.SQL", sb.toString());
	}

	public String createCategoryStr(Module module) {
		List<Category> categories = module.getCategories();
		StringBuilder sb = new StringBuilder();
		for (Category category : categories) {
			sb.append(createCategoryStr(null, category.getValueColumn().split("、"), category.getCategoryName(), category.getBizName(), category.getBizType(), DBUtils.getEadpDataType()));
		}
		return sb.toString();
	}

	public String produceCategoriesCode(List<Category> categories) {
		CustomStringBuilder sb = new CustomStringBuilder();
		sb.appendln("-- 字典配置");
		UUIDFactory factory = UUIDFactory.getInstance().start();
		for (Category category : categories) {
			category.setId(factory.cost());
			category.setEadpDataType(DBUtils.getEadpDataType());
			int i = 0;
			for (CategoryEntry entry : category) {
				entry.setId(factory.cost());
				entry.setOrders(++i);
				entry.setCategoryId(category.getId());
				entry.setEadpDataType(category.getEadpDataType());
				if (Strings.isNullOrEmpty(entry.getCode())) {
					entry.setCode(factory.cost());
				}
			}
			sb.appendln(category.sql());
		}
		factory.destroy();
		return sb.toString();
	}

	private String produceCategoryCode(Category category) {
		CustomStringBuilder sb = new CustomStringBuilder();
		String categoryName = category.getCategoryName();
		String bizName = category.getBizName();
		sb.appendln(String.format(DELETE_FROM_CFG_CATEGORY_ENTRY, categoryName));
		sb.appendln(String.format(DELETE_FROM_CFG_CATEGORY, categoryName));
		sb.appendln().appendln();
		sb.appendln("-- 表[CFG_CATEGORY]的数据如下:");
		sb.appendln(String.format(INSERT_INTO_CFG_CATEGORY, category.getId(), bizName, categoryName, category.getBizType(), category.getEadpDataType()));
		sb.appendln().appendln();
		sb.appendln("-- 表[CFG_CATEGORY_ENTRY]的数据如下:");
		for (CategoryEntry entry : category) {
			sb.appendln(String.format(INSERT_INTO_CFG_CATEGORY_ENTRY, entry.getId(), entry.getCode(), entry.getCategoryId(), entry.getEadpDataType(), entry.getOrders(), entry.getName()));
		}
		sb.appendln().appendln();

		return sb.toString();
	}

	public String createCategoryStr(String[] keyArray, String[] valueArray, String categoryName, String bizName, String bizType, String version) {
		UUIDFactory uuidFun = UUIDFactory.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(DELETE_FROM_CFG_CATEGORY_ENTRY, categoryName));
		sb.append(String.format(DELETE_FROM_CFG_CATEGORY, categoryName));
		sb.append(StringUtils.CRLF).append(StringUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY]的数据如下:").append(StringUtils.CRLF);
		String cfgCategoryId = uuidFun.cost();
		sb.append(String.format(INSERT_INTO_CFG_CATEGORY, cfgCategoryId, bizName, categoryName, bizType, version));
		sb.append(StringUtils.CRLF).append(StringUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY_ENTRY]的数据如下:").append(StringUtils.CRLF);
		for (int i = 0; i < valueArray.length; i++) {
			if (Strings.isNullOrEmpty(valueArray[i])) {
				break;
			}
			sb.append(String.format(INSERT_INTO_CFG_CATEGORY_ENTRY, uuidFun.cost(),
									(keyArray == null ? uuidFun.cost() : keyArray[i].trim()), cfgCategoryId, version,
									String.valueOf(i + 1), valueArray[i].trim()));
		}
		sb.append(StringUtils.CRLF).append(StringUtils.CRLF);
		uuidFun.destroy();
		return sb.toString();
	}
	
	private static final String DELETE_FROM_CFG_CATEGORY_ENTRY = "DELETE FROM CFG_CATEGORY_ENTRY WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY "
			+ "WHERE CATEGORYNAME='%s');";
	private static final String DELETE_FROM_CFG_CATEGORY = "DELETE FROM CFG_CATEGORY WHERE CATEGORYNAME='%s';";
	private static final String INSERT_INTO_CFG_CATEGORY = "insert into \"CFG_CATEGORY\"(\"ID\",\"BIZNAME\",\"CATEGORYNAME\",\"BIZTYPE\",\"FROMBIZ\","
			+ "\"CANCFG\",\"EADPDATATYPE\",\"BZ\",\"FROMJAVA\") values('%s','%s','%s','%s',0,'0','%s',NULL,NULL);";
	private static final String INSERT_INTO_CFG_CATEGORY_ENTRY = "insert into \"CFG_CATEGORY_ENTRY\"(\"ID\",\"CODE\",\"REMARK\",\"CATEGORYID\","
			+ "\"PARENT_CODE\",\"EADPDATATYPE\",\"ORDERS\",\"CANCFG\",\"CASCADECODE\",\"NAME\",\"NAME_LOCAL\")"
			+ " values('%s','%s',NULL,'%s',NULL,'%s',%d,'0',NULL,'%s',NULL);";
}
