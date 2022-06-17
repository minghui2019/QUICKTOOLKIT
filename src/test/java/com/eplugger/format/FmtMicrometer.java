package com.eplugger.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @ClassName: FmtMicrometer
 * @Description: 格式化数字为千分位工具类
 * @author wsq E-mail：
 * @date 2017-6-1 下午02:25:57
 * 
 */
public class FmtMicrometer {

	/**
	 * @Title: fmtMicrometer
	 * @Description: 格式化数字为千分位
	 * @param text
	 * @return 设定文件
	 * @return String 返回类型
	 */
	public static String fmtMicrometer(String text) {
		DecimalFormat df = null;
		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1) {
				df = new DecimalFormat("###,##0.0");
			} else {
				df = new DecimalFormat("###,##0.00");
			}
		} else {
			df = new DecimalFormat("###,##0");
		}
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	public static void main(String[] args) throws ParseException {

		NumberFormat numberFormat1 = NumberFormat.getNumberInstance();
		System.out.println(numberFormat1.format(11122.33)); // 结果是11,122.33

		NumberFormat numberFormat2 = NumberFormat.getNumberInstance();
		System.out.println(numberFormat2.format(11122.00)); // 结果是11,122

		NumberFormat numberFormat3 = NumberFormat.getNumberInstance();
		numberFormat3.setGroupingUsed(false); // 设置了以后不会有千分位，如果不设置，默认是有的
		System.out.println(numberFormat3.format(11122.33)); // 结果是11122.33

		// 将一个可能包含千分位的数字转换为不含千分位的形式：
		String amount1 = "1,999,113,000.00";
		double d1 = new DecimalFormat().parse(amount1).doubleValue(); // 这里使用的是parse，不是format
		System.out.println(String.valueOf(d1)); // 结果是13000.0
		
		System.out.println(new DecimalFormat("###,##0.00").parse(amount1).toString());
		System.out.println(new DecimalFormat().parse(amount1).toString());
	}

}