package com.eplugger.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final DateFormat DATEFORMAT_DATE_NOSEPARATOR = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat DATEFORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat DATEFORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DATEFORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DATEFORMAT_TIME_NOSEPARATOR = new SimpleDateFormat("HHmmss");
	
	/**
	 * 格式: yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String formatDateNoSeparator(Date date) {
		return DATEFORMAT_DATE_NOSEPARATOR.format(date);
	}
	public static String formatDateNoSeparator() {
		return formatDateNoSeparator(new Date());
	}
	
	/**
	 * 格式: yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return DATEFORMAT_DATE.format(date);
	}
	
	public static String formatDate() {
		return formatDate(new Date());
	}
	
	/**
	 * 格式: yyyy-MM-dd hh:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return DATEFORMAT_DATETIME.format(date);
	}
	
	/**
	 * 格式: hh:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		return DATEFORMAT_TIME.format(date);
	}
	
	/**
	 * 格式: hhmmss
	 * @param date
	 * @return
	 */
	public static String formatTimeNoSeparator(Date date) {
		return DATEFORMAT_TIME_NOSEPARATOR.format(date);
	}
	
	/**
	 * 格式: hhmmss
	 * @return
	 */
	public static String formatTimeNoSeparator() {
		return formatTimeNoSeparator(new Date());
	}
}
