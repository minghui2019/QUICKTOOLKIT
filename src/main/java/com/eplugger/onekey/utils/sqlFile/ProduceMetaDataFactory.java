package com.eplugger.onekey.utils.sqlFile;

import java.util.List;
import java.util.stream.Collectors;

import com.eplugger.common.lang.CustomStringBuilder;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.factory.AbstractProduceCodeFactory;
import com.eplugger.onekey.utils.SqlUtils;
import com.eplugger.utils.DBUtils;
import com.eplugger.uuid.UUIDFactory;
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
		UUIDFactory factory = UUIDFactory.getInstance().start();
		String eadpDataType = DBUtils.getEadpDataType();
		for (Field field : fields) {
			field.setOrders(++orders);
			field.setId(factory.cost());
			field.setEadpDataType(eadpDataType);
			field.setBeanId(beanId);
		}
		factory.destroy();
		return produceMetadata(fields);
	}

	public String produceMetadata(List<Field> fields) {
		CustomStringBuilder sb = new CustomStringBuilder();
		String uuidStr = fields.stream().map(f -> "'" + f.getId() + "'").collect(Collectors.joining(","));
		sb.appendln("-- 元数据");
		sb.append("delete from SYS_ENTITY_META WHERE id in (").append(uuidStr).append(");").appendln();
		sb.appendln("-- 表[SYS_ENTITY_META]的数据如下:");
		for (Field field : fields) {
			sb.append(field.sql());
		}
		sb.appendln();
		return sb.toString();
	}
}
