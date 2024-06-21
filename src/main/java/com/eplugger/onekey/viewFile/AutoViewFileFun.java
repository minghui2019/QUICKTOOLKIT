package com.eplugger.onekey.viewFile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.eplugger.onekey.entity.Categories;
import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.viewFile.entity.ModuleView;
import com.eplugger.onekey.viewFile.entity.ViewRowBuilder;
import com.eplugger.onekey.viewFile.entity.ViewRowBuilder.ViewRow;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.OtherUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;
import top.tobak.utils.ExcelUtils;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

import static com.eplugger.onekey.viewFile.AutoViewFileConstant.categoryMap;
import static com.eplugger.onekey.viewFile.AutoViewFileConstant.defaultViewMap;
import static com.eplugger.onekey.viewFile.AutoViewFileConstant.initCategoryMap;
import static com.eplugger.onekey.viewFile.AutoViewFileConstant.initDefaultViewMap;
import static com.eplugger.onekey.viewFile.AutoViewFileConstant.initModuleViewMap;
import static com.eplugger.onekey.viewFile.AutoViewFileConstant.moduleViewMap;

@Slf4j
public class AutoViewFileFun {
    private static String filePath = FileUtils.getUserHomeDirectory() + "视图";
    private static String xlsName = "共享视图.xls";


