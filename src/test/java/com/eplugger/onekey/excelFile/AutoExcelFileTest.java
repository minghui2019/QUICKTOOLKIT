package com.eplugger.onekey.excelFile;

import java.util.List;

import com.eplugger.enums.BizType;
import com.eplugger.enums.FromBiz;
import com.eplugger.onekey.entity.Categories;
import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.entity.CategoryMapping;
import com.eplugger.onekey.entity.ISqlBizEntity;
import com.eplugger.onekey.excelFile.entity.CategoryRegion;
import com.eplugger.onekey.excelFile.entity.CategorySubType;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.utils.DBUtils;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

public class AutoExcelFileTest {
    @Before
    public void testSetSchoolCode() {
        DBUtils.schoolInfo = SchoolInfo.广东工业大学;
    }

    @Test
    public void createCategories() {
        Categories categories = new Categories();
        Category category1 = new Category();
        category1.setId("category1");
        category1.setCategoryName("DM_REGION");
        category1.setBizName("区域");
        category1.setBizType(BizType.项目.name());
        category1.setFromBiz(FromBiz.业务表.code());
        category1.setTableName("DM_REGION");
        category1.setEadpDataType(DBUtils.getEadpDataType());

        CategoryMapping categoryMapping1 = new CategoryMapping();
        categoryMapping1.setId("categoryMapping1");
        categoryMapping1.setTableName("DM_REGION");
        categoryMapping1.setCodeColumn("REGION_ID");
        categoryMapping1.setValueColumn("REGION_NAME");
        categoryMapping1.setValueColumnLocal("REGION_NAME_LOCAL");
        categoryMapping1.setCategoryId(category1.getId());
        categoryMapping1.setUpCodeColumn("UP_REGION_ID");
        categoryMapping1.setEadpDataType(DBUtils.getEadpDataType());
        category1.setCategoryMapping(categoryMapping1);
        categories.addCategory(category1);
        Category category2 = new Category();
        category2.setId("category2");
        category2.setCategoryName("DM_SUB_TYPE");
        category2.setBizName("子类别");
        category2.setBizType(BizType.项目.name());
        category2.setFromBiz(FromBiz.业务表.code());
        category2.setTableName("DM_SUB_TYPE");
        category2.setEadpDataType(DBUtils.getEadpDataType());
        CategoryMapping categoryMapping2 = new CategoryMapping();
        categoryMapping2.setId("categoryMapping2");
        categoryMapping2.setTableName("DM_SUB_TYPE");
        categoryMapping2.setCodeColumn("CODE");
        categoryMapping2.setValueColumn("NAME");
        categoryMapping2.setValueColumnLocal("NAME_LOCAL");
        categoryMapping2.setCategoryId(category2.getId());
        categoryMapping2.setEadpDataType(DBUtils.getEadpDataType());
        category2.setCategoryMapping(categoryMapping2);
        categories.addCategory(category2);
        List<Class<? extends ISqlBizEntity>> clzs = Lists.newArrayList();
        clzs.add(CategoryRegion.class);
        clzs.add(CategorySubType.class);
        AutoExcelFile.createCategories("../../xlsx/区域和子类别字典.xlsx", categories, clzs);
    }

    @Test
    public void transferExcelText() {
        AutoExcelFile.transferExcelText();
    }
}