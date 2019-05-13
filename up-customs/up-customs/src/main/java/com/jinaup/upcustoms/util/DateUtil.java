package com.jinaup.upcustoms.util;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author 作者:shihai.liu
 * @since 2018年3月12日
 *
 */
public class DateUtil {

	private static Logger log = LoggerFactory.getLogger(DateUtil.class);

	/** 毫秒 */
	public final static long MS = 1;
	/** 每秒钟的毫秒数 */
	public final static long SECOND_MS = MS * 1000;
	/** 每分钟的毫秒数 */
	public final static long MINUTE_MS = SECOND_MS * 60;
	/** 每小时的毫秒数 */
	public final static long HOUR_MS = MINUTE_MS * 60;
	/** 每天的毫秒数 */
	public final static long DAY_MS = HOUR_MS * 24;

	/** 标准日期格式 */
	public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/** 标准时间格式 */
	public final static String NORM_TIME_PATTERN = "HH:mm:ss";
	/** 标准日期时间格式 */
	public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** HTTP头中日期时间格式 */
	public final static String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

	/** 标准时间格式 */
	public final static String NORM_HM_PATTERN = "HH:mm:ss";

	/** YMD日期时间格式 */
	public final static String NORM_YMD_PATTERN = "yyyyMMdd";
	/** YMDH日期时间格式 */
	public final static String NORM_YMDH_PATTERN = "yyyyMMddHH";
	
	/** YMDHMS日期时间格式 */
	public final static String NORM_YMDHMS_PATTERN = "yyyyMMddHHmmss";

	/** 标准日期（不含时间）格式化器 */
	private final static SimpleDateFormat NORM_DATE_FORMAT = new SimpleDateFormat(NORM_DATE_PATTERN);
	/** 标准时间格式化器 */
	private final static SimpleDateFormat NORM_TIME_FORMAT = new SimpleDateFormat(NORM_TIME_PATTERN);
	/** 标准日期时间格式化器 */
	private final static SimpleDateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat(NORM_DATETIME_PATTERN);
	/** HTTP日期时间格式化器 */
	private final static SimpleDateFormat HTTP_DATETIME_FORMAT = new SimpleDateFormat(HTTP_DATETIME_PATTERN, Locale.US);

	/** 标准日期时间格式化器 */
	private final static SimpleDateFormat NORM_YMD_FORMAT = new SimpleDateFormat(NORM_YMD_PATTERN);

	private final static SimpleDateFormat NORM_HM_FORMAT = new SimpleDateFormat(NORM_HM_PATTERN);

	private final static SimpleDateFormat NORM_YMDH_FORMAT = new SimpleDateFormat(NORM_YMDH_PATTERN);
	
	private final static SimpleDateFormat NORM_YMDHMS_FORMAT = new SimpleDateFormat(NORM_YMDHMS_PATTERN);
	
	private static ThreadLocal<DateFormat> dateToSecFormatter = new ThreadLocal<DateFormat>();
	
	private static ThreadLocal<DateFormat> weixinDateFormatter = new ThreadLocal<DateFormat>();
	
	private static ThreadLocal<DateFormat> dateToMonthFormatter = new ThreadLocal<DateFormat>();
	
	/**
	 * 当前时间，格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 当前时间的标准形式字符串
	 */
	public static String now() {
		return formatDateTime(new Date());
	}

	/**
	 * 当前日期，格式 yyyy-MM-dd
	 * 
	 * @return 当前日期的标准形式字符串
	 */
	public static String today() {
		return formatDate(new Date());
	}

	// ------------------------------------ Format start
	// ----------------------------------------------
	/**
	 * 根据特定格式格式化日期
	 * 
	 * @param date
	 *            被格式化的日期
	 * @param format
	 *            格式
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(Date date) {
		return NORM_DATETIME_FORMAT.format(date);
	}

	/**
	 * 格式 yyyyMMdd
	 * 
	 * @param date
	 *            被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateYmd(Date date) {
		return NORM_YMD_FORMAT.format(date);
	}

	/**
	 * 格式化为Http的标准日期格式
	 * 
	 * @param date
	 *            被格式化的日期
	 * @return HTTP标准形式日期字符串
	 */
	public static String formatHttpDate(Date date) {
		// return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
		// Locale.US).format(date);
		return HTTP_DATETIME_FORMAT.format(date);
	}

