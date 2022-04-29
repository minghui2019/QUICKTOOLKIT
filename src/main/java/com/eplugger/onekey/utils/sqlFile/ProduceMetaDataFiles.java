package com.eplugger.onekey.utils.sqlFile;

import java.util.List;
import java.util.stream.Collectors;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;
import com.eplugger.uuid.UUIDFun;
import com.google.common.base.Strings;

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
		List<Field> fields = fieldList.stream().filter(f -> Strings.isNullOrEmpty(f.getAssociation())).collect(Collectors.toList());
		List<String> uuidList = UUIDFun.getInstance().getUuidsList(fields.size());
		String uuidStr = uuidList.stream().map(u -> "'" + u + "'").collect(Collectors.joining(", "));
		
		StringBuffer sb = new StringBuffer();
		String eadpDataType = DBUtils.getEadpDataType();
		
		sb.append("delete from SYS_ENTITY_META WHERE id in (").append(uuidStr).append(");").append(StringUtils.CRLF).append(StringUtils.CRLF);
		sb.append("-- 表[SYS_ENTITY_META]的数据如下:").append(StringUtils.CRLF);
		int i = 0;
		for (Field field : fields) {
			sb.append("insert into \"SYS_ENTITY_META\"(\"ID\",\"BEANID\",\"CATEGORYNAME\",\"ORDERS\",\"MEANING\",\"NAME\",\"DATA_TYPE\",")
			.append("\"EADPDATATYPE\",\"BUSINESSFILTERTYPE\",\"USESTATE\") values('")
			.append(uuidList.get(i++)).append("','").append(beanId).append("',")
			.append(field.getCategoryName() == null ? "NULL" : "'" + field.getCategoryName() + "'")
			.append(",").append(++orders).append(",'").append(field.getFieldName()).append("','")
			.append(field.getFieldId()).append("','")
			.append(getMetadataDataType(field.getDataType())).append("',").append("'")
			.append(eadpDataType).append("','no','use');").append(StringUtils.CRLF);
		}
		sb.append(StringUtils.CRLF);
		return sb.toString();
	}

	public static int getMaxOrdersByBeanId(String beanId) {
		String sql = "";
		if (DBUtils.isSqlServer()) {
			sql = "SELECT top 1 ORDERS FROM SYS_ENTITY_META WHERE BEANID='" + beanId + "' ORDER BY ORDERS DESC;";
		} else if (DBUtils.isOracle()) {
			sql = "SELECT ORDERS FROM(SELECT ORDERS FROM SYS_ENTITY_META WHERE BEANID='" + beanId + "' ORDER BY ORDERS DESC) WHERE ROWNUM=1";
		}
		return DBUtils.getOrdersFromEntityMeta(sql);
	}
}
