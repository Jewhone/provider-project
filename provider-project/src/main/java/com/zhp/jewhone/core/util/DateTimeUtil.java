package com.zhp.jewhone.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Date 与 LocalDateTime/LocalDate/LocalTime 转换
 * 
 * @see LocalDateTime
 * @see LocalDate
 * @see LocalTime
 * @author yubing
 * @time 2016年9月26日
 */
public class DateTimeUtil {
	public static final int TYPE_YEAR = 1;// 类型年
	public static final int TYPE_MONTH = 2;// 类型月
	public static final int TYPE_DAY = 3;// 类型日
	public static final int TYPE_HOUR = 4;// 类型时
	public static final int TYPE_MINUTE = 5;// 类型分
	public static final int TYPE_SECOND = 6;// 类型秒

	/**
	 * 将时间字符串转换为Date类型
	 * 
	 * @param dateStr
	 * @return Date
	 */
	public static Date toDate(String dateStr) {
		LocalDate date = LocalDate.parse(dateStr);
		return convertLocalDateToDate(date);
	}

	/**
	 * 将时间字符串转换为Date类型
	 * 
	 * @param dateStr
	 * @return Date
	 */
	public static Date toDate(String dateStr, String source) {
		SimpleDateFormat sdf = new SimpleDateFormat(source);// 小写的mm表示的是分钟
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 将Date类型的时间转换为LocalDateTime
	 * 
	 * @param Date
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertDateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withNano(0);
	}

