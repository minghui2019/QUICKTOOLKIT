package com.eplugger.onekey.utils.sqlFile;

import java.io.File;
import java.util.List;

import com.eplugger.common.io.FileUtils;
import com.eplugger.onekey.addModule.Constants;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.Module;

public class ProduceSqlFiles {

	public static void produceCreateTableSqlFiles(Module module) {
		List<Field> fields = module.getMainModule().getFieldList();
		String key = module.getMainModule().getSuperClassMap().get("entity");
		List<Field> addSuperClassFields = Constants.addSuperClassFields(key);
		if (addSuperClassFields != null) {
			fields.addAll(addSuperClassFields);
		}
		String sqlCode = ProduceSqlFactory.getInstance().produceCreateTableSql(module.getMainModule(), false, null, null);
		
		String metadata = ProduceMetaDataFactory.getInstance().produceMetadata(module.getMainModule().getBeanId(), fields);
		
		FileUtils.write(FileUtils.getUserHomeDirectory() + "AddListModule\\sql" + File.separator + module.getMainModule().getModuleName() + ".SQL", sqlCode + metadata);
	}
}
