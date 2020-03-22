package javax.core.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具包
 * 
 * @author tanyongde
 */
public class DateUtils {
	
	private DateUtils(){}

	/**
	 * 获取当前日期 格式: yyyy-M-d
	 * 
	 * @return
	 */
	public static String getDate() {
		Date date = new Date();
		String formater = "yyyy-M-d"; // yyyy-M-d
		SimpleDateFormat format = new SimpleDateFormat(formater);
		return format.format(date);
	}

	/**
	 * 获取当前日期 格式:参数 例如 yyyy-MM-dd
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getDate(String formatStr) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(formatStr);
		// TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");
		// df.setTimeZone(zone);
		return df.format(c.getTime());
	}

	/**
	 * 格式化指定日期 格式:参数 例如 yyyy-MM-dd
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String getDate(Date date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(StringUtils.notNull(formatStr, "yyyy-MM-dd HH:mm:ss"));// yyyy-MM-dd
		// HH:mm:ss
		return date == null ? "" : format.format(date);
	}

	/**
	 * 获取当前日期时间 格式: yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static Date getDateTime() {
		return getDateTime(getDate("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 获取当前日期时间 格式: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dtime
	 * @return Date
	 */
	public static Date getDateTime(String dtime) {
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format1.parse(dtime);
		} catch ( ParseException e ) {}
		return null;
	}

	/**
	 * 获取当前日期时间 格式:参数 例如 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dtime
	 * @param formatStr
	 * @return
	 */
	public static Date getDateTime(String dtime, String formatStr) {
		try {
			DateFormat format1 = new SimpleDateFormat(formatStr);
			return format1.parse(dtime);
		} catch ( ParseException e ) {}
		return null;
	}

	/**
	 * 格式化时间 格式: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDate(String date) {
		String formater = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(formater);
		return date == null ? null : format.format(getDateTime(date));
	}

	/**
	 * 格式化时间 格式:第2参数 例如 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @param formatStr
	 * @return String
	 */
	public static String formatDate(String date, String formatStr) {
		String formater = formatStr;
		SimpleDateFormat format = new SimpleDateFormat(formater);
		return date == null ? null : format.format(getDateTime(date));
	}

	/**
	 * 取得某一特定的日期
	 * 
	 * @param sDate
	 * @param iDay
	 * @param sformat
	 * @return
	 */
	public static String getSomeDate(String sDate, int iDay, String sformat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(sformat);
			Date date = format.parse(sDate);
			long Time = (date.getTime() / 1000) + 60 * 60 * 24 * iDay;
			date.setTime(Time * 1000);
			return format.format(date);
		} catch ( Exception ex ) {
			return "";
		}
	}

	/**
	 * 时间间隔 是否在当前时间范围
	 * 
	 * @param btime
	 * @param etime
	 * @return
	 */
	public static boolean dateBound(String btime, String etime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = formatter.parse(btime, new ParsePosition(0));
		Date ed = formatter.parse(etime, new ParsePosition(0));
		return dateBound(sd, ed);
	}

	public static boolean dateBound(Date sd, Date ed) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date td = formatter.parse(getDate(), new ParsePosition(0)); // 当前时间
		if (sd == null || ed == null || td == null) return false;
		if (sd.getTime() > td.getTime() || ed.getTime() < td.getTime()) return false;
		return true;
	}
	
	/**
	 * 显示当前时间与目标时间的距离
	 * 
	 * **秒前  ** 分钟前  ** 小时前 超过7天显示时间
	 * @param ctime
	 * @param format
	 * @return
	 */
	public static String distance(Date ctime, String format) {
		if (ctime == null) return "";
		format = StringUtils.notNull(format, "yyyy-MM-dd HH:mm");
		long result = Math.abs(System.currentTimeMillis() - ctime.getTime());
		if (result < 60000) {
			return (result / 1000) + "秒钟前";
		} else if (result >= 60000 && result < 3600000) {
			return (result / 60000) + "分钟前";
		} else if (result >= 3600000 && result < 86400000) {
			return (result / 3600000) + "小时前";
		} else if (result >= 86400000 && result < 86400000 * 7) {
			return (result / 86400000) + "天前";
		} else {
			return getDate(ctime, format);
		}
	}
}
