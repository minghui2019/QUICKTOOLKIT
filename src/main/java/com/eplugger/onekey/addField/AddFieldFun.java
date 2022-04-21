package com.eplugger.onekey.addField;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.addField.entity.Field;
import com.eplugger.onekey.addField.util.FieldParse;
import com.eplugger.onekey.addField.util.ModuleTableParse;
import com.eplugger.onekey.utils.javaFile.ProduceJavaFiles;
import com.eplugger.onekey.utils.sqlFile.ProduceMetaDataFiles;
import com.eplugger.onekey.utils.sqlFile.ProduceSqlFiles;
import com.eplugger.utils.DateUtils;

/**
 * 加字段自动生成java代码，sql命令（数据库类型支持sqlServer），元数据
 * 注：数据库字段数据类型
 * String -- varchar(500),
 * Date -- datetime,
 * Double -- decimal(18,6),
 * Integer -- decimal(18,6)
 * @author Admin
 *
 */
public class AddFieldFun {
	private static final String FILE_OUT_PATH = "C:/Users/Admin/Desktop/AddField";
	public static void main(String[] args) {
		Map<String, String> map = ModuleTableParse.getInstance().getValidModuleTableMap("src/main/resource/field/ModuleTable.xml");
		String[] moduleNames = map.keySet().toArray(new String[] {}); //模块名
		String[] tableNames = map.values().toArray(new String[] {}); //数据库表名
		String[] beanIds = moduleNames; //beanId默认等同模块名
		List<Field> fieldList = FieldParse.getInstance().getValidList("src/main/resource/field/Field.xml");

//		String version = "V8.2.3sp3";//eadp版本
		
		StringBuilder scsb = new StringBuilder();
		StringBuilder mdsb = new StringBuilder();
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < moduleNames.length; i++) {
			String tableName = tableNames[i];
			String beanId = beanIds[i];
			String metadata = ProduceMetaDataFiles.produceMetadata(beanId, fieldList);
			mdsb.append(metadata);
			if (set.contains(tableName)) {
				continue;
			}
			String sqlCode = ProduceSqlFiles.produceSqlCode(tableName, fieldList);
			scsb.append(sqlCode);
			set.add(tableName);
		}
		String javaCode = ProduceJavaFiles.produceEntityJavaCode(fieldList);
		String today = DateUtils.formatDate();
		String dateFm = DateUtils.formatDateNoSeparator();
		FileUtils.writeAndBackupSrcFile(FILE_OUT_PATH + File.separator + dateFm + File.separator + today + ".java", javaCode);
		
		scsb.append(StringUtils.CRLF);
		FileUtils.writeAndBackupSrcFile(FILE_OUT_PATH + File.separator + dateFm + File.separator + today + ".sql", scsb.toString() + mdsb.toString());
		
		try {
			Desktop.getDesktop().open(new File(FILE_OUT_PATH + File.separator + dateFm));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
