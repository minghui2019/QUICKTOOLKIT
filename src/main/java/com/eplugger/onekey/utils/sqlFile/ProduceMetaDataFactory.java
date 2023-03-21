package com.eplugger.onekey.utils.sqlFile;

import java.util.List;
import java.util.stream.Collectors;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.factory.AbstractProduceCodeFactory;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;
import com.eplugger.uuid.UUIDFun;
import com.google.common.base.Strings;

public class ProduceMetaDataFactory extends AbstractProduceCodeFactory {
	private static class ProduceMetaDataFactorySingleton {
		private static final ProduceMetaDataFactory FACTORY = new ProduceMetaDataFactory(); 
	}

	public static ProduceMetaDataFactory getInstance() {
		return ProduceMetaDataFactorySingleton.FACTORY;
	}
	private ProduceMetaDataFactory() { }

	/**
	 * 生成元数据
	 * @param beanId
	 * @param fieldList
	 * @return
	 */
	public String produceMetadata(String beanId, List<Field> fieldList) {
		int orders = SqlUtils.getMaxOrdersByBeanId(beanId);//元数据排序号，默认1
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
			.append(this.getMetadataDataType(field.getDataType())).append("',").append("'")
			.append(eadpDataType).append("','").append(field.getBusinessFilterType()).append("','use');").append(StringUtils.CRLF);
		}
		sb.append(StringUtils.CRLF);
		return sb.toString();
	}
	
	/**
	 * 元数据数据类型
	 * @param javaDataType java数据类型
	 * @return
	 */
	public String getMetadataDataType(String javaDataType) {
		String result = "";
		switch (javaDataType) {
		case OtherUtils.TPYE_STRING:
			result = "string";
			break;
		case OtherUtils.TPYE_TIMESTAMP:
		case OtherUtils.TPYE_DATE:
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

}