	/**
	 * 格式 yyyy-MM-dd
	 * 
	 * @param date
	 *            被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		// return format(d, "yyyy-MM-dd");
		return NORM_DATE_FORMAT.format(date);
	}

	public static String formatHmDate(Date date) {
		return NORM_HM_FORMAT.format(date);
	}
	public static String formatDateYmdh(Date date) {
		return NORM_YMDH_FORMAT.format(date);
	}
	
	public static String formatDateYmdhms(Date date) {
		return NORM_YMDHMS_FORMAT.format(date);
	}
	// ------------------------------------ Format end
	// ----------------------------------------------

	// ------------------------------------ Parse start
	// ----------------------------------------------
	/**
	 * 将特定格式的日期转换为Date对象
	 * 
	 * @param dateString
	 *            特定格式的日期
	 * @param format
	 *            格式，例如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static Date parse(String dateString, String format) {
		try {
			return (new SimpleDateFormat(format)).parse(dateString);
		} catch (ParseException e) {
			log.error("Parse " + dateString + " with format " + format + " error!", e);
		}
		return null;
	}

	/**
	 * 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString
	 *            标准形式的时间字符串
	 * @return 日期对象
	 */
	public static Date parseDateTime(String dateString) {
		// return parse(s, "yyyy-MM-dd HH:mm:ss");
		try {
			return NORM_DATETIME_FORMAT.parse(dateString);
		} catch (ParseException e) {
			log.error("Parse " + dateString + " with format " + NORM_DATETIME_FORMAT.toPattern() + " error!", e);
		}
		return null;
	}

	/**
	 * 格式yyyy-MM-dd
	 * 
	 * @param dateString
	 *            标准形式的日期字符串
	 * @return 日期对象
	 */
	public static Date parseDate(String dateString) {
		try {
			return NORM_DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			log.error("Parse " + dateString + " with format " + NORM_DATE_PATTERN + " error!", e);
		}
		return null;
	}

	/**
	 * 格式HH:mm:ss
	 * 
	 * @param timeString
	 *            标准形式的日期字符串
	 * @return Date
	 */
	public static Date parseTime(String timeString) {
		try {
			return NORM_TIME_FORMAT.parse(timeString);
		} catch (ParseException e) {
			log.error("Parse " + timeString + " with format " + NORM_TIME_PATTERN + " error!", e);
		}
		return null;
	}

	/**
	 * 格式： 1、yyyy-MM-dd HH:mm:ss 2、yyyy-MM-dd 3、HH:mm:ss
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @return Date
	 */
	public static Date parse(String dateStr) {
		int length = dateStr.length();
		try {
			if (length == DateUtil.NORM_DATETIME_PATTERN.length()) {
				return parseDateTime(dateStr);
			} else if (length == DateUtil.NORM_DATE_PATTERN.length()) {
				return parseDate(dateStr);
			} else if (length == DateUtil.NORM_TIME_PATTERN.length()) {
				return parseTime(dateStr);
			}
		} catch (Exception e) {
			log.error("Parse " + dateStr + " with format normal error!", e);
		}
		return null;
	}
	// ------------------------------------ Parse end
	// ----------------------------------------------

	// ------------------------------------ Offset start
	// ----------------------------------------------

	/**
	 * 
	 * @author shihai.liu
	 * @date 2019年2月23日
	 * @param dayType 0 昨天 1：今天 2：明天
	 * @return
	 */
	public static String getYTTDay(int dayType) {
		if(dayType == 0) {
			Date date = offsiteDate(new Date(), Calendar.DAY_OF_YEAR, -1);
			return NORM_YMD_FORMAT.format(date);
		}else if(dayType == 1) {
			Date date = offsiteDate(new Date(), Calendar.DAY_OF_YEAR, 0);
			return NORM_YMD_FORMAT.format(date);
		}else {
			Date date = offsiteDate(new Date(), Calendar.DAY_OF_YEAR, 1);
			return NORM_YMD_FORMAT.format(date);
		}
	}
	
	/**
	 * 昨天
	 * 
	 * @return Date
	 */
	public static Date yesterday() {
		return offsiteDate(new Date(), Calendar.DAY_OF_YEAR, -1);
	}

	/**
	 * 上周
	 * 
	 * @return Date
	 */
	public static Date lastWeek() {
		return offsiteDate(new Date(), Calendar.WEEK_OF_YEAR, -1);
	}

	/**
	 * 上个月
	 * 
	 * @return Date
	 */
	public static Date lastMouth() {
		return offsiteDate(new Date(), Calendar.MONTH, -1);
	}

