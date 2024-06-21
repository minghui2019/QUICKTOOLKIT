package com.eplugger.onekey.viewFile;

import java.util.List;
import java.util.Map;

import com.eplugger.onekey.entity.Categories;
import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.viewFile.entity.ModuleView;
import com.eplugger.onekey.viewFile.entity.ModuleViews;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.tobak.utils.ExcelUtils;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

public class AutoViewFileConstant {
    public static Map<String, ModuleView> moduleViewMap = Maps.newHashMap(); // key:表名,value:视图名
    public static Map<String, Category> categoryMap = Maps.newHashMap(); // key:表名,value:视图名
    public static Map<String, List<String[]>> defaultViewMap = Maps.newHashMap(); // key:表名,value:视图名

    public static void initModuleViewMap() {
        if (moduleViewMap != null && !moduleViewMap.isEmpty()) {
            return;
        }
        ModuleViews moduleViews = AutoViewFileConstant.getModuleViews();
        moduleViewMap = moduleViews.getValidModuleViewMap();
    }


    public static void initCategoryMap() {
        if (categoryMap != null && !categoryMap.isEmpty()) {
            return;
        }
        Categories categories = AutoViewFileConstant.getCategories();
        categoryMap = categories.getCategoryMap();
    }

    public static void initDefaultViewMap() {
        Workbook workbook = ExcelUtils.openWorkbook("src/main/resource/view", "视图共享.xlsx");
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i); // 遍历拿每一个sheet
            String viewName = sheet.getSheetName(); // 表名
            List<String[]> rows = Lists.newArrayList();
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    continue;
                }
                rows.add(new String[] { row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue() });
            }
            defaultViewMap.put(viewName, rows);
        }
    }

    public static ModuleViews getModuleViews() {
        try {
            ModuleViews moduleViews = XmlParseUtils.toBean(ModuleViews.class, "src/main/resource/view/ModuleView.xml");
            return moduleViews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleViews();
    }

    public static Categories getCategories() {
        try {
            Categories categories = XmlParseUtils.toBean(Categories.class, "src/main/resource/view/Category.xml");
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Categories();
    }
}