    /**
     * 自动生成视图语句
     */
    public static void generationViewSql() {
        initModuleViewMap();
        initDefaultViewMap();
        Workbook workbook = ExcelUtils.openWorkbook(filePath + File.separator + "xls" + File.separator, xlsName);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i); // 遍历拿每一个sheet
            String tableName = sheet.getSheetName(); // 表名
            ModuleView moduleView = moduleViewMap.get(tableName);
            if (moduleView == null) {
                for (String key : moduleViewMap.keySet()) {
                    ModuleView view = moduleViewMap.get(key);
                    if (tableName.equals(view.getSheetName())) {
                        moduleView = view;
                        break;
                    }
                }
            }
            String viewName = moduleView.getViewName(); // 视图名
            List<ViewRow> viewRows = Lists.newArrayList();
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    continue;
                }
                if (row.getCell(1) == null || Strings.isNullOrEmpty(row.getCell(1).toString())) { // 如果当前行没有数据，跳出循环
                    break;
                }
                ViewRow viewRow = new ViewRowBuilder().setColumnName(row.getCell(0)).setAlias(row.getCell(1)).setNullValue(row.getCell(2))
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
            String key = viewRow.getColumnName();
            String value = viewRow.getAlias();
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
    public static void generationTableExcel() {
        initModuleViewMap();
        initDefaultViewMap();
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
            List<String[]> entityMetas = DBUtils.getMeaningBySql(sqlToMeaning);
            Map<String, String> meaningMap = Maps.newHashMap();// 存放表中注释
            for (String[] entityMeta : entityMetas) {
                meaningMap.put(entityMeta[0], entityMeta[1]);
            }

            String sheetName = entry.getValue().getSheetName();
            // 建立新的sheet对象（excel的工作表）
            HSSFSheet sheet = ExcelUtils.createSheet(wb, sheetName, new int[]{8000, 3000, 2000, 6000, 10000, 5000});
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

    public static void generationTableExcel1() throws Exception {
        initModuleViewMap();
        initDefaultViewMap();
        // 创建HSSFWorkbook对象(excel的工作簿)
        HSSFWorkbook wb = new HSSFWorkbook();
        Categories categories = new Categories();
        Set<String> set = Sets.newHashSet();
        for (Entry<String, ModuleView> entry : moduleViewMap.entrySet()) {
            String beanId = entry.getValue().getBeanId();
            // PAYWAY -> PAY_WAY
            Map<String, String> nameMap = DBUtils.getEntryNameBySql("select * from " + entry.getKey());
            String sqlToCfg = null;
            // ["payWay", "radio", "PAY_WAY"]
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
            String sqlToMeaning = "select NAME,MEANING,CATEGORYNAME from SYS_ENTITY_META where BEANID='" + beanId + "'";
            // payWay -> 支付方式
            List<String[]> entityMetas = DBUtils.getMeaningBySql(sqlToMeaning);
            Map<String, String> meaningMap = Maps.newHashMap();// 存放表中注释
            Map<String, String> categoryMap = Maps.newHashMap();// 存放表中注释

            for (String[] entityMeta : entityMetas) {
                meaningMap.put(entityMeta[0], entityMeta[1]);
                categoryMap.put(entityMeta[0], entityMeta[2]);
            }

            String sheetName = entry.getValue().getSheetName();
            // 建立新的sheet对象（excel的工作表）
            int[] widths = { 8000, 4000, 2000, 5000, 6000, 10000, 5000 };
            HSSFSheet sheet = ExcelUtils.createSheet(wb, sheetName, widths);
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            int cellCount = widths.length;
            int rowIndex = -1;
            // 添加表头
            List<String[]> rows = Lists.newArrayList();
            rows.add(new String[] { "表字段名", "视图字段名", "空值", "中文简称", "自定义字典", "关联表名及查询值", "注释" });
            rows.add(new String[] { "ID", "WID", null, "主键", null, null, null });

            if (entry.getKey().contains("MEMBER")) {
                rows.add(new String[] { "PERSON_CODE", "ZGH", null, "职工号", null, null, null });
                rows.add(new String[] { "PROJECT_ID", "XMID", null, "项目id", null, null, null });
            } else if (entry.getKey().contains("AUTHOR")) {
                rows.add(new String[] { "AUTHOR_ACCOUNT", "ZGH", null, "职工号", null, null, null });
                rows.add(new String[] { OtherUtils.getMainBeanId(entry.getKey()) + "_ID", "CGID", null, "成果id", null, null, null });
            }

            List<String[]> list = defaultViewMap.get(entry.getValue().getViewName());
            /*
             * PAYWAY -> PAY_WAY nameMap
             * ["payWay", "radio", "PAY_WAY"] cfgList
             * payWay -> 支付方式 meaningMap
             */
            Map<String, String> aliasMap = Maps.newHashMap();
            Map<String, String[]> rowMap = Maps.newLinkedHashMap();
            for (Iterator<String[]> it = list.iterator(); it.hasNext(); ) {
                String[] cells = it.next();
                if ("Wid".equals(cells[0])) {
                    continue;
                }
                boolean flag = true;
                for (Entry<String, String> entry1 : meaningMap.entrySet()) {
                    if (rowMap.containsKey(entry1.getKey())) {
                        continue;
                    }
                    if (!"负责人".equals(entry1.getValue()) && (entry1.getValue().contains(cells[1]) || cells[1].contains(entry1.getValue()))) {
                        rowMap.put(entry1.getKey(), new String[] {nameMap.get(entry1.getKey().toUpperCase()), cells[0], null, cells[1], null, null, cells[2]});
                        flag = false;
                    } else if (entry1.getKey().equals("unitId") && (cells[1].contains("归属") || cells[1].contains("归在")) && cells[1].contains("单位")) {
                        rowMap.put(entry1.getKey(), new String[] {nameMap.get(entry1.getKey().toUpperCase()), cells[0], null, cells[1], null, null, cells[2]});
                        flag = false;
                    } else if (entry1.getKey().equals("authorizeDate") && cells[1].equals("立项日期")) {
                        rowMap.put(entry1.getKey(), new String[] {nameMap.get(entry1.getKey().toUpperCase()), cells[0], null, cells[1], null, null, cells[2]});
                        flag = false;
                    } else if (entry1.getKey().equals("subjectClassId") && cells[1].equals("科研大类代码")) {
                        rowMap.put(entry1.getKey(), new String[] {nameMap.get(entry1.getKey().toUpperCase()), cells[0], null, cells[1], null, null, cells[2]});
                        flag = false;
                    } else if (entry1.getKey().equals("name") && cells[1].equals("项目名称")) {
                        rowMap.put(entry1.getKey(), new String[] {nameMap.get(entry1.getKey().toUpperCase()), cells[0], null, cells[1], null, null, cells[2]});
                        flag = false;
                    } else if (entry1.getKey().equals("titleId") && cells[1].equals("专业技术职务代码")) {
                        rowMap.put(entry1.getKey(), new String[] {nameMap.get(entry1.getKey().toUpperCase()), cells[0], null, cells[1], null, null, cells[2]});
                        flag = false;
                    } else {
                        continue;
                    }
                }
                if (flag) {
                    rowMap.put(cells[0], new String[] {null, cells[0], "yes", cells[1], null, null, cells[2]});
                }
            }

            log.debug("别名: [{}]", aliasMap);

            for (int i = 0; i < cfgList.size(); i++) {
                if ("hidden".equals(cfgList.get(i)[1]) || "file".equals(cfgList.get(i)[1])) {
                    continue;
                }
//                String categroy = null;
//                String joinTable = null;
                String categoryName = cfgList.get(i)[2];
                String fieldId = cfgList.get(i)[0];
                if (Strings.isNullOrEmpty(categoryName)) {
                    categoryName = categoryMap.get(fieldId);
                }
                Category category = new Category();
                if (!Strings.isNullOrEmpty(categoryName)) {
                    if ("unitId".equals(fieldId)) {
                        categoryName = "UNIT_ALL";
                    }
                    String sql = "SELECT TABLENAME,CODECOLUMN,VALUECOLUMN,CATEGORYNAME,BIZNAME,BIZTYPE,FROMBIZ FROM CFG_CATEGORY CC LEFT JOIN CFG_CATEGORY_MAPPING CCM ON CC.ID=CCM.CATEGORYID WHERE CATEGORYNAME=:categoryName";
                    Map<String, Object> params = Maps.newHashMap();
                    params.put("categoryName", categoryName);
                    List<Category> categoryList = DBUtils.getListBySql(sql, params, (rs, rowNum) -> {
                        Category category1 = new Category();
                        category1.setTableName(rs.getString("TABLENAME"));
                        category1.setCodeColumn(rs.getString("CODECOLUMN"));
                        category1.setValueColumn(rs.getString("VALUECOLUMN"));
                        category1.setCategoryName(rs.getString("CATEGORYNAME"));
                        category1.setBizName(rs.getString("BIZNAME"));
                        category1.setBizType(rs.getString("BIZTYPE"));
                        category1.setFromBiz(rs.getInt("FROMBIZ"));
                        return category1;
                    });
                    category = categoryList.get(0);
                    if (!set.contains(category.getBizName())) {
                        categories.addCategory(category);
                        set.add(category.getBizName());
                    }
                }

                //个别字段命名不规范问题
                if ("publishLevelId".equals(fieldId)) {
                    fieldId = "paperLevelId";
                } else if ("honerType".equals(fieldId)) {
                    fieldId = "honorType";
                } else if ("credentialFileId".equals(fieldId)) {
                    fieldId = "crednetialFileId";
                } else if ("paperSpace".equals(fieldId)) {
                    fieldId = "pageSpace";
                } else if ("bearUnitOrder".equals(fieldId) && !"XJ_PROJECT".equals(beanId)) {
                    fieldId = "undertakingUnitRanking";
                }

                categoryName = "1".equals(category.getFromBiz()) ? "" : category.getCategoryName();
                String joinTable = "1".equals(category.getFromBiz()) ? category.getTableName() + "," + category.getCodeColumn() + "," + category.getValueColumn() : "";
                if (rowMap.containsKey(fieldId)) {
                    String[] strs = rowMap.get(fieldId);
                    strs[4] = categoryName;
                    strs[5] = joinTable;
                } else {
                    String firstSpell = aliasMap.containsKey(fieldId) ? aliasMap.get(fieldId) : "**" + StringUtils.getFirstSpell(meaningMap.get(fieldId));
                    rowMap.put(fieldId, new String[] { nameMap.get(fieldId.toUpperCase()) == null ? ("*" + fieldId) : nameMap.get(fieldId.toUpperCase()), firstSpell, null, meaningMap.get(fieldId), categoryName, joinTable, null });
                }
            }
            rows.addAll(rowMap.values());

            for (String[] strs : rows) {
                if ("性别代码".equals(strs[3])) {
                    strs[4] = "RY_XM_NN";
                } else if ("学历代码".equals(strs[3])) {
                    strs[4] = "EDU_LEVEL";
                } else if ("审核状态".equals(strs[3])) {
                    strs[5] = "dm_check,ID,NAME";
                }
                ExcelUtils.setCellValues(sheet, ++rowIndex, cellCount, strs);
            }
        }
        ExcelUtils.outExcel(wb, filePath + File.separator + "xls" + File.separator, xlsName);

        Document document = XmlParseUtils.fromBean(categories, "src/main/resource/view/Category.xml", true);
        log.debug("\n" + document.asXML());
    }

    public static void generationViewSql1() {
        initModuleViewMap();
        initCategoryMap();
        initDefaultViewMap();
        Workbook workbook = ExcelUtils.openWorkbook(filePath + File.separator + "xls" + File.separator, xlsName);
        grantSb.append("use ").append(DBUtils.schoolInfo.dbName).append(";").append(StringUtils.CRLF);
        grantSb.append("exec sp_grantdbaccess kygx;").append(StringUtils.CRLF);
        grantSb.append("GO").append(StringUtils.CRLF).append(StringUtils.CRLF);
        grantSb.append("--授权视图的select查看权限给用户kygx").append(StringUtils.CRLF);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i); // 遍历拿每一个sheet
            String tableName = sheet.getSheetName(); // 表名
            ModuleView moduleView = moduleViewMap.get(tableName);
            if (moduleView == null) {
                for (String key : moduleViewMap.keySet()) {
                    ModuleView view = moduleViewMap.get(key);
                    if (tableName.equals(view.getSheetName())) {
                        moduleView = view;
                        break;
                    }
                }
            }
            String viewName = moduleView.getViewName(); // 视图名
            List<ViewRow> viewRows = Lists.newArrayList();
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    continue;
                }
                if (row.getCell(1) == null || Strings.isNullOrEmpty(row.getCell(1).toString())) { // 如果当前行没有数据，跳出循环
                    break;
                }
                ViewRow viewRow = new ViewRowBuilder().setColumnName(row.getCell(0)).setAlias(row.getCell(1)).setNullValue(row.getCell(2))
                    .setMeaning(row.getCell(3)).setOwnDictionary(row.getCell(4)).setJoinTable(row.getCell(5))
                    .setNote(row.getCell(6)).getViewRow();
                viewRows.add(viewRow);
            }
            generationViewSql1(viewName, tableName, moduleView.getCheckStatus(), viewRows);
        }
        FileUtils.write(filePath + File.separator + "sql" + File.separator + "授权命令.sql", grantSb);
    }

    private static StringBuilder grantSb = new StringBuilder();
    private static Set<String> categoryNames = Sets.newHashSet();
    private static void generationViewSql1(String viewName, String tableName, String checkStatus, List<ViewRow> viewRows) {
        StringBuilder sb = new StringBuilder();
        StringBuilder categorySb = new StringBuilder();
        StringBuilder joinTableSb = new StringBuilder();
        sb.append("-- ").append(viewName).append(" (").append(moduleViewMap.get(tableName).getDescribed()).append(")");
        sb.append("IF EXISTS(SELECT 1 FROM SYS.VIEWS WHERE NAME='").append(viewName).append("') DROP VIEW ").append(viewName).append(";").append(StringUtils.CRLF)
            .append("GO").append(StringUtils.CRLF);
        sb.append("CREATE VIEW ").append(viewName).append(" AS").append(StringUtils.CRLF).append("(SELECT").append(StringUtils.CRLF);
        grantSb.append("GRANT SELECT ON ").append(viewName).append(" TO kygx;").append(StringUtils.CRLF);
        int i = 0, size = viewRows.size();
        for (ViewRow viewRow : viewRows) {
            i++;
            String columnName = viewRow.getColumnName();
            String alias = viewRow.getAlias();
            String ownDictionary = viewRow.getOwnDictionary();
            String joinTable = viewRow.getJoinTable();
            if (!Strings.isNullOrEmpty(ownDictionary)) { // 有字典的属性
                if (!categoryNames.contains(ownDictionary)) {
                    categoryNames.add(ownDictionary);
                    categorySb.append("-- ").append(viewName).append(" (").append(categoryMap.get(ownDictionary).getBizName()).append(")");
                    categorySb.append("IF EXISTS(SELECT 1 FROM SYS.VIEWS WHERE NAME='V_").append(ownDictionary).append("') DROP VIEW V_").append(ownDictionary).append(";").append(StringUtils.CRLF)
                        .append("GO").append(StringUtils.CRLF);
                    categorySb.append("CREATE VIEW V_").append(ownDictionary).append(" AS ")
                        .append("(SELECT CODE,NAME FROM CFG_CATEGORY_ENTRY CCE LEFT JOIN CFG_CATEGORY CC ON CCE.CATEGORYID = CC.ID WHERE CATEGORYNAME='")
                        .append(ownDictionary).append("'").append(");").append(StringUtils.CRLF)
                        .append("GO").append(StringUtils.CRLF).append(StringUtils.CRLF);
                    grantSb.append("GRANT SELECT ON V_").append(ownDictionary).append(" TO kygx;").append(StringUtils.CRLF);
                }
            } else if (!Strings.isNullOrEmpty(joinTable)) { // 关联表
                String[] str = joinTable.split(",");
                if (!categoryNames.contains(str[0])) {
                    categoryNames.add(str[0]);
                    categorySb.append("-- ").append(viewName).append(" (").append(categoryMap.get(joinTable).getBizName()).append(")");
                    String viewName1 = str[0].toUpperCase();
                    viewName1 = viewName1.replace("DM_", "");
                    categorySb.append("IF EXISTS(SELECT 1 FROM SYS.VIEWS WHERE NAME='V_").append(viewName1).append("') DROP VIEW V_").append(viewName1).append(";").append(StringUtils.CRLF)
                        .append("GO").append(StringUtils.CRLF);
                    categorySb.append("CREATE VIEW V_").append(viewName1).append(" AS ")
                        .append("(SELECT ").append(str[1]).append(" CODE,").append(str[2]).append(" NAME FROM ").append(str[0]).append(");")
                        .append(StringUtils.CRLF)
                        .append("GO").append(StringUtils.CRLF).append(StringUtils.CRLF);
                    grantSb.append("GRANT SELECT ON V_").append(viewName1).append(" TO kygx;").append(StringUtils.CRLF);
                }
            }
            if (!Strings.isNullOrEmpty(viewRow.getNullValue())) { // 需要给空值的字段
                if ("yes".equals(viewRow.getNullValue())) {
                    if ("XMFZRZGH".equals(alias)) {
                        if (joinTableSb.indexOf("BIZ_PERSON") == -1) {
                            joinTableSb.append(StringUtils.CRLF).append("LEFT JOIN BIZ_PERSON PERSON ON MAINTABLE.CHARGER_CODE=PERSON.ID");
                        }
                        sb.append("PERSON.ACCOUNT").append(" ").append(alias == null ? "" : alias); // 别名为空，用字段名代替
                    }
                    else if ("XMFZRSZDWDM".equals(alias)) {
                        if (joinTableSb.indexOf("BIZ_PERSON") == -1) {
                            joinTableSb.append(StringUtils.CRLF).append("LEFT JOIN BIZ_PERSON PERSON ON MAINTABLE.CHARGER_CODE=PERSON.ID");
                        }
                        sb.append("PERSON.UNIT_ID").append(" ").append(alias == null ? "" : alias); //
                    }
                    else if ("CYRS".equals(alias) || "CYMD".equals(alias)) {
                        String joinColumn = "PROJECT_ID";
                        String authorTable = tableName + "_MEMBER";
                        String personName = "PERSON_NAME";
                        String authorType = "MEMBER_TYPE";
                        if ("BIZ_YF_CONTRACT".equals(tableName)) {
                            joinColumn = "CONTRACT_ID";
                        } else if ("BIZ_XJ_PROJECT".equals(tableName) || "BIZ_ZX_PROJECT".equals(tableName)) {
                        } else {
                            joinColumn = OtherUtils.getMainBeanId1(tableName) + "_ID";
                            authorTable = tableName + "_AUTHOR";
                            personName = "AUTHOR_NAME";
                            authorType = "AUTHOR_TYPE";
                        }
                        if (joinTableSb.indexOf(authorTable) == -1) {
                            joinTableSb
                                .append(StringUtils.CRLF)
                                .append("LEFT JOIN (SELECT ")
                                .append(joinColumn)
                                .append(",COUNT(1) CYRS").append(",STUFF((SELECT ', ' + T2.").append(personName).append(" + (CASE T2.").append(authorType).append(" WHEN '2' THEN '(外)' WHEN '3' THEN '(学)' ELSE '' END) FROM ").append(authorTable).append(" T2 WHERE T2.")
                                .append(joinColumn).append("=T1.").append(joinColumn).append(" ORDER BY T2.ORDER_ID FOR XML PATH('')), 1, 2, '') CYMD")
                                .append(" FROM ").append(authorTable).append(" T1 GROUP BY ").append(joinColumn).append(") MEMBER ON MEMBER.").append(joinColumn).append("=MAINTABLE.ID");
                        }
                        sb.append("MEMBER.").append(alias).append(" ").append(alias == null ? "" : alias); //
                    }
//                    else if ("CYMD".equals(alias)) {
//                        String joinColumn = "";
//                        if ("BIZ_YF_CONTRACT".equals(tableName)) {
//                            joinColumn = "CONTRACT_ID";
//                        } else if ("BIZ_XJ_PROJECT".equals(tableName) || "BIZ_ZX_PROJECT".equals(tableName)) {
//                            joinColumn = "PROJECT_ID";
//                        }
//                        joinTableSb
//                            .append(StringUtils.CRLF)
//                            .append("left join (SELECT ")
//                            .append(joinColumn)
//                            .append(",STUFF((SELECT ', ' + t2.PERSON_NAME + (case t2.MEMBER_TYPE when '2' then '(外)' when '3' then '(学)' else '' end) FROM ").append(tableName).append("_MEMBER t2 WHERE t2.")
//                            .append(joinColumn).append("=t1.").append(joinColumn).append(" order by t2.ORDER_ID FOR XML PATH('')), 1, 2, '') CYMD FROM ")
//                            .append(tableName).append("_MEMBER t1 GROUP BY ").append(joinColumn).append(") MEMBER2 ON MEMBER2.").append(joinColumn)
//                            .append("=MAINTABLE.ID");
//                        sb.append("MEMBER2.").append(alias).append(" ").append(alias == null ? "" : alias); //
//                    }
                    else if ("DZJF".equals(alias)) {
                        String joinColumn = "PROJECT_ID";
                        joinTableSb
                            .append(StringUtils.CRLF)
                            .append("LEFT JOIN (SELECT ")
                            .append(joinColumn)
                            .append(",SUM(INCOME_FEE) DZJF FROM ").append("BIZ_PROJECT_INCOME GROUP BY ").append(joinColumn).append(") INCOME ON INCOME.").append(joinColumn).append("=MAINTABLE.ID");
                        sb.append("INCOME.").append(alias).append(" ").append(alias == null ? "" : alias); //
                    }
                    else if ("SJZCJE".equals(alias)) {
                        String joinColumn = "PROJECT_ID";
                        joinTableSb
                            .append(StringUtils.CRLF)
                            .append("LEFT JOIN (SELECT ")
                            .append(joinColumn)
                            .append(",SUM(PAYOUT_FEE) SJZCJE FROM ").append("BIZ_PROJECT_PAYOUT_DETAIL GROUP BY ").append(joinColumn).append(") DETAIL ON DETAIL.").append(joinColumn).append("=MAINTABLE.ID");
                        sb.append("DETAIL.").append(alias).append(" ").append(alias == null ? "" : alias); //
                    }
                    else if ("KMMC".equals(alias) && "BIZ_PROJECT_BUDGET_FEE".equals(tableName)) {
                        joinTableSb.append(StringUtils.CRLF).append("LEFT JOIN DM_BUDGET_SUBJECT DBS ON MAINTABLE.SUBJECT_CODE = DBS.CODE");
                        sb.append("DBS.NAME").append(" ").append(alias == null ? "" : alias); //
                    }
                    else if ("ZGH".equals(alias)) {
                        String joinColumn = "";
                        if ("KH_RESULT_DETAILS".equals(tableName)) {
                            joinColumn = "PERSON_ID";
                        } else if ("BIZ_PERSONNEL_DISPATCH".equals(tableName)) {
                            joinColumn = "DISPATCH_ID";
                        }
                        if (!Strings.isNullOrEmpty(joinColumn)) {
                            joinTableSb.append(StringUtils.CRLF).append("LEFT JOIN BIZ_PERSON PERSON ON MAINTABLE.").append(joinColumn).append("=PERSON.ID");
                        }
                        sb.append("PERSON.ACCOUNT").append(" ").append(alias == null ? "" : alias); //
                    }
                    else if ("YQRSZDW".equals(alias) && "BIZ_PERSONNEL_DISPATCH".equals(tableName)) {
                        sb.append("PERSON.UNIT_ID").append(" ").append(alias == null ? "" : alias); //
                    }
                    else if ("KHNF".equals(alias) && "KH_RESULT_DETAILS".equals(tableName)) {
                        joinTableSb.append(StringUtils.CRLF).append("LEFT JOIN KH_ASSESS_TIME KAT ON KAT.ID=MAINTABLE.ASSESS_TIME_ID");
                        sb.append("CONVERT(VARCHAR(4), KAT.BEGIN_DATE, 112)").append(" ").append(alias == null ? "" : alias); //
                    }
                    else {
                        sb.append("NULL ").append(alias == null ? columnName : alias); // 别名为空，用字段名代替
                    }
                } else {
                    sb.append("'").append(viewRow.getNullValue()).append("' ").append(alias == null ? columnName : alias); // 别名为空，用字段名代替
                }
            } else {
                sb.append("MAINTABLE.").append(columnName).append(" ").append(alias == null ? "" : alias); // 别名为空，用字段名代替
            }
            if (size != i) {
                sb.append(",");
            }
            sb.append(" --").append(viewRow.getMeaning()).append("  ").append(viewRow.getNote()).append(StringUtils.CRLF);
        }
        sb.append("FROM ").append(tableName).append(" MAINTABLE").append(joinTableSb).append(StringUtils.CRLF);
        if (!Strings.isNullOrEmpty(checkStatus)) {
            sb.append(" WHERE MAINTABLE.CHECKSTATUS='2'");
        } else if ("BIZ_PROJECT_BUDGET_FEE".equals(tableName)) {
            sb.append(" WHERE MAINTABLE.SUBJECT_CODE IS NOT NULL");
        }
        sb.append(");").append(StringUtils.CRLF);
        if (DBUtils.isSqlServer()) {
            sb.append("GO").append(StringUtils.CRLF);
        }
        FileUtils.write(filePath + File.separator + "sql" + File.separator + viewName + ".sql", sb.append(StringUtils.CRLF).append(StringUtils.CRLF).append(categorySb));
    }


    public static void mergeFiles() throws IOException {
        File directory = FileUtils.getFile(filePath + File.separator + "sql" + File.separator);
        if (!directory.isDirectory()) {
            return;
        }
        File[] files = directory.listFiles();
        List<File> files1 = Lists.newArrayList();
        for (File file : files) {
            if (file.getName().indexOf("授权命令") != -1) {
                continue;
            }
            files1.add(file);
        }
        FileUtils.mergeFiles(new File(filePath + File.separator + "sql", "视图创建命令.sql"), files1.toArray(new File[0]));
    }
}
