package com.eplugger.onekey.excelFile;

import java.io.File;

import top.tobak.common.lang.StringUtils;
import top.tobak.utils.ExcelUtils;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class AutoExcelFileFun {

    public static void transferExcelText() {

        Workbook workbook = ExcelUtils.openWorkbook(AutoExcelFileFun.class.getResource("/").getPath() + "testText.xls", false);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i); // 遍历拿每一个sheet
            for (Row row : sheet) {
                String value = row.getCell(0).getStringCellValue();
                row.createCell(1).setCellValue(StringUtils.getFirstSpell(value, HanyuPinyinCaseType.LOWERCASE));
            }
        }
        ExcelUtils.outExcel(workbook, new File(AutoExcelFileFun.class.getResource("/").getPath() + "/testText.xls"));
    }
}
