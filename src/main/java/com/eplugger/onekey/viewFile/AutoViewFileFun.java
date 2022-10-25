package com.eplugger.onekey.viewFile;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.viewFile.entity.ModuleView;
import com.eplugger.onekey.viewFile.entity.ModuleViews;
import com.eplugger.onekey.viewFile.entity.ViewRowBuilder;
import com.eplugger.onekey.viewFile.entity.ViewRowBuilder.ViewRow;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.ExcelUtils;
import com.eplugger.utils.OtherUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Slf4j
public class AutoViewFileFun {
    public static Map<String, String> tableList = Maps.newLinkedHashMap(); // key:表名,value:视图名
    public static Map<String, ModuleView> moduleViewMap = Maps.newHashMap(); // key:表名,value:视图名
    private static String filePath = FileUtils.getUserHomeDirectory() + "视图";
    private static String xlsName = "共享视图.xlsx";

    private static void initModuleViewMap() {
        if (moduleViewMap != null && !moduleViewMap.isEmpty()) {
            return;
        }
        ModuleViews moduleViews = AutoViewFileConstant.getModuleViews();
        moduleViewMap = moduleViews.getValidModuleViewMap();
    }

    /**
     * 自动生成视图语句
     */
    public static void generationViewSql() {
        initModuleViewMap();
        Workbook workbook = ExcelUtils.openWorkbook(filePath + File.separator + "xls" + File.separator, xlsName);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i); // 遍历拿每一个sheet
            String tableName = sheet.getSheetName(); // 表名
            ModuleView moduleView = moduleViewMap.get(tableName);
            String viewName = moduleView.getViewName(); // 视图名
            List<ViewRow> viewRows = Lists.newArrayList();
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    continue;
                }
                if (row.getCell(1) == null || Strings.isNullOrEmpty(row.getCell(1).toString())) { // 如果当前行没有数据，跳出循环
                    break;
                }
                ViewRow viewRow = new ViewRowBuilder().setLieMing(row.getCell(0)).setBieMing(row.getCell(1)).setNullValue(row.getCell(2))
                        .setOwnDictionary(row.getCell(3)).setJoinTable(row.getCell(4)).setNote(row.getCell(5)).getViewRow();
                viewRows.add(viewRow);
            }
            generationViewSql(viewName, tableName, viewRows);
        }
    }

    private static void generationViewSql(String viewName, String tableName, List<ViewRow> viewRows) {
        StringBuilder sb = new StringBuilder();
        sb.append("create view ").append(viewName).append(StringUtils.CRLF).append("as").append(StringUtils.CRLF).append("(SELECT").append(StringUtils.CRLF);
        int i = 0, size = viewRows.size();
        for (ViewRow viewRow : viewRows) {
            i++;
            String key = viewRow.getLieMing();
            String value = viewRow.getBieMing();
            if (!Strings.isNullOrEmpty(viewRow.getOwnDictionary())) { // 有字典的属性
                String str = "V8.1.1".equals(DBUtils.getEadpDataType()) ? "\"VALUE\"" : "\"NAME\"";
                sb.append("(SELECT CFG_CATEGORY_ENTRY.").append(str).append(" FROM CFG_CATEGORY_ENTRY,CFG_CATEGORY WHERE CFG_CATEGORY.CATEGORYNAME='")
                        .append(viewRow.getOwnDictionary()).append("' AND CFG_CATEGORY.ID=CFG_CATEGORY_ENTRY.CATEGORYID AND ")
                        .append(tableName).append(".").append(key).append("=CFG_CATEGORY_ENTRY.CODE) ");// + " AND " + tableName + "." + key + "<>NULL) "
                sb.append(value == null ? key : value); // 别名为空，用字段名代替
            } else if (!Strings.isNullOrEmpty(viewRow.getJoinTable())) { // 关联表
                String[] str = viewRow.getJoinTable().split(",");
                if ("PAPER_LEVEL_ID".equals(key) || "EMBODY_TYPE_ID".equals(key)) {
                    if (DBUtils.isOracle()) {
                        sb.append("(SELECT listagg(NAME, ',') WITHIN GROUP(ORDER BY ID) FROM ").append(str[0])
                                .append(" where INSTR(").append(tableName).append(".").append(key).append(", ")
                                .append(str[0]).append(".").append(str[1]).append(")>0");
                    } else if (DBUtils.isSqlServer()) {
                        sb.append("stuff((SELECT ',' + NAME FROM ").append(str[0])
                                .append(" WHERE CHARINDEX(").append(str[0]).append(".").append(str[1]).append(", ")
                                .append(tableName).append(".").append(key).append(")>0 for xml path('')),1,1,''");
                    }
                } else {
                    sb.append("(SELECT NAME FROM ").append(str[0])
                            .append(" WHERE ").append(str[0]).append(".").append(str[1]).append("=").append(tableName).append(".").append(key);
                    if (StringUtils.equalsIgnoreCase(key, "PROJECT_SOURCE_ID")) {
                        sb.append(" AND DM_PROJECT_STAT_SOURCE.CLASS_ID=").append(tableName).append(".SUBJECT_CLASS_ID");
                    }
                }
                sb.append(") ");
                sb.append(value == null ? key : value); // 别名为空，用字段名代替
            } else { // 没有字典的属性
                if (!Strings.isNullOrEmpty(viewRow.getNullValue())) { // 需要给空值的字段
                    sb.append("NULL ").append(value == null ? key : value); // 别名为空，用字段名代替
                } else {
                    sb.append(key).append(" ").append(value == null ? "" : value); // 别名为空，用字段名代替
                }
            }
            if (size != i) {
                sb.append(",");
            }
            sb.append(" --").append(viewRow.getNote()).append(StringUtils.CRLF);
        }
        sb.append("FROM ").append(tableName).append(");").append(StringUtils.CRLF);
        if (DBUtils.isSqlServer()) {
            sb.append("GO" + StringUtils.CRLF);
        }
        FileUtils.write(filePath + File.separator + "sql" + File.separator + viewName + ".sql", sb);
    }

    /**
     * 根据所有表名生成对应Excel
     */
    public static void generationTabelExcel() {
        initModuleViewMap();
        // 创建HSSFWorkbook对象(excel的工作簿)
        HSSFWorkbook wb = new HSSFWorkbook();
        for (Map.Entry<String, ModuleView> entry : moduleViewMap.entrySet()) {
            String beanId = entry.getValue().getBeanId();
            Map<String, String> nameMap = DBUtils.getEntryNameBySql("select * from " + entry.getKey());
            String sqlToCfg = null;
            List<String[]> cfgList = null;
            // 专利特殊处理(jsp=/business/patent/noDocking/patentAdd.jsp)823项目需要打开
            if (beanId.equals("patent")) {
                sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION)='" + beanId + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanId + "/NODOCKING/" + beanId + "ADD.JSP' and ROLE = '4')";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
            }
            else if (beanId.equals("patentAuthor")) {
                sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION) like '%TO_ADD%' and JSP = '/BUSINESS/PATENT/NODOCKING/PATENTADD.JSP' and ROLE = '4')";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
            }
            else if (beanId.equals("projectIncome")) {
                sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE where UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION)='" + beanId + "ACTION!TO_ADD' and JSP = '/BUSINESS/outlay/" + beanId + "/" + beanId + "ADD.JSP' and ROLE = '4')";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
            }
            else if (beanId.contains("Member")) {
                sqlToCfg = "SELECT NAME,DATA_TYPE,CATEGORYNAME FROM SYS_CFG_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_TABLE where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(JSP) like '%" + beanId + "MANAGE.JSP%' and ROLE = '4')";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
            }
            else if (beanId.contains("Author")) {
                sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION) like '%TO_ADD%' and ROLE = '4' AND BINDSCENEID IS NOT NULL)";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
                if (cfgList.size() == 0) {
                    sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION) like '%TO_ADD%' and ROLE = '4')";
                    cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
                }
            }
            else if ("educationStat".equals(beanId)) {
                sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_EDIT_TABLE where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_EDITTABLE where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and ROLE = '4' AND BINDSCENEID IS NOT NULL)";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
            }
            else {
                sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select ID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION)='" + beanId + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanId + "/" + beanId + "ADD.JSP' and ROLE = '4')";
                cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
                if (cfgList.size() == 0) {
                    sqlToCfg = "SELECT NAME,FORMITEMTYPE,CATEGORYNAME FROM SYS_CFG_FORM where sceneid=(select BINDSCENEID from SYS_CFG_SCENE,SYS_CFG_SCENE_FORM where ID=SCENEID and UPPER(BEANID)='" + beanId + "' and UPPER(REQACTION)='" + beanId + "ACTION!TO_ADD' and JSP = '/BUSINESS/" + beanId + "/" + beanId + "ADD.JSP' and ROLE = '4')";
                    cfgList = DBUtils.getCFGFromBySql(sqlToCfg);// 存放表中所有列
                }
            }
            String sqlToMeaning = "select NAME,MEANING from SYS_ENTITY_META where BEANID='" + beanId + "'";
            Map<String, String> meaningMap = DBUtils.getMeaningBySql(sqlToMeaning); // 存放表中注释

            // 建立新的sheet对象（excel的工作表）
            HSSFSheet sheet = ExcelUtils.createSheet(wb, entry.getKey(), new int[]{8000, 3000, 2000, 6000, 10000, 5000});
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            // 添加表头
            ExcelUtils.setCellValues(sheet, 0, 6, new String[]{"列名", "别名", "是否为空值", "自定义字典", "关联表名及查询值", "注释"});
            ExcelUtils.setCellValues(sheet, 1, 6, new String[]{"ID", "ID", null, null, null, "主键"});
            int rowNum = 1;
            if (entry.getKey().contains("MEMBER")) {
                ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"PERSON_CODE", "ZGH", null, null, null, "职工号"});
                if (entry.getKey().contains("YF_CONTRACT")) {
                    ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"CONTRACT_ID", "HTID", null, null, null, "合同id"});
                } else {
                    ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"PROJECT_ID", "XMID", null, null, null, "项目id"});
                }
            } else if (entry.getKey().contains("AUTHOR")) {
                ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{"AUTHOR_ACCOUNT", "ZGH", null, null, null, "职工号"});
                ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{OtherUtils.getMainBeanId(entry.getKey()) + "_ID", "CGID", null, null, null, "成果id"});
            }
            for (int i = 0; i < cfgList.size(); i++) {
                if ("hidden".equals(cfgList.get(i)[1]) || "file".equals(cfgList.get(i)[1])) {
                    continue;
                }
                String categroy = null;
                String joinTable = null;
                if (StringUtils.isNotBlank(cfgList.get(i)[2])) {
                    if ("unitId".equals(cfgList.get(i)[0])) {
                        joinTable = "BIZ_UNIT,ID";
                    } else {
                        String sql = "SELECT TABLENAME,CODECOLUMN FROM CFG_CATEGORY_MAPPING WHERE CATEGORYID IN (SELECT ID FROM CFG_CATEGORY WHERE CATEGORYNAME = '" + cfgList.get(i)[2] + "')";
                        String[] strs = DBUtils.getStrsBySql(sql);
                        categroy = (strs[0] == null) ? cfgList.get(i)[2] : null;
                        joinTable = (strs[0] == null) ? null : strs[0] + "," + strs[1];
                    }
                }
                String nameTemp = cfgList.get(i)[0];
                //个别字段命名不规范问题
                if ("publishLevelId".equals(nameTemp)) {
                    nameTemp = "paperLevelId";
                } else if ("honerType".equals(nameTemp)) {
                    nameTemp = "honorType";
                } else if ("credentialFileId".equals(nameTemp)) {
                    nameTemp = "crednetialFileId";
                } else if ("paperSpace".equals(nameTemp)) {
                    nameTemp = "pageSpace";
                } else if ("bearUnitOrder".equals(nameTemp) && !"XJ_PROJECT".equals(beanId)) {
                    nameTemp = "undertakingUnitRanking";
                }
                ExcelUtils.setCellValues(sheet, ++rowNum, 6, new String[]{
                        nameMap.get(nameTemp.toUpperCase()) == null ? ("*" + nameTemp) : nameMap.get(nameTemp.toUpperCase()),
                        StringUtils.getFirstSpell(meaningMap.get(cfgList.get(i)[0])), null, categroy, joinTable,
                        meaningMap.get(cfgList.get(i)[0])});
            }
        }
        ExcelUtils.outExcel(wb, filePath + File.separator + "xls" + File.separator, xlsName);
    }
}