	/**
	 * 获取指定日期偏移指定时间后的时间
	 * 
	 * @param date
	 *            基准日期
	 * @param calendarField
	 *            偏移的粒度大小（小时、天、月等）使用Calendar中的常数
	 * @param offsite
	 *            偏移量，正数为向后偏移，负数为向前偏移
	 * @return Date
	 */
	public static Date offsiteDate(Date date, int calendarField, int offsite) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarField, offsite);
		return cal.getTime();
	}
	// ------------------------------------ Offset end
	// ----------------------------------------------

	/**
	 * 判断两个日期相差的时长 返回 minuend - subtrahend 的差
	 * 
	 * @param subtrahend
	 *            减数日期
	 * @param minuend
	 *            被减数日期
	 * @param diffField
	 *            相差的选项：相差的天、小时
	 * @return long
	 */
	public static long diff(Date subtrahend, Date minuend, long diffField) {
		long diff = minuend.getTime() - subtrahend.getTime();
		return diff / diffField;
	}

	/**
	 * 计时，常用于记录某段代码的执行时间，单位：纳秒
	 * 
	 * @param preTime
	 *            之前记录的时间
	 * @return long
	 */
	public static long spendNt(long preTime) {
		return System.nanoTime() - preTime;
	}

	/**
	 * 计时，常用于记录某段代码的执行时间，单位：毫秒
	 * 
	 * @param preTime
	 *            之前记录的时间
	 * @return long
	 */
	public static long spendMs(long preTime) {
		return System.currentTimeMillis() - preTime;
	}

	/**
	 * 
	 * @Title: GetmilliSecondsLeftToday @Description: 一天还剩下多少毫秒 @author
	 * mac.liu @date 2017年5月18日 @return @return int @throws
	 */
	public static int getmilliSecondsLeftToday() {
		Long milliSecondsLeftToday = 86400000 - DateUtils.getFragmentInMilliseconds(Calendar.getInstance(), Calendar.DATE);
		return milliSecondsLeftToday.intValue();
	}
	/**
	 * 
	 * @Title: GetmilliSecondsLeftToday @Description: 一天还剩下多少秒 @author
	 * mac.liu @date 2017年5月18日 @return @return int @throws
	 */
	public static int getSecondsLeftToday() {
		Long milliSecondsLeftToday = 86400 - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE);
		return milliSecondsLeftToday.intValue();
	}

	/**
	 * 
	 * @Title: getSecondsLeftFuture @Description: 将来到现在还剩下多少秒 @author
	 * mac.liu @date 2017年5月26日 @param date @return @return int @throws
	 */
	public static int getSecondsLeftFuture(Date date) {

		Long leftMinSed = (date.getTime() - System.currentTimeMillis()) / 1000;
		if (leftMinSed > 0) {
			return leftMinSed.intValue();
		} else {
			return 0;
		}
	}

	public static long getLeftSubDay(Date futureDate) {
		long leftDay = DateUtils.getFragmentInDays(futureDate, Calendar.YEAR) - DateUtils.getFragmentInDays(Calendar.getInstance(), Calendar.YEAR);
		return leftDay;
	}

	public static Date getFutureDay(int days) throws ParseException {
		Date date = new Date();
		DateFormat df = dateToSecFormatter.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyy-MM-dd");
			dateToSecFormatter.set(df);
		}
		Date d = df.parse(df.format(date));
		Long time = d.getTime() + (1l * days * 24 * 60 * 60 * 1000);
		return new Date(time);
	}
	
	/**
	 * 返回yyyyMM
	 * @author shihai.liu
	 * @date 2019年4月24日
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getYM(Date date) throws ParseException {
		DateFormat df = dateToMonthFormatter.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyyMM");
			dateToMonthFormatter.set(df);
		}
		return df.format(date);
	}

	public static Date getLastDay(int days) {
		Date date = new Date();
		DateFormat df = dateToSecFormatter.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyy-MM-dd");
			dateToSecFormatter.set(df);
		}
		Long time = 0l;
		try {
			Date d = df.parse(df.format(date));
			time = d.getTime() - (1l * days * 24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date(time);
	}
	/**
	 * 
	 * @Title: getCurrentZeroTime @Description: TODO(当天的零点秒数) @param @return
	 * 设定文件 @return int 返回类型 @author youle.heng @throws
	 */
	public static int getCurrentZeroTime() {
		long current = System.currentTimeMillis();// 当前时间毫秒数
		long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
		return (int) (zero / 1000);
	}

	/**
	 * 微信订单过期时间
	 * 
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @return
	 */
	public static String weixinExpire() {
		DateFormat df = weixinDateFormatter.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyyMMddHHmmss");
			weixinDateFormatter.set(df);
		}
		return df.format(new Date(System.currentTimeMillis() + 1l * 30 * 60 * 1000));
	}
	
	
	/**
	 * 
	 * @Title: getDayLastTime   
	 * @Description: TODO(获取传入的日期 设置到 23:59:59)   例如 传入 2019-02-23 16:21:21 出来的值为 2019-02-23 23:59:59
	 * @param: @param date
	 * @param: @return      
	 * @return: Date      
	 * @throws
	 */
	public static Date  getDayLastTime(Date date) {
		String dateStr = DateUtil.formatDate(date) + " 23:59:59";
		return DateUtil.parse(dateStr);
	}
	
	/**
	 * 
	 * @Title: getDayLastMonthDay   
	 * @Description: TODO(距离月末的天数)   
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	public static int getDayLastMonthDay() {
		Calendar c = Calendar.getInstance();
		int d = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int now = c.get(Calendar.DAY_OF_MONTH);
		return d - now;
	}
}
