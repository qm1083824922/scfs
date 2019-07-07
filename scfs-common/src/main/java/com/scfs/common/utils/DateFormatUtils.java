package com.scfs.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.DateTime;

/**
 * 
 * @author Administrator
 *
 */
public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String YYMMDD = "yyMMdd";
	public static final String YYYYMM = "yyyyMM";

	public static Date parse(String pattern, String date) throws ParseException {
		if (date == null) {
			return null;
		}
		SimpleDateFormat fmt = getDateFormat(pattern);
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			ParseException ex = new ParseException(e.getMessage() + " - expected '" + pattern + '\'',
					e.getErrorOffset());

			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}

	public static String format(String pattern, Date d) {
		if (d == null) {
			return null;
		}
		return getDateFormat(pattern).format(d);
	}

	public static Date formatToDate(String pattern, String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private static final ThreadLocal<ConcurrentMap<String, SimpleDateFormat>> locals = new ThreadLocal<ConcurrentMap<String, SimpleDateFormat>>();

	public static SimpleDateFormat getDateFormat(String pattern) {
		ConcurrentMap<String, SimpleDateFormat> map = (ConcurrentMap<String, SimpleDateFormat>) locals.get();
		if (map == null) {
			map = new ConcurrentHashMap<String, SimpleDateFormat>();
			locals.set(map);
		}
		SimpleDateFormat df = (SimpleDateFormat) map.get(pattern);
		if (df == null) {
			df = new SimpleDateFormat(pattern);
			map.put(pattern, df);
		}
		return df;
	}

	/**
	 * 得到应用服务器的当前时间
	 *
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 往前推n月
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date beforeMonth(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -n);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 往前推n天
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date beforeDay(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -n);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 往后推n天
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date afterDay(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, n);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 获取指定时间的 milliseconds
	 * 
	 * @param date
	 * @return
	 */
	public static long getMillis(Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 日期相减(返回秒值)
	 * 
	 * @param date
	 * @param date1
	 * @return long
	 */
	public static long diffDateTime(Date date, Date date1) {
		return (long) ((getMillis(date) - getMillis(date1)) / 1000);
	}

	/**
	 * 日期相减(返回天数)
	 * 
	 * @param date
	 * @param date1
	 * @return long
	 */
	public static long diffDate(Date date, Date date1) throws ParseException {
		date = parse(YYYY_MM_DD, format(date, YYYY_MM_DD));
		date1 = parse(YYYY_MM_DD, format(date1, YYYY_MM_DD));
		long day = (date.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param sDate
	 *            较小的时间
	 * @param bDate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date sDate, Date bDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		sDate = sdf.parse(sdf.format(sDate));
		bDate = sdf.parse(sdf.format(bDate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(sDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bDate);
		long time2 = cal.getTimeInMillis();
		long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(betweenDays));
	}

	/**
	 * 计算两个日期相减获取月份
	 * 
	 * @param sDate
	 * @param bDate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetweenMonth(String sData, String bDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMM);
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(sData));
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);

		c.setTime(sdf.parse(bDate));
		int year2 = c.get(Calendar.YEAR);
		int month2 = c.get(Calendar.MONTH);

		int result;
		if (year1 == year2) {
			result = month1 - month2;
		} else {
			result = 12 * (year1 - year2) + month1 - month2;
		}
		return result;
	}

	/**
	 * 计算指定月份区间
	 * 
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMM);// 格式化为年月

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(sdf.parse(minDate));
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

		max.setTime(sdf.parse(maxDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sdf.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}

		return result;
	}

	/**
	 * 字符串的日期格式的计算
	 * 
	 * @param sDate
	 *            较小的时间
	 * @param bDate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(String sDate, String bDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(sDate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bDate));
		long time2 = cal.getTimeInMillis();
		long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(betweenDays));
	}

	/**
	 * 获取某年第一天
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getFirstDateOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date firstDate = calendar.getTime();
		return firstDate;
	}

	/**
	 * 获取某年最后一天
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getEndDateOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date endDate = calendar.getTime();
		return endDate;
	}

	/**
	 * 获取某年某季的第一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getFirstDateOfQuarter(int year, int quarter) {
		Date firstDate = null;
		switch (quarter) {
		case 1:
			firstDate = getFirstDateOfMonth(year, 1);
			break;
		case 2:
			firstDate = getFirstDateOfMonth(year, 4);
			break;
		case 3:
			firstDate = getFirstDateOfMonth(year, 7);
			break;
		case 4:
			firstDate = getFirstDateOfMonth(year, 10);
			break;
		default:
			break;
		}
		return firstDate;
	}

	/**
	 * 获取某年某季的最后一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getEndDateOfQuarter(int year, int quarter) {
		Date endDate = null;
		switch (quarter) {
		case 1:
			endDate = getEndDateOfMonth(year, 3);
			break;
		case 2:
			endDate = getEndDateOfMonth(year, 6);
			break;
		case 3:
			endDate = getEndDateOfMonth(year, 9);
			break;
		case 4:
			endDate = getEndDateOfMonth(year, 12);
			break;
		default:
			break;
		}
		return endDate;
	}

	/**
	 * 获取某年某月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDateOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getEndDateOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取指定年月的上个月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDateOfPreMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.MONTH, -1);
		Date preMonthFirst = calendar.getTime();
		return preMonthFirst;
	}

	/**
	 * 获取指定年月的上个月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getEndDateOfPreMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.MONTH, -1);
		Date preMonthFirst = calendar.getTime();
		return preMonthFirst;
	}

	/**
	 * 获取上一年
	 * 
	 * @param year
	 * @return
	 */
	public static int getPreYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.add(Calendar.YEAR, -1);
		return calendar.get(Calendar.YEAR);
	}

	public static Date getPreYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
		return calendar.getTime();
	}

	/**
	 * 获取上一月
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPreMonthDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 获取每个月份
	 * 
	 * @return
	 */
	public static List<String> getBeforeMonths(Date startDate, Date endDate) {
		List<String> results = new ArrayList<String>();
		Calendar dd = Calendar.getInstance();// 定义日期实例
		dd.setTime(startDate);// 设置日期起始时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		while (dd.getTime().before(endDate)) {// 判断是否到结束日期
			String str = sdf.format(dd.getTime());
			results.add(str);
			dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1
		}
		results.add(sdf.format(endDate));
		return results;
	}

	/**
	 * 获取日
	 * 
	 * @return
	 */
	public static Integer getDateDay(Date nowDate) {
		Calendar dd = Calendar.getInstance();// 定义日期实例
		dd.setTime(nowDate);// 设置日期起始时间
		int day = dd.get(Calendar.DATE);// 获取日
		return day;
	}

	/***
	 * 获取当月1号日期
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static Date getNowDateFristDay(Date nowDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		int day = cal.get(Calendar.DATE);// 获取日
		cal.add(Calendar.DATE, -day);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	/**
	 * 获取两个时间差，格式00:00:00(时:分:秒)
	 * @return
	 */
	public static String getBetweenTime(Date startDate, Date endDate) {
        long l = endDate.getTime() - startDate.getTime();  
        long day = l / (24 * 60 * 60 * 1000);  
        long hour = (l / (60 * 60 * 1000) - day * 24);  
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);  
        long second = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  
        
        return String.valueOf(hour + day*24) + ":" + String.valueOf(min) + ":" +String.valueOf(second);
	}

	/**
	 * 获取两个时间差，单位小时(抹去分钟)
	 * @return
	 */
	public static long getBetweenHours(Date startDate, Date endDate) {
        long l = endDate.getTime() - startDate.getTime();  
        long day = l / (24 * 60 * 60 * 1000);  
        long hour = (l / (60 * 60 * 1000) - day * 24);          
        return hour + day*24;
	}
	
	 /**
     * 对日期的【秒】进行加/减
     *
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

	
	public static void main(String[] args) throws ParseException {
		System.out.println(getFirstDateOfMonth(2016, 2) + "=====" + getEndDateOfMonth(2016, 2));
		System.out.println(getFirstDateOfYear(2016) + "=====" + getEndDateOfYear(2016));
		System.out.println(getFirstDateOfPreMonth(2017, 1) + "=====" + getEndDateOfPreMonth(2017, 1));

		System.out.println(getFirstDateOfQuarter(2017, 1) + "=====" + getEndDateOfQuarter(2017, 1));

		System.out.println("");
		System.out.println(getPreYear(2017));

		System.out.println("");
		System.out.println(diffDate(parse(YYYY_MM_DD, "2017-01-10"), parse(YYYY_MM_DD, "2017-01-01")));

		System.out.println("");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		System.out.println(sdf.format(date));
		System.out.println(sdf.format(getPreMonthDate(date)));

		Date currDate = new Date();
		List<String> result = getBeforeMonths(getPreYear(currDate), currDate);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
		}
		System.out.println(format(YYYY_MM_DD,getNowDateFristDay(new Date())));
		
		
		System.out.println(getBetweenTime(parse(YYYY_MM_DD, "2017-03-07"), parse(YYYY_MM_DD, "2017-10-20")));
	}
}
