package com.andframe.util.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AfDateFormat {
	
	public static Locale LOCALE = Locale.ENGLISH;
	public static DateFormat DAY = new SimpleDateFormat("M月d日",LOCALE);
	/**
	 * 日期形式格式化
	 * @see y-M-d
	 */
	public static DateFormat DATE = new SimpleDateFormat("y-M-d",LOCALE);
	public static DateFormat TIME = new SimpleDateFormat("HH:mm:ss",LOCALE);
	public static DateFormat SIMPLE = new SimpleDateFormat("M月d日 HH:mm",LOCALE);
	public static DateFormat FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",LOCALE);

	public static String format(String format, Object date) {
		return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
	}

	public static String formatDate(Date date) {
		Calendar calender = Calendar.getInstance();
		int thisyear = calender.get(Calendar.YEAR);
		calender.setTime(date);
		int dateyear = calender.get(Calendar.YEAR);
		if (thisyear == dateyear) {
			return DAY.format(date);
		}
		return DATE.format(date);
	}

	public static String formatTime(Date date) {
		Calendar calender = Calendar.getInstance();
		int thisday = calender.get(Calendar.DAY_OF_MONTH);
		int thismonth = calender.get(Calendar.MONTH);
		int thisyear = calender.get(Calendar.YEAR);

		Date now = calender.getTime();
		calender.setTime(date);

		int dateday = calender.get(Calendar.DAY_OF_MONTH);
		int datemonth = calender.get(Calendar.MONTH);
		int dateyear = calender.get(Calendar.YEAR);
		if (date.before(now)) {
			if (dateyear < thisyear) {// 今年以前
				return DATE.format(date);
			} else if (datemonth < thismonth) {// 这个月以前
				return SIMPLE.format(date);
			} else if (dateday < thisday - 2) {// 前天之前
				return format("d号 HH:mm", date);
			} else if (dateday < thisday - 1) {// 昨天之前
				return format("前天 HH:mm", date);
			} else if (dateday < thisday) {// 今天之前
				return format("昨天 HH:mm", date);
			} else {
				return format("今天 HH:mm", date);
			}
		} else {
			if (dateyear > thisyear) { // 明年以后
				return DATE.format(date);
			} else if (datemonth > thismonth) {// 下个月以后
				return SIMPLE.format(date);
			} else if (dateday > thisday + 2) {// 后天以后
				return format("d号 HH:mm", date);
			} else if (dateday > thisday + 1) {// 明天以后
				return format("后天 HH:mm", date);
			} else if (dateday > thisday) {// 今天以后
				return format("明天 HH:mm", date);
			} else {
				return format("今天 HH:mm", date);
			}
		}
	}

	public static String formatDuration(Date begDate, Date endDate) {
		StringBuilder builder = new StringBuilder(formatDate(begDate));
		builder.append(" - ");
		builder.append(formatDate(endDate));
		return builder.toString();
	}

	public static String formatDurationTime(Date begDate, Date endDate) {
		StringBuilder builder = new StringBuilder(formatTime(begDate));
		builder.append(" - ");
		builder.append(formatTime(endDate));
		return builder.toString();
	}
}
