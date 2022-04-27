package com.eplugger.onekey.addCategoryEntry.utils;

import java.io.IOException;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.uuid.UUIDFun;

public class ProduceCategorySqlCode {
	private ProduceCategorySqlCode() {
	}
	public static String createCategoryStr(String[] keyArray, String[] valueArray, String categoryName, String bizName, String bizType, String version) {
		UUIDFun uuidFun = UUIDFun.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(DELETE_FROM_CFG_CATEGORY_ENTRY, categoryName));
		sb.append(String.format(DELETE_FROM_CFG_CATEGORY, categoryName));
		sb.append(StringUtils.CRLF).append(StringUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY]的数据如下:").append(StringUtils.CRLF);
		String cfgCategoryId = uuidFun.getUuid();
		sb.append(String.format(INSERT_INTO_CFG_CATEGORY, cfgCategoryId, bizName, categoryName, bizType, version));
		sb.append(StringUtils.CRLF).append(StringUtils.CRLF);
		sb.append("-- 表[CFG_CATEGORY_ENTRY]的数据如下:").append(StringUtils.CRLF);
		for (int i = 0; i < valueArray.length; i++) {
			if (valueArray[i] == null) {
				break;
			}
			sb.append(String.format(INSERT_INTO_CFG_CATEGORY_ENTRY, uuidFun.getUuid(),
					(keyArray == null ? uuidFun.getUuid() : keyArray[i].trim()), cfgCategoryId, version,
					String.valueOf(i + 1), valueArray[i].trim()));
		}
		sb.append(StringUtils.CRLF).append(StringUtils.CRLF);
		try {
			uuidFun.destroyUuids();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private static final String DELETE_FROM_CFG_CATEGORY_ENTRY = "DELETE FROM CFG_CATEGORY_ENTRY WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY "
			+ "WHERE CATEGORYNAME='%s');" + StringUtils.CRLF;
	private static final String DELETE_FROM_CFG_CATEGORY = "DELETE FROM CFG_CATEGORY WHERE CATEGORYNAME='%s';" + StringUtils.CRLF;
	private static final String INSERT_INTO_CFG_CATEGORY = "insert into \"CFG_CATEGORY\"(\"ID\",\"BIZNAME\",\"CATEGORYNAME\",\"BIZTYPE\",\"FROMBIZ\","
			+ "\"CANCFG\",\"EADPDATATYPE\",\"BZ\",\"FROMJAVA\") values('%s','%s','%s','%s',0,'0','%s',NULL,NULL);"
			+ StringUtils.CRLF;
	private static final String INSERT_INTO_CFG_CATEGORY_ENTRY = "insert into \"CFG_CATEGORY_ENTRY\"(\"ID\",\"CODE\",\"REMARK\",\"CATEGORYID\","
			+ "\"PARENT_CODE\",\"EADPDATATYPE\",\"ORDERS\",\"CANCFG\",\"CASCADECODE\",\"NAME\",\"NAME_LOCAL\")"
			+ " values('%s','%s',NULL,'%s',NULL,'%s',%s,'0',NULL,'%s',NULL);" + StringUtils.CRLF;
}