	/**
	 * 将Date类型的当前时间转换为LocalDateTime
	 * 
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertDateToLocalDateTime() {
		return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).withNano(0);
	}

	/**
	 * 将Date类型的时间转换为LocalTime
	 * 
	 * @param Date
	 * @return LocalTime
	 */
	public static LocalTime convertDateToLocalTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime().withNano(0);
	}

	/**
	 * 将Date类型的当前时间转换为LocalTime
	 * 
	 * @return LocalTime
	 */
	public static LocalTime convertDateToLocalTime() {
		return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalTime().withNano(0);
	}

	/**
	 * 将Date类型的时间转换为LocalDate
	 * 
	 * @param Date
	 * @return LocalDate
	 */
	public static LocalDate convertDateToLocalDate(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * 将Date类型的当前时间转换为LocalDate
	 * 
	 * @return LocalDate
	 */
	public static LocalDate convertDateToLocalDate() {
		return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * 将LocalDateTime类型的时间转换为Date
	 * 
	 * @param LocalDateTime
	 * @return Date
	 */
	public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将LocalDateTime类型的当前时间转换为Date
	 * 
	 * @return Date
	 */
	public static Date convertLocalDateTimeToDate() {
		return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将LocalDate类型的时间转换为Date
	 * 
	 * @param LocalDate
	 * @return Date
	 */
	public static Date convertLocalDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将LocalDate类型的当前时间转换为Date
	 * 
	 * @return Date
	 */
	public static Date convertLocalDateToDate() {
		return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将LocalTime类型的时间转换为Date
	 * 
	 * @param LocalTime
	 * @return Date
	 */
	public static Date convertLocalTimeToDate(LocalTime localTime) {
		return Date.from(LocalDateTime.of(LocalDate.now(), localTime).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将LocalTime类型的当前时间转换为Date
	 * 
	 * @return Date
	 */
	public static Date convertLocalTimeToDate() {
		return Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将LocalDateTime转换为指定pattern格式的日期时间字符串
	 * 
	 * @param LocalDateTime
	 * @param pattern
	 * @return String
	 */
	public static String convertLocalDateTimeToStringWithoutMillionsecond(LocalDateTime localDateTime, String pattern) {
		if (pattern == null)
			throw new NullPointerException("pattern should not be null.");
		if (pattern == "")
			throw new IllegalArgumentException("pattern should be a date format");
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将LocalDateTime转换为 "yyyy-MM-dd HH:mm:ss" 格式的日期时间字符串
	 * 
	 * @param LocalDateTime
	 * @return String
	 */
	public static String convertLocalDateTimeToStringWithoutMillionsecond(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate() + " " + localDateTime.toLocalTime().withNano(0);
	}

	private static final long ONE_MINUTE = 60;
	private static final long ONE_HOUR = 3600;
	public static final long ONE_DAY = 86400;
	private static final long ONE_MONTH = 2592000;
	private static final long ONE_YEAR = 31104000;
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static String formartSeccentToDateTime(Integer time) {
		int in = 86400;
		int seccent = time;
		int day = seccent / in;
		int dayTemp = (seccent - (day * in));// 计算天后剩余时间；
		int hour = dayTemp / 3600;// 小时
		int hourTemp = dayTemp - (hour * 3600);// 计算小时剩余时间
		int min = hourTemp / 60;
		int sec = hourTemp - min * 60;
		String str = "";
		if (day > 0) {
			str += day + "天";
		}
		if (hour > 0) {
			str += hour + "小时";
		}
		str += min + "分钟" + sec + "秒";

		return str;
	}

	public static Calendar calendar = Calendar.getInstance();

	/**
	 * 距离今天多久
	 * 
	 * @param date
	 * @return
	 *
	 */
	public static String fromToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long time = date.getTime() / 1000;
		long now = new Date().getTime() / 1000;
		long ago = now - time;
		if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟前";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟前";
		else if (ago <= ONE_DAY * 2)
			return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
		else if (ago <= ONE_DAY * 3)
			return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
		else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			return month + "个月" + day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
		} else {
			long year = ago / ONE_YEAR;
			int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
															// so month+1
			return year + "年前" + month + "月" + calendar.get(Calendar.DATE) + "日";
		}

	}

	/**
	 * 根据出生日期获取人的年龄
	 * 
	 * @param strBirthDate
	 *            (yyyy-mm-dd or yyyy/mm/dd)
	 * @return
	 */
	public static String getPersonAgeByBirthDate(Date dateBirthDate) {
		if (dateBirthDate == null) {
			return "";
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime localDateTime = convertDateToLocalDateTime(dateBirthDate);
		String strBirthDate = localDateTime.format(dtf);

		// 读取当前日期
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		// 计算年龄
		int age = year - Integer.parseInt(strBirthDate.substring(0, 4)) - 1;
		if (Integer.parseInt(strBirthDate.substring(5, 7)) < month) {
			age++;
		} else if (Integer.parseInt(strBirthDate.substring(5, 7)) == month && Integer.parseInt(strBirthDate.substring(8, 10)) <= day) {
			age++;
		}
		return String.valueOf(age);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime localDateTime = convertDateToLocalDateTime(date);
		return localDateTime.format(dtf);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (StringUtils.isEmpty(pattern)) {
			return formatDate(date);
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDateTime = convertDateToLocalDateTime(date);
		return localDateTime.format(dtf);
	}

	/**
	 * 给时间累加
	 * 
	 * @param type
	 *            类型年月日 时分秒
	 * @param 需要累加的数
	 *            可正数可负数
	 * @return
	 */

	public static Date addDate(Date date, int type, int amount) {
		if (date == null) {
			return null;
		}
		// 读取当前日期
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (type) {
		case TYPE_YEAR:
			c.add(Calendar.YEAR, amount);
			break;
		case TYPE_MONTH:
			c.add(Calendar.MONTH, amount);
			break;
		case TYPE_DAY:
			c.add(Calendar.DATE, amount);
			break;
		case TYPE_HOUR:
			c.add(Calendar.HOUR, amount);
		case TYPE_MINUTE:
			c.add(Calendar.MINUTE, amount);
		case TYPE_SECOND:
			c.add(Calendar.SECOND, amount);
			break;

		default:
			break;
		}
		return c.getTime();
	}

	/**
	 * 计算差值
	 * 
	 * @param date
	 * @return
	 */
	public static Period calculateDiffFromNow(Date date) {
		return Period.between(DateTimeUtil.convertDateToLocalDate(date), LocalDate.now());
	}

	public static String getBirthyDayByRealNumber(String number) {
		if (number.length() == 15) {
			Pattern pattern = Pattern.compile("^(\\d{6})(\\d{6})(.*)");
			Matcher matcher = pattern.matcher(number);
			if (matcher.find()) {
				String group = matcher.group(2);
				String year = group.substring(0, 4);
				String mon = group.substring(4, 6);
				String day = group.substring(6, 8);
				return year + "-" + mon + "-" + day;
			}
		} else if (number.length() == 18) {
			Pattern pattern = Pattern.compile("^(\\d{6})(\\d{8})(.*)");
			Matcher matcher = pattern.matcher(number);
			if (matcher.find()) {
				String group = matcher.group(2);
				String year = group.substring(0, 4);
				String mon = group.substring(4, 6);
				String day = group.substring(6, 8);

				return year + "-" + mon + "-" + day;
			}
		}
		return "";
	}
	
	/**
	 * 计算日期差(天)
	 * 
	 * @param date
	 * @return
	 */
	public static Long getDayFromNow(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		long time = date.getTime() / 1000;
		long now = new Date().getTime() / 1000;
		long ago = now - time;
		return ago / ONE_DAY;
	}
}
