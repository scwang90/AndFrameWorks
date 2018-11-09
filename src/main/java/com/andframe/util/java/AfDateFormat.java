package com.andframe.util.java;

import android.support.annotation.NonNull;

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
	public static DateFormat SIMPLE = new SimpleDateFormat("yyyy-M-d HH:mm",LOCALE);
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
		int thisYear = calender.get(Calendar.YEAR);
		calender.setTime(date);
		int dateYear = calender.get(Calendar.YEAR);
		if (thisYear == dateYear) {
			return format("M月d日", date);
		}
		return DATE.format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		Calendar calender = Calendar.getInstance();
		int thisYear = calender.get(Calendar.YEAR);
		calender.setTime(date);
		int dateYear = calender.get(Calendar.YEAR);
		if (thisYear == dateYear) {
			return format("M月d日 HH:mm", date);
		}
		return SIMPLE.format(date);
	}

	/**
	 * 显示倒计时长 - 最小单位 - 天
	 * @param date 截止时间
	 * @return 格式化的倒计时
	 */
	public static String formatCountdownToDay(Date date) {
		if (date == null) {
			return "";
		}
		Date roundNow = roundDate(new Date());
		Date roundDate = roundDate(date);
		if (roundNow.equals(roundDate)) {
			return "今天";
		} else {
			long span = roundDate.getTime() - roundNow.getTime();
			if (span > 0) {
				return (span / 24 / 3600 / 1000) + "天";
			} else {
				return "已过" + (-span / 24 / 3600 / 1000) + "天";
			}
		}
	}

	/**
	 * 显示倒计时长 - 最小单位 - 小时
	 * @param date 截止时间
	 * @return 格式化的倒计时
	 */
	public static String formatCountdownToHour(Date date) {
		if (date == null) {
			return "";
		}
		Date roundNow = roundHour(new Date());
		Date roundDate = roundHour(date);
		if (roundNow.equals(roundDate)) {
			return "现在";
		} else {
			String day = "";
			long span = roundDate.getTime() - roundNow.getTime();
			int daySpan = 24 * 3600 * 1000;
			if (span < 0) {
				day = "已过";
				span = -span;
			}
			if (span > daySpan) {
				long daytime = (span - (span % daySpan));
				day = day + ((daytime) / 24 / 3600 / 1000) + "天";
				span = span - daytime;
			}
			return day + (span / 3600 / 1000) + "时";
		}
	}

	/**
	 * 显示倒计时长 - 最小单位 - 秒
	 * @param date 截止时间
	 * @return 格式化的倒计时
	 */
	public static String formatCountdownToSecond(Date date) {
		if (date == null) {
			return "";
		}
		Date roundNow = new Date();
		Date roundDate = new Date(date.getTime());
		if (roundNow.equals(roundDate)) {
			return "现在";
		} else {
			String day = "";
			long span = roundDate.getTime() - roundNow.getTime();
			int daySpan = 24 * 3600 * 1000;
			if (span < 0) {
				day = "已过";
				span = -span;
			}
			if (span > daySpan) {
				long dayTime = (span - (span % daySpan));
				day = day + (dayTime / daySpan) + "天";
				span = span - dayTime;
			}
			int hourSpan = 3600 * 1000;
			if (span > hourSpan) {
				long hourTime = (span - (span % hourSpan));
				day = day + (hourTime / hourSpan) + "时";
				span = span - hourTime;
			} else if (day.contains("天")) {
				day = day + "0时";
			}
			int minuteSpan = 60 * 1000;
			if (span > minuteSpan) {
				long minuteTime = (span - (span % minuteSpan));
				day = day + (minuteTime / minuteSpan) + "分";
				span = span - minuteTime;
			} else if (day.contains("时")) {
				day = day + "0分";
			}

			return day + (span / 1000) + "秒";
		}
	}

	/**
	 * 动态格式化时间 如 昨天，今天
	 */
	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		Calendar calender = Calendar.getInstance();
		int thisDay = calender.get(Calendar.DAY_OF_MONTH);
		int thisMonth = calender.get(Calendar.MONTH);
		int thisYear = calender.get(Calendar.YEAR);

		Date now = calender.getTime();
		calender.setTime(date);

		int dateDay = calender.get(Calendar.DAY_OF_MONTH);
		int dateMonth = calender.get(Calendar.MONTH);
		int dateYear = calender.get(Calendar.YEAR);
		if (date.before(now)) {
			if (dateYear < thisYear) {// 今年以前
				return DATE.format(date);
			} else if (dateMonth < thisMonth) {// 这个月以前
				return format("M月d日", date);
			} else if (dateDay < thisDay - 2) {// 前天之前
				return format("d号 HH:mm", date);
			} else if (dateDay < thisDay - 1) {// 昨天之前
				return format("前天 HH:mm", date);
			} else if (dateDay < thisDay) {// 今天之前
				return format("昨天 HH:mm", date);
			} else {
				return format("今天 HH:mm", date);
			}
		} else {
			if (dateYear > thisYear) { // 明年以后
				return DATE.format(date);
			} else if (dateMonth > thisMonth) {// 下个月以后
				return format("M月d日", date);
			} else if (dateDay > thisDay + 2) {// 后天以后
				return format("d号 HH:mm", date);
			} else if (dateDay > thisDay + 1) {// 明天以后
				return format("后天 HH:mm", date);
			} else if (dateDay > thisDay) {// 今天以后
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
		if (duration > 24 * 60 * 60) {
			return (duration / (24 * 60 * 60)) + "天" + (length > 0 ? formatDuration(duration % (20 * 60 * 60), length) : "");
		} else if (duration > 60 * 60) {
			return (duration / (60 * 60)) + "时" + (length > 0 ? formatDuration(duration % (60 * 60), length) : "");
		} else if (duration > 60) {
			return (duration / (60)) + "分" + (length > 0 ? formatDuration(duration % (60), length) : "");
		}
		return duration + "秒";
	}

	/**
	 * 格式化一个 起始时间段 - 精确到 天
	 * @param begDate 开始时间
	 * @param endDate 结束时间
	 * @return 返回格式化内容
	 */
	public static String formatDuration(Date begDate, Date endDate) {
		if (begDate == null || endDate == null) {
			return "";
		}
		return formatDate(begDate) + " - " +
				formatDate(endDate);
	}

	/**
	 * 格式化一个 起始时间段 - 精确到 分钟
	 * @param begDate 开始时间
	 * @param endDate 结束时间
	 * @return 返回格式化内容
	 */
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


	public static Date parser(Date date, int hour, int minute) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE, minute);
		return calender.getTime();
	}

	public static Date parser(Date date, int year, int month, int day) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
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
		calender.set(Calendar.MILLISECOND,0);
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

	/**
	 * 小时下取整
	 */
	public static Date roundHour(Date date) {
		try {
			DateFormat format = new SimpleDateFormat("y-M-d HH", LOCALE);
			return format.parse(format.format(date));
		} catch (ParseException e) {
			return new Date(0);
		}
	}

	/**
	 * 分钟下取整
	 */
	public static Date roundMinute(Date date) {
		try {
			return SIMPLE.parse(SIMPLE.format(date));
		} catch (ParseException e) {
			return new Date(0);
		}
	}

	public static boolean isSameYear(Date date1, Date date2) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date1);
		int year = calender.get(Calendar.YEAR);
		calender.setTime(date2);
		return year == calender.get(Calendar.YEAR);
	}

	public static boolean isSameMonth(Date date1, Date date2) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date1);
		int year = calender.get(Calendar.YEAR);
		int month = calender.get(Calendar.MONTH);
		calender.setTime(date2);
		return year == calender.get(Calendar.YEAR)
				&& month == calender.get(Calendar.MONTH);
	}

	public static boolean isSameDay(Date date1, Date date2) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date1);
		int year = calender.get(Calendar.YEAR);
		int month = calender.get(Calendar.MONTH);
		int day = calender.get(Calendar.DAY_OF_MONTH);
		calender.setTime(date2);
		return year == calender.get(Calendar.YEAR)
				&& month == calender.get(Calendar.MONTH)
				&& day == calender.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 格式化一个时间段
	 * @param start 开始时间
	 * @param close 结束时间
	 * @param split 中间分隔符
	 * @param formats formats[0] 带年份的格式 formats[1] 不带年份的日期
	 */
	public static String formatTimeSpan(@NonNull Date start,@NonNull Date close,@NonNull String split, String... formats) {
		String formatYear = formats.length > 0 ? formats[0] : "Y-M-d";
		String formatDate = formats.length > 1 ? formats[1] : formatYear;
		if (isSameYear(start, close)) {
			return String.format("%s %s %s", format(formatDate, start), split, format(formatDate, close));
		} else {
			return String.format("%s %s %s", format(formatYear, start), split, format(formatYear, close));
		}
	}
}
