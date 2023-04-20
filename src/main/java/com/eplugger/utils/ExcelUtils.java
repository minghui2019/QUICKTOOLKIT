package com.eplugger.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.eplugger.common.io.FileUtils;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class ExcelUtils {
	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";
	
	/**
	 * 判断文件是否是excel
	 * @throws Exception 
	 */
	public static File checkExcelVaild(String pathname) throws Exception {
		File file = new File(pathname);
		if (!file.exists() || !file.isFile()) {
			throw new Exception("文件不存在");
		}
		if (!(pathname.endsWith(EXCEL_XLS) || pathname.endsWith(EXCEL_XLSX))) {
			throw new Exception("请使用Excel文件");
		}
		return file;
	}
	
	/**
	 * 判断Excel的版本,获取Workbook
	 */
	public static Workbook openWorkbook(String filePath, String xlsName) {
		return openWorkbook(filePath + File.separator + xlsName, false);
	}
	
	/**
	 * 判断Excel的版本，打开Excel文件
	 * @param pathname
	 * @param isCache 是否使用StreamingReader打开文件，可以提高大文件读取速度，但不支持导出
	 * @return
	 */
	public static Workbook openWorkbook(String pathname, boolean isCache) {
		File excelFile = null;
		try {
			excelFile = ExcelUtils.checkExcelVaild(pathname);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(excelFile);// 文件流
			if (excelFile.getName().endsWith(EXCEL_XLS)) { // Excel 2003
				return new HSSFWorkbook(in);
			}
			if (excelFile.getName().endsWith(EXCEL_XLSX)) { // Excel 2007/2010
				if (isCache) {
					return StreamingReader.builder().rowCacheSize(100) // 缓存到内存中的行数，默认是10
							.bufferSize(4096) // 读取资源时，缓存到内存的字节大小，默认是1024
							.open(in);
				}
				return new XSSFWorkbook(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 创建新的sheet对象（excel的工作表）
	 * @param wb
	 * @param sheetname
	 * @param widths
	 * @return
	 */
	public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetname, int[] widths) {
		checkNotNull(wb);
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet(sheetname);
		for (int i = 0; i < widths.length; i++) {
			sheet.setColumnWidth(i, widths[i]);
		}
		return sheet;
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
    	outExcel(wb, new File(filePath + fileName));
    }

    public static void outExcel(Workbook wb, File file) {
    	FileUtils.createFileParentDir(file);
    	try {
			FileOutputStream output = new FileOutputStream(file);
			wb.write(output); // 写入磁盘
			output.flush();
			output.close();
			log.debug("EXCEL文件 " + file.getName() + " 生成成功");
			log.debug("由于实体类属性命名不规则，带星号的别名必须核对清楚！");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
