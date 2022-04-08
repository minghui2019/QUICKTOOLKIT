package com.eplugger.onekey.addCategoryEntry.utils;

import com.eplugger.util.OtherUtils;

public class ProduceCategorySqlCode {
	private ProduceCategorySqlCode() {
	}
	public static String createCategoryStr(String[] keyArray, String[] valueArray, String categoryName, String bizName, String bizType, String version) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(DELETE_FROM_CFG_CATEGORY_ENTRY, categoryName));
		sb.append(String.format(DELETE_FROM_CFG_CATEGORY, categoryName));
		sb.append(OtherUtils.CRLF).append(OtherUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY]的数据如下:").append(OtherUtils.CRLF);
		String cfgCategoryId = OtherUtils.getUuid();
		sb.append(String.format(INSERT_INTO_CFG_CATEGORY, cfgCategoryId, bizName, categoryName, bizType, version));
		sb.append(OtherUtils.CRLF).append(OtherUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY_ENTRY]的数据如下:").append(OtherUtils.CRLF);
		for (int i = 0; i < valueArray.length; i++) {
			if (valueArray[i] == null) {
				break;
			}
			sb.append(String.format(INSERT_INTO_CFG_CATEGORY_ENTRY, OtherUtils.getUuid(),
					(keyArray == null ? OtherUtils.getUuid() : keyArray[i].trim()), cfgCategoryId, version,
					String.valueOf(i + 1), valueArray[i].trim()));
		}
		sb.append(OtherUtils.CRLF).append(OtherUtils.CRLF);
		return sb.toString();
	}
	
	private static final String DELETE_FROM_CFG_CATEGORY_ENTRY = "DELETE FROM CFG_CATEGORY_ENTRY WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY "
			+ "WHERE CATEGORYNAME='%s');" + OtherUtils.CRLF;
	private static final String DELETE_FROM_CFG_CATEGORY = "DELETE FROM CFG_CATEGORY WHERE CATEGORYNAME='%s';" + OtherUtils.CRLF;
	private static final String INSERT_INTO_CFG_CATEGORY = "insert into \"CFG_CATEGORY\"(\"ID\",\"BIZNAME\",\"CATEGORYNAME\",\"BIZTYPE\",\"FROMBIZ\","
			+ "\"CANCFG\",\"EADPDATATYPE\",\"BZ\",\"FROMJAVA\") values('%s','%s','%s','%s',0,'0','%s',NULL,NULL);"
			+ OtherUtils.CRLF;
	private static final String INSERT_INTO_CFG_CATEGORY_ENTRY = "insert into \"CFG_CATEGORY_ENTRY\"(\"ID\",\"CODE\",\"REMARK\",\"CATEGORYID\","
			+ "\"PARENT_CODE\",\"EADPDATATYPE\",\"ORDERS\",\"CANCFG\",\"CASCADECODE\",\"NAME\",\"NAME_LOCAL\")"
			+ " values('%s','%s',NULL,'%s',NULL,'%s',%s,'0',NULL,'%s',NULL);" + OtherUtils.CRLF;
}
