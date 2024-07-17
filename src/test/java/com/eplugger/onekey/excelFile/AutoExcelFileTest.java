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
import com.eplugger.uuid.UUIDFactory;
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
        UUIDFactory uuid = UUIDFactory.getInstance().start();
        Categories categories = new Categories();
        Category category1 = new Category("DM_REGION1","区域1", BizType.项目.name(), FromBiz.业务表.code());
        category1.setId(uuid.cost());
        category1.setEadpDataType(DBUtils.getEadpDataType());
        CategoryMapping categoryMapping1 = new CategoryMapping("DM_REGION", "REGION_ID", "REGION_NAME", "REGION_NAME_LOCAL", category1.getId());
        categoryMapping1.setId(uuid.cost());
        categoryMapping1.setCascadeColumn("UP_REGION_ID");
        categoryMapping1.setWhereSql("UP_REGION_ID is null");
        categoryMapping1.setOrders("REGION_ID asc");
        categoryMapping1.setEadpDataType(DBUtils.getEadpDataType());
        category1.setCategoryMapping(categoryMapping1);
        categories.addCategory(category1);

        Category category3 = new Category("DM_REGION2","区域2", BizType.项目.name(), FromBiz.业务表.code());
        category3.setId(uuid.cost());
        category3.setEadpDataType(DBUtils.getEadpDataType());
        CategoryMapping categoryMapping3 = new CategoryMapping("DM_REGION", "REGION_ID", "REGION_NAME", "REGION_NAME_LOCAL", category3.getId());
        categoryMapping3.setId(uuid.cost());
        categoryMapping3.setCascadeColumn("UP_REGION_ID");
        categoryMapping3.setWhereSql("UP_REGION_ID is not null");
        categoryMapping3.setOrders("REGION_ID asc");
        categoryMapping3.setEadpDataType(DBUtils.getEadpDataType());
        category3.setCategoryMapping(categoryMapping3);
        categories.addCategory(category3);

        Category category2 = new Category("DM_SUB_TYPE","子类别", BizType.项目.name(), FromBiz.业务表.code());
        category2.setId(uuid.cost());
        category2.setEadpDataType(DBUtils.getEadpDataType());
        CategoryMapping categoryMapping2 = new CategoryMapping("DM_SUB_TYPE", "CODE", "NAME", "NAME_LOCAL", category2.getId());
        categoryMapping2.setId(uuid.cost());
        categoryMapping2.setOrders("CODE asc");
        categoryMapping2.setEadpDataType(DBUtils.getEadpDataType());
        category2.setCategoryMapping(categoryMapping2);
        categories.addCategory(category2);
        List<Class<? extends ISqlBizEntity>> clzs = Lists.newArrayList();
        clzs.add(CategoryRegion.class);
        clzs.add(CategorySubType.class);
        AutoExcelFile.createCategories("../../xlsx/区域和子类别字典.xlsx", categories, clzs);
        uuid.stop().destroy();
    }

    @Test
    public void transferExcelText() {
        AutoExcelFile.transferExcelText();
    }
}