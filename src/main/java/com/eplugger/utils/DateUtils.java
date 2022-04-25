package com.eplugger.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {
	public static final DateFormat DATEFORMAT_DATE_NOSEPARATOR = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat DATEFORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat DATEFORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DATEFORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DATEFORMAT_TIME_NOSEPARATOR = new SimpleDateFormat("HHmmss");
	
	// ******************* format *******************
	/**
	 * <pre>
	 * 格式: yyyyMMdd
	 * 默认取当前时间
	 * </pre>
	 * @return
	 */
	public static String formatDateNoSeparator() {
		return formatDateNoSeparator(new Date());
	}
	/**
	 * 格式: yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String formatDateNoSeparator(Date date) {
		return DATEFORMAT_DATE_NOSEPARATOR.format(date);
	}
	
	/**
	 * <pre>
	 * 格式: yyyy-MM-dd
	 * 默认取当前时间
	 * </pre>
	 * @return
	 */
	public static String formatDate() {
		return formatDate(new Date());
	}
	/**
	 * 格式: yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return DATEFORMAT_DATE.format(date);
	}
	
	/**
	 * <pre>
	 * 格式: yyyy-MM-dd HH:mm:ss
	 * 默认取当前时间
	 * </pre>
	 * @return
	 */
	public static String formatDateTime() {
		return formatDateTime(new Date());
	}
	/**
	 * 格式: yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return DATEFORMAT_DATETIME.format(date);
	}
	
	/**
	 * <pre>
	 * 格式: HH:mm:ss
	 * 默认取当前时间
	 * </pre>
	 * @return
	 */
	public static String formatTime() {
		return formatTime(new Date());
	}
	/**
	 * 格式: HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		return DATEFORMAT_TIME.format(date);
	}
	
	/**
	 * <pre>
	 * 格式: HHmmss
	 * 默认取当前时间
	 * </pre>
	 * @return
	 */
	public static String formatTimeNoSeparator() {
		return formatTimeNoSeparator(new Date());
	}
	/**
	 * 格式: HHmmss
	 * @param date
	 * @return
	 */
	public static String formatTimeNoSeparator(Date date) {
		return DATEFORMAT_TIME_NOSEPARATOR.format(date);
	}
	
	public static String format(DateFormat df, Date date) {
		return df.format(date);
	}
	
	// ******************* parse *******************
	public static Date parseDate(String source) {
		return parse(DATEFORMAT_DATE, source);
	}
	
	/**
	 * 字符串转换日期(java.util.Date)。
	 * @param df 转换的DateFormat
	 * @param source 待转换的字符串
	 * @return java.util.Date格式日期；异常则null。
	 */
	public static Date parse(DateFormat df, String source) {
		if (Strings.isNullOrEmpty(source)) {
			return null;
		}
		try {
			return df.parse(source);
		} catch (ParseException e) {
			log.error("日期格式化错误，原格式：" + source);
		}
		return null;
	}
}
