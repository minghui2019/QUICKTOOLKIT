package com.eplugger.onekey.dictionary;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.eplugger.common.io.FileUtils;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.utils.DBUtils;
import com.eplugger.utils.ExcelUtils;

public class AutoGmjjhy {
    public static void main(String[] args) {
        //exportExcel();
        importExcel();
    }

    private static void importExcel() {
        String sql = "SELECT * FROM DM_GMJJHY";
        List<Gmjjhy> list = getListBySql(sql);
        Workbook workbook = ExcelUtils.getWorkbook("C:/Users/Admin/Desktop" + File.separator, "国民经济行业(1).xls");
        List<String> level1 = new ArrayList<String>();
        List<String> level2 = new ArrayList<String>();
        List<String> level3 = new ArrayList<String>();
        List<String> level4 = new ArrayList<String>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);// 遍历拿每一个sheet
            // 为跳过第一行目录设置count
            int count = 0;
            for (Row row : sheet) {
                if (count < 1) {
                    count++;
                    continue;
                }
                if (StringUtils.isBlank(row.getCell(0).toString())) { // 如果当前行没有数据，跳出循环
                    return;
                }
                
                if (null != row.getCell(0) && !level1.contains(row.getCell(0).toString())) {
                    level1.add(row.getCell(0).toString());
                }
                if (null != row.getCell(1) && !level2.contains(row.getCell(1).toString())) {
                    level2.add(row.getCell(1).toString());
                }
                if (null != row.getCell(2) && !"".equals(row.getCell(2).toString()) && !level3.contains(row.getCell(2).toString())) {
                    level3.add(row.getCell(2).toString());
                }
                if (null != row.getCell(3) && !level4.contains(row.getCell(3).toString())) {
                    level4.add(row.getCell(3).toString());
                }
            }
        }
        System.out.println(list.size());
        List<Gmjjhy> list1 = list.stream()
                .filter(o -> !level1.contains(o.getName()))
                .filter(o -> !level2.contains(o.getName()))
                .filter(o -> !level3.contains(o.getName()))
                .filter(o -> !level4.contains(o.getName()))
                .collect(Collectors.toList());
        List<String> list2 = list1.stream().map(Gmjjhy::getId).collect(Collectors.toList());
        List<Gmjjhy> list3 = list.stream().filter(a -> !list2.contains(a.getId())).collect(Collectors.toList());
        StringBuffer sb = new StringBuffer("DELETE FROM DM_GMJJHY;" + StringUtils.CRLF);
        //([ID], [NAME], [LEVEL_ID], [UP_ID], [CODE], [NAME_LOCAL])
        for (Gmjjhy gmjjhy : list3) {
            String nameLocal = gmjjhy.getNameLocal();
            if (nameLocal.contains("\'")) {
                nameLocal = nameLocal.replace("\'", "\'\'");
                System.out.println(nameLocal);
            }
            if ("A" .equals(gmjjhy.getLevelId())) {
                sb.append("INSERT INTO DM_GMJJHY VALUES ('" + gmjjhy.getId() + "', '" + gmjjhy.getName() + "', '" + gmjjhy.getLevelId() + "', null, '" + gmjjhy.getCode() + "', '" + nameLocal + "');");
            } else {
                sb.append("INSERT INTO DM_GMJJHY VALUES ('" + gmjjhy.getId() + "', '" + gmjjhy.getName() + "', '" + gmjjhy.getLevelId() + "', '" + gmjjhy.getUpId() + "', '" + gmjjhy.getCode() + "', '" + nameLocal + "');");
            }
            sb.append(StringUtils.CRLF);
        }
        FileUtils.write("C:/Users/Admin/Desktop/国民经济行业.sql", sb.toString());
