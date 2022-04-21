package com.eplugger.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eplugger.common.io.FileUtils;

public class ExcelUtils {
	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";
	
	/**
	 * 判断文件是否是excel
	 */
	public static void checkExcelVaild(File file) {
		try {
			if (!file.exists()) {
				throw new Exception("文件不存在");
			}
			if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
				throw new Exception("文件不是Excel");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断Excel的版本,获取Workbook
	 */
	public static Workbook getWorkbook(String filePath, String xlsName) {
		File excelFile = new File(filePath + File.separator + xlsName); // 创建文件对象
		Workbook wb = null;
		try {
			FileInputStream in = new FileInputStream(excelFile);// 文件流
			ExcelUtils.checkExcelVaild(excelFile);
			if (excelFile.getName().endsWith(EXCEL_XLS)) { // Excel 2003
				wb = new HSSFWorkbook(in);
			} else if (excelFile.getName().endsWith(EXCEL_XLSX)) { // Excel 2007/2010
				wb = new XSSFWorkbook(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	/**
	 * 填充一行单元格
	 */
	public static void setCellValues(HSSFSheet sheet, int r, int count, String[] strs, CellStyle style) {
		HSSFRow row = sheet.createRow(r);
		for (int i = 0; i < count; i++) {
			if (strs[i] != null) {
			    HSSFCell cell = row.createCell(i);
			    cell.setCellStyle(style);
				cell.setCellValue(strs[i]);
			}
		}
	}
	
	public static void setCellValues(HSSFSheet sheet, int r, int count, String[] strs) {
	    HSSFRow row = sheet.createRow(r);
	    row.setHeight((short) 300);
	    for (int i = 0; i < count; i++) {
	        if (strs[i] != null) {
	            row.createCell(i).setCellValue(strs[i]);
	        }
	    }
	}
	
	/**
     * 写出EXCEL文件
     */
    public static void outExcel(Workbook wb, String filePath, String fileName) {
    	File file = new File(filePath + fileName);
    	FileUtils.createFileParentDir(file);
    	try {
			FileOutputStream output = new FileOutputStream(file);
			wb.write(output); // 写入磁盘
			output.flush();
			output.close();
			System.out.println("EXCEL文件 " + fileName + " 生成成功");
			System.out.println("由于实体类属性命名不规则，带星号的别名必须核对清楚！");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
