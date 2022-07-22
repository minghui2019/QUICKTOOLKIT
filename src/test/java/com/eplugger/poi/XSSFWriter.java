
package com.eplugger.poi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.eplugger.utils.ExcelUtils;

public class XSSFWriter {
	@Test
	public void testName() throws Exception {
		URL url = XSSFWriter.class.getClassLoader().getResource("");
		String rowname = "编号,日,序号,类型,会计,科目代码,科目名称,基础科目,经济科目,收支类型代码,收支类型名称,摘要,经办人,借方金额,贷方金额,子项目代码,子项目名称,主项目代码,主项目名称,授权号,预算项代码,预算项名称,制单,制单时间,复核,复核时间,核销代码,往来单位,债权债务人,业务日期,对方户名,对方账号,用途,开户行,联行号,银行流水号,来源代码,来源名称,拨款文号,财政项目,财政经济科目,支付令,支出类型代码,经费性质,资金性质,账户类型,类款项,预算年度,信息关联号,特殊核算码,特殊核算名称,材料编号,借方数量,贷方数量,往来预算项,合同号,车牌号码,凭证备注,项目大类,负责人工号,负责人姓名,SA_DEPART,SA_F01,SA_F02,SA_F03,SA_F04,SA_F05,SA_F06,SA_F07,SA_F08,SA_F09,SA_F10,SA_F11,SA_F12,SA_F13,SA_F14,SA_F15,SA_F16,SA_F17,SA_F18,SA_F19,SA_F20,冻结码,预约单号,是否冲红";
		List<Map<String, Object>> readBigExcel = readBigExcel(url.getPath() + File.separator + "AIRS项目明细账 截止2022.5.31.xlsx", rowname , 0, 0, 0);
		System.out.println(readBigExcel.size());
		System.out.println(readBigExcel.get(0).toString());
		System.out.println(readBigExcel.get(1).toString());
		System.out.println(readBigExcel.get(17043).toString());
	}

	/**
	 * 大批量数据读取 15W以上 思路：采用分段缓存，防止出现OOM的情况 格式限制：必须使用xlsx的格式，调用前需判断格式
	 */
	public static List<Map<String, Object>> readBigExcel(String file, String rowname, int stasheetNum,
			int starowNum, int stacolumn) throws Exception {
		// 定义返回值
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try (Workbook wk = ExcelUtils.openWorkbook(file, true)) { // 打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
			Sheet sheet = wk.getSheetAt(stasheetNum);
			String[] rownameSplit = rowname.split(",");
			int columnlength = rownameSplit.length;
			Cell cell = null;// 定义单元格
			// 遍历所有的行（）
			System.out.println(sheet.getFirstRowNum());
			System.out.println(sheet.getLastRowNum());
			for (Row row : sheet) {
				// row=sheet.getRow(i);//获取当前循环的行数据(因为只缓存了部分数据，所以不能用getRow来获取)此处采用增强for循环直接获取row对象
				Map<String, Object> paramMap = new HashMap<String, Object>();// 定义一个map做数据接收
				if (row.getRowNum() >= starowNum) { // 从设定的行开始取值
					// System.out.println("开始遍历第" + row.getRowNum() + "行数据：");
					// 对当前行逐列进行循环取值
					for (int j = stacolumn; j < columnlength; j++) {
						if (row == null) {
							paramMap.put(rownameSplit[j], null);// 将单元格值放入map
						} else {
							 
							if (cell == null || cell.getCellType() == CellType.BLANK) {
								paramMap.put(rownameSplit[j], null);// 将单元格值放入map
							} else {
								paramMap.put(rownameSplit[j], cell.getStringCellValue());// 将单元格值放入map
							}

						}

					}
					// 一行循环完成，将map存入list
					resultList.add(paramMap);
				}
			}

		}
		return resultList;
	}
	
	@Test
	public void testReadCellType() throws Exception {
		readBigExcel("C:\\Users\\Admin\\Desktop\\工作簿1.xlsx");
	}
	
	private void readBigExcel(String file) {
		Workbook wk = ExcelUtils.openWorkbook(file, true);
		try {
			Sheet sheet = wk.getSheetAt(0);
			for (Row row : sheet) {
				for (Cell cell : row) {
					CellType cellType = cell.getCellType();
					if (cellType == CellType.STRING) {
						System.out.println((cell.getColumnIndex() + 1) + ": " + cellType + ": " + cell.getRichStringCellValue());
					} else if (cellType == CellType.NUMERIC) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
			                Date d = cell.getDateCellValue();
			                if (d != null)
			                	System.out.println((cell.getColumnIndex() + 1) + ": " + cellType + ": " + new SimpleDateFormat("yyyy-MM-dd").format(d));
			            } else {
			            	System.out.println((cell.getColumnIndex() + 1) + ": " + cellType + ": " + cell.getNumericCellValue());
			            }
					}
				}
			}
		} finally {
			try {
				wk.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}