//        Map<String, List<Gmjjhy>> groupingByLevelId = list1.stream().collect(Collectors.groupingBy(Gmjjhy::getLevelId));
//        List<Gmjjhy> listA = groupingByLevelId.get("A");
//        System.out.println(collect.size());
//        System.out.println(list1.size());
//        System.out.println(level1.size());
//        System.out.println(listA.size());
//        listA.forEach(System.out::println);
//        level1.forEach(System.out::println);
//        level2.forEach(System.out::println);
//        level3.forEach(System.out::println);
//        level4.forEach(System.out::println);
//        System.out.println(level1.size()+level2.size()+level3.size()+level4.size());
    }

    public static void exportExcel() {
        String sql = "SELECT * FROM DM_GMJJHY";
        List<Gmjjhy> list = getListBySql(sql);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 8000);
        CellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        ExcelUtils.setCellValues(sheet, 0, 4, new String[] { "一级", "二级", "三级", "四级" });
        int rowNum = 0;
        Map<String, List<Gmjjhy>> groupingByLevelId = list.stream().collect(Collectors.groupingBy(Gmjjhy::getLevelId));
        List<Gmjjhy> listTemp = list.stream().filter(s -> s.getUpId() != null)
                .sorted(Comparator.comparing(Gmjjhy::getId)).collect(Collectors.<Gmjjhy>toList());
        Map<String, List<Gmjjhy>> groupingByUpId = listTemp.stream().collect(Collectors.groupingBy(Gmjjhy::getUpId));
        List<Gmjjhy> listA = groupingByLevelId.get("A");
        for (Gmjjhy gmjjhy : listA) {
            int firstRow = rowNum + 1;
            int lastRow = rowNum + 1;
            List<Gmjjhy> list2 = groupingByUpId.get(gmjjhy.getId());
            for (Gmjjhy gmjjhy2 : list2) {
                int firstRow1 = rowNum + 1;
                int lastRow1 = rowNum + 1;
                List<Gmjjhy> list3 = groupingByUpId.get(gmjjhy2.getId());
                for (Gmjjhy gmjjhy3 : list3) {
                    int firstRow2 = rowNum + 1;
                    int lastRow2 = rowNum + 1;
                    List<Gmjjhy> list4 = groupingByUpId.get(gmjjhy3.getId());
                    for (Gmjjhy gmjjhy4 : list4) {
                        ExcelUtils.setCellValues(sheet, ++rowNum, 4, new String[] { "", "", "", gmjjhy4.getName() });
                        lastRow = rowNum;
                        lastRow1 = rowNum;
                        lastRow2 = rowNum;
                    }
                    if (lastRow2 - firstRow2 > 1) {
                        CellRangeAddress region2 = new CellRangeAddress(firstRow2, lastRow2, 2, 2);
                        sheet.addMergedRegion(region2);
                        HSSFCell cell = sheet.getRow(firstRow2).getCell(2);
                        cell.setCellValue(gmjjhy3.getName());
                        cell.setCellStyle(style);
                    }
                }
                if (lastRow1 - firstRow1 > 1) {
                    CellRangeAddress region1 = new CellRangeAddress(firstRow1, lastRow1, 1, 1);
                    sheet.addMergedRegion(region1);
                    HSSFCell cell = sheet.getRow(firstRow1).getCell(1);
                    cell.setCellValue(gmjjhy2.getName());
                    cell.setCellStyle(style);
                }
            }
            if (lastRow - firstRow > 1) {
                CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, 0, 0);
                sheet.addMergedRegion(region);
                HSSFCell cell = sheet.getRow(firstRow).getCell(0);
                cell.setCellValue(gmjjhy.getName());
                cell.setCellStyle(style);
            }
        }
        ExcelUtils.outExcel(wb, "C:/Users/Admin/Desktop" + File.separator, "国民经济行业.xls");
    }

    public static List<Gmjjhy> getListBySql(String sql) {
        List<Gmjjhy> list = new ArrayList<>();
        Connection conn = DBUtils.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Gmjjhy gmjjhy = new Gmjjhy(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6));
                list.add(gmjjhy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeAll(conn, ps, rs);
        }
        return list;
    }
}

class Gmjjhy {
    private String id;
    private String name;
    private String levelId;
    private String upId;
    private String code;
    private String nameLocal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public Gmjjhy() {
        super();
    }

    public Gmjjhy(String id, String name, String levelId, String upId, String code, String nameLocal) {
        super();
        this.id = id;
        this.name = name;
        this.levelId = levelId;
        this.upId = upId;
        this.code = code;
        this.nameLocal = nameLocal;
    }

    @Override
    public String toString() {
        return "Gmjjhy [id=" + id + ", name=" + name + ", levelId=" + levelId + ", upId=" + upId + ", code=" + code
                + ", nameLocal=" + nameLocal + "]";
    }
}
