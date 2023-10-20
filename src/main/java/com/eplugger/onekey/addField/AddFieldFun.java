package com.eplugger.onekey.addField;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eplugger.common.lang.CustomStringBuilder;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.onekey.utils.entityMeta.EntityMetaField;
import com.eplugger.onekey.utils.entityMeta.FieldEntityMetaFacade;
import com.eplugger.onekey.utils.javaFile.ProduceJavaFactory;
import com.eplugger.onekey.utils.sqlFile.ProduceMetaDataFactory;
import com.eplugger.onekey.utils.sqlFile.ProduceSqlFactory;
import com.eplugger.uuid.UUIDFun;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;
import top.tobak.utils.DateUtils;
import top.tobak.xml.dom4j.utils.ParseXmlUtils;

/**
 * 加字段自动生成java代码，sql命令（数据库类型支持sqlServer），元数据
 * 注：数据库字段数据类型
 * String -- varchar(500),
 * Date -- datetime,
 * Double -- decimal(18,6),
 * Integer -- decimal(18,6)
 * @author minghui
 */
public class AddFieldFun {
	private static final String FILE_OUT_PATH_PARENT = FileUtils.getUserHomeDirectory() + "AddField";
	public static final String FILE_OUT_PATH_FIELD = "src/main/resource/field/Field.xml";
	public static final String FILE_OUT_PATH_MODULETABLE = "src/main/resource/field/ModuleTable.xml";
	public static void main(String[] args) throws Exception {
		createSqlAndJavaFile();
	}
	
	public static void createSqlAndJavaFile() throws Exception {
		ModuleTables moduleTables = ParseXmlUtils.toBean(FILE_OUT_PATH_MODULETABLE, ModuleTables.class);
		Map<String, String> map = moduleTables.getValidModuleTableMap();
		String[] moduleNames = map.keySet().toArray(new String[0]); //模块名
		String[] tableNames = map.values().toArray(new String[0]); //数据库表名
		String[] beanIds = moduleNames; //beanId默认等同模块名
		Fields fields = ParseXmlUtils.toBean(FILE_OUT_PATH_FIELD, Fields.class);
		List<Field> fieldList = fields.getFieldList();

		StringBuilder scsb = new StringBuilder();
		StringBuilder mdsb = new StringBuilder();
		Set<String> set = Sets.newHashSet();
		for (int i = 0; i < moduleNames.length; i++) {
			String tableName = tableNames[i];
			String beanId = beanIds[i];
			String metadata = ProduceMetaDataFactory.getInstance().produceMetadata(beanId, fieldList);
			mdsb.append(metadata);
			if (set.contains(tableName)) {
				continue;
			}
			String sqlCode = ProduceSqlFactory.getInstance().produceSqlCode(tableName, fieldList);
			scsb.append(sqlCode);
			set.add(tableName);
		}
		String javaCode = ProduceJavaFactory.getInstance().produceEntityJavaCode(fieldList);
		UUIDFun.getInstance().destroyUuids();
		
		String today = DateUtils.formatDate();
		String dateFm = DateUtils.formatDateNoSeparator();
		FileUtils.writeAndBackupSrcFile(FILE_OUT_PATH_PARENT + File.separator + dateFm + File.separator + today + ".java", javaCode);
		
		scsb.append(StringUtils.CRLF);
		FileUtils.writeAndBackupSrcFile(FILE_OUT_PATH_PARENT + File.separator + dateFm + File.separator + today + ".sql", scsb.toString() + mdsb.toString());
		
		FileUtils.openTaskBar(new File(FILE_OUT_PATH_PARENT + File.separator + dateFm));
	}

	public static final String ITEM_TYPE_MONEY = "money-元";// 金额
	public static final String ITEM_TYPE_GREAT_MONEY = "money-万元";// 金额

    public static void createUpdateSceneEntityMetas(EntityMetaField... fields) {
		createUpdateSceneEntityMetas(Lists.newArrayList(fields));
    }

    private static void createUpdateSceneEntityMetas(List<EntityMetaField> fields) {
		List<FieldEntityMetaFacade> fieldFacades = register(fields);
		CustomStringBuilder csb = new CustomStringBuilder();
		for (FieldEntityMetaFacade fieldFacade : fieldFacades) {
			csb.appendln(fieldFacade.toString());
		}
		String today = DateUtils.formatDate();
		String dateFm = DateUtils.formatDateNoSeparator();
		FileUtils.writeAndBackupSrcFile(FileUtils.getUserHomeDirectory() + "entityMeta" + File.separator + dateFm + File.separator + today + ".sql", csb.toString());
		FileUtils.openTaskBar(new File(FileUtils.getUserHomeDirectory() + "entityMeta" + File.separator + dateFm));
	}

	private static List<FieldEntityMetaFacade> register(List<EntityMetaField> fields) {
		List<FieldEntityMetaFacade> fieldFacades = Lists.newArrayList();
		for (EntityMetaField field : fields) {
			fieldFacades.add(register(field));
		}
		return fieldFacades;
	}

	private static FieldEntityMetaFacade register(EntityMetaField field) {
		return new FieldEntityMetaFacade(field);
	}
}
