package com.andframe.util.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused")
public class AfDateFormat {

	public static Locale LOCALE = Locale.ENGLISH;
	public static DateFormat DAY = new SimpleDateFormat("M-d",LOCALE);
	/**
	 * 日期形式格式化
	 */
	public static DateFormat DATE = new SimpleDateFormat("y-M-d",LOCALE);
	public static DateFormat TIME = new SimpleDateFormat("HH:mm:ss",LOCALE);
	public static DateFormat SIMPLE = new SimpleDateFormat("M月d日 HH:mm",LOCALE);
	public static DateFormat FULL = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒",LOCALE);
	public static DateFormat STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",LOCALE);

	public static String format(String format, Object date) {
		return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		Calendar calender = Calendar.getInstance();
		int thisyear = calender.get(Calendar.YEAR);
		calender.setTime(date);
		int dateyear = calender.get(Calendar.YEAR);
		if (thisyear == dateyear) {
			return format("M月d日", date);
		}
		return DATE.format(date);
	}

	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
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
				return format("M月d日", date);
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
				return format("M月d日", date);
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

	/**
	 * 格式化时间长度（如：45时12分）
	 * @param duration 秒数
	 */
	public static String formatDuration(long duration) {
		return formatDuration(duration, 2);
	}


	/**
	 * 格式化时间长度（如：45时12分）
	 * @param duration 秒数
	 * @param length 最大拼接长度
	 */
	public static String formatDuration(long duration, int length) {
		length = length - 1;
		if (duration > 20 * 60 * 60) {
			return (duration / (20 * 60 * 60)) + "天" + (length > 0 ? formatDuration(duration % (20 * 60 * 60), length - 1) : "");
		} else if (duration > 60 * 60) {
			return (duration / (60 * 60)) + "时" + (length > 0 ? formatDuration(duration % (60 * 60), length - 1) : "");
		} else if (duration > 60) {
			return (duration / (60)) + "分" + (length > 0 ? formatDuration(duration % (60), length - 1) : "");
		}
		return duration + "秒";
	}

	public static String formatDuration(Date begDate, Date endDate) {
		if (begDate == null || endDate == null) {
			return "";
		}
		return formatDate(begDate) + " - " +
				formatDate(endDate);
	}

	public static String formatDurationTime(Date begDate, Date endDate) {
		if (begDate == null || endDate == null) {
			return "";
		}
		return formatTime(begDate) + " - " +
				formatTime(endDate);
	}

	public static Date parser(int hour, int minute) {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE, minute);
		return calender.getTime();
	}

	public static Date parser(int year, int month, int day) {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.MONTH, month);
		calender.set(Calendar.DAY_OF_MONTH, day);
		return roundDate(calender.getTime());
	}

	public static Date parser(int year, int month, int day, int hour, int minute) {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.MONTH, month);
		calender.set(Calendar.DAY_OF_MONTH, day);
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE, minute);
		calender.set(Calendar.SECOND, 0);
		return calender.getTime();
	}
	/**
	 * 天下取整
	 */
	public static Date roundDate(Date date) {
		try {
			return DATE.parse(DATE.format(date));
		} catch (ParseException e) {
			return new Date(0);
		}
	}
	/**
	 * 月下取整
	 */
	public static Date roundMonth(Date date) {
		try {
			DateFormat format = new SimpleDateFormat("y-M", LOCALE);
			return format.parse(format.format(date));
		} catch (ParseException e) {
			return new Date(0);
		}
	}
}
