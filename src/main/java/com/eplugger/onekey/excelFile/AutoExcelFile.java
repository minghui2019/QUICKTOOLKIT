package com.eplugger.onekey.excelFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baidu.translate.service.TransService;
import com.eplugger.common.lang.CustomStringBuilder;
import com.eplugger.onekey.entity.Categories;
import com.eplugger.onekey.entity.ISqlBizEntity;
import com.eplugger.trans.CharMatcherHandlerFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;
import top.tobak.poi.excel.ExcelReader;
import top.tobak.utils.ExcelUtils;

public class AutoExcelFile {

    public static void transferExcelText() {
        Workbook workbook = ExcelUtils.openWorkbook(AutoExcelFile.class.getResource("/").getPath() + "testText.xls", false);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i); // 遍历拿每一个sheet
            for (Row row : sheet) {
                String value = row.getCell(0).getStringCellValue();
                row.createCell(1).setCellValue(StringUtils.getFirstSpell(value, HanyuPinyinCaseType.LOWERCASE));
            }
        }
        ExcelUtils.outExcel(workbook, new File(AutoExcelFile.class.getResource("/").getPath() + "/testText.xls"));
    }

    public static void createCategories(String filePath, Categories categories, List<Class<? extends ISqlBizEntity>> clzs) {
        ExcelReader reader = ExcelUtils.getReader(filePath);
        CustomStringBuilder sbInsert = new CustomStringBuilder();
        CustomStringBuilder sbUpdate = new CustomStringBuilder();
        for (int i = 0; i < clzs.size(); i++) {
            reader.setSheet(i);
            Class<ISqlBizEntity> clz = (Class<ISqlBizEntity>) clzs.get(i);
            List<ISqlBizEntity> sqlEntityList = reader.readAll(clz);
            List<String> query = sqlEntityList.stream().map(e -> e.getName()).distinct().collect(Collectors.toList());
            Map<String, String> zh2En = TransService.transTextZh2En(query);
            for (Entry<String, String> entry : zh2En.entrySet()) {
                String result = CharMatcherHandlerFactory.getFactory().matcherChar(entry.getValue().trim());
                entry.setValue(result);
            }
            List<String> insertSql = Lists.newArrayList();
            List<String> updateSql = Lists.newArrayList();

            List<ISqlBizEntity> parent = sqlEntityList.stream().filter(e -> Strings.isNullOrEmpty(e.getParentCode())).sorted().collect(Collectors.toList());
            Map<String, List<ISqlBizEntity>> childs = sqlEntityList.stream().filter(e -> !Strings.isNullOrEmpty(e.getParentCode())).collect(Collectors.groupingBy(ISqlBizEntity::getParentCode));
            for (ISqlBizEntity sqlEntity : parent) {
                List<ISqlBizEntity> sqlEntities = childs.get(sqlEntity.getCode());
                sqlEntity.convertCode();
                sqlEntity.setNameLocal(zh2En.get(sqlEntity.getName()));
                insertSql.add(sqlEntity.sql());
                updateSql.add(sqlEntity.updateSql());
                if (sqlEntities == null) {
                    continue;
                }
                int j = 0;
                for (ISqlBizEntity entity : sqlEntities) {
                    entity.setCode(String.valueOf(++j));
                    entity.convertCode();
                    entity.setNameLocal(zh2En.get(entity.getName()));
                    insertSql.add(entity.sql());
                    updateSql.add(entity.updateSql());
                }
            }

            insertSql.forEach(sbInsert::appendln);
            updateSql.forEach(sbUpdate::appendln);
            sbInsert.appendln();
            sbInsert.appendln();
            sbUpdate.appendln();
            sbUpdate.appendln();
        }
        String fileName = "字典配置SQL" + DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_FORMAT) + ".sql";
        FileUtils.write(FileUtils.getUserHomeDirectory() + "Category\\" + fileName, sbInsert.toString() + sbUpdate.toString() + categories.sql());
        FileUtils.openTaskBar(new File(FileUtils.getUserHomeDirectory() + "Category\\"));
    }

    public static String convertCode(String parentCode, String code) {
        parentCode = Strings.nullToEmpty(parentCode);
        if (parentCode.length() == 1) {
            parentCode = "0" + parentCode;
        }
        if (code.length() == 1) {
            code = "0" + code;
        }
        if (parentCode.length() == 0) {
            code += "00";
        }
        return parentCode + code;
    }

    public static String convertCode(String parentCode, String code, boolean isParent) {
        if (isParent) {
            code += "00";
        }
        return parentCode + code;
    }
}
