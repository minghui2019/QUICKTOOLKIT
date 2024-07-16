package com.eplugger.onekey.excelFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eplugger.onekey.entity.Categories;
import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.entity.ISqlBizEntity;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
        int i = 0;
//        List<String> categorySql = Lists.newArrayList();
        for (Category category : categories) {
            Class<ISqlBizEntity> clz = (Class<ISqlBizEntity>) clzs.get(i);
            List<ISqlBizEntity> sqlEntityList = reader.readAll(clz);
//            List<String> query = sqlEntityList.stream().map(e -> e.getName()).distinct().collect(Collectors.toList());
//            Map<String, String> zh2En = TransService.transTextZh2En(query);
            Map<String, String> zh2En = Maps.newHashMap();
            System.out.println(zh2En);
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
            i++;
            reader.setSheet(i);

            insertSql.forEach(System.out::println);
            updateSql.forEach(System.out::println);
//            categorySql.add(category.sql());
        }
        System.out.println(categories.sql());
//        categorySql.forEach(System.out::println);
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
