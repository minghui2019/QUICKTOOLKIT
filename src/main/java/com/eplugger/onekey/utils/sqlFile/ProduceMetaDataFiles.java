package com.eplugger.onekey.utils.sqlFile;

import java.util.List;

import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.util.DBUtil;
import com.eplugger.util.OtherUtils;

public class ProduceMetaDataFiles {
	/**
	 * 元数据数据类型
	 * @param javaDataType java数据类型
	 * @return
	 */
	public static String getMetadataDataType(String javaDataType) {
		String result = "";
		switch (javaDataType) {
		case OtherUtils.TPYE_STRING:
			result = "string";
			break;
		case OtherUtils.TPYE_DATE:
			result = "date";
			break;
		case OtherUtils.TPYE_TIMESTAMP:
			result = "date";
			break;
		case OtherUtils.TPYE_DOUBLE:
			result = "float";
			break;
		case OtherUtils.TPYE_INTEGER:
			result = "int";
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String produceMetadata(String beanId, List<Field> fieldList) {
		int orders = ProduceMetaDataFiles.getMaxOrdersByBeanId(beanId);//元数据排序号，默认1
		int size = fieldList.size();
		String[] uuids = new String[size];
		StringBuffer uuidSb = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		String eadpDataType = DBUtil.getEadpDataType();
		for (int i = 0; i < size; i++) {
			if (StringUtils.isNotBlank(fieldList.get(i).getAssociation())) {
				continue;
			}
			uuids[i] = OtherUtils.getUuid();
			uuidSb.append("'").append(uuids[i]).append("'");
			if (i < size - 1) {
				uuidSb.append(",");
			}
		}
		sb.append("delete from SYS_ENTITY_META WHERE id in (").append(uuidSb.toString()).append(");").append(OtherUtils.CRLF).append(OtherUtils.CRLF);
		sb.append("-- 表[SYS_ENTITY_META]的数据如下:").append(OtherUtils.CRLF);
		for (int i = 0; i < size; i++) {
			Field field = fieldList.get(i);
			if (StringUtils.isNotBlank(field.getAssociation())) {
				continue;
			}
			sb.append("insert into \"SYS_ENTITY_META\"(\"ID\",\"BEANID\",\"CATEGORYNAME\",\"ORDERS\",\"MEANING\",\"NAME\",\"DATA_TYPE\",")
					.append("\"EADPDATATYPE\",\"BUSINESSFILTERTYPE\",\"USESTATE\") ").append("values('")
					.append(uuids[i]).append("','").append(beanId).append("',")
					.append(field.getCategoryName() == null ? "NULL" : "'" + field.getCategoryName() + "'")
					.append(",").append((++orders)).append(",'").append(field.getFieldName()).append("','")
					.append(field.getFieldId()).append("','")
					.append(getMetadataDataType(field.getDataType())).append("',").append("'")
					.append(eadpDataType).append("','no','use');").append(OtherUtils.CRLF);
		}
		sb.append(OtherUtils.CRLF);
		return sb.toString();
	}

	public static int getMaxOrdersByBeanId(String beanId) {
		String sql = "";
		if (DBUtil.isSqlServer()) {
			sql = "SELECT top 1 ORDERS FROM SYS_ENTITY_META WHERE BEANID='" + beanId + "' ORDER BY ORDERS DESC;";
		} else if (DBUtil.isOracle()) {
			sql = "SELECT ORDERS FROM(SELECT ORDERS FROM SYS_ENTITY_META WHERE BEANID='" + beanId + "' ORDER BY ORDERS DESC) WHERE ROWNUM=1";
		}
		return DBUtil.getOrdersFromEntityMeta(sql);
	}
}
