package javax.core.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 时间操作
 * @author Tanyongde
 *
 */
public class TimeUtils {
	
	private TimeUtils(){}
	
	/**
	 * 
	 * 将给定时间 增加 到指定的 年月日，返回增加后的 字符串
	 *
	 *
	 * <br/>作者： 谭勇德
	 * 编写日期： Apr 7, 2011
	 * 版本 V1.0
	 * @param endTime 默认时间格式为 yyyy-MM-dd
	 * @param year 年数,如果设置值大于0，则增加。如果设置值小于0，则减
	 * @param month 月数,如果设置值大于0，则增加。如果设置值小于0，则减
	 * @param day 日数,如果设置值大于0，则增加。如果设置值小于0，则减
	 * @return    
	 * @throws Exception 
	 * @throws
	 */
	public static String addTime(String endTime,String formater,int year,int month,int day) throws Exception{
		formater = StringUtils.isEmpty(formater) ? "yyyy-MM-dd" : formater;
		if(!StringUtils.isEmpty(endTime)){
			SimpleDateFormat sdf = new SimpleDateFormat(formater);
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(endTime));
			if(0 != year){
				c.add(Calendar.YEAR, year);
			}
			if(0 != month){
				c.add(Calendar.MONTH, month);
			}
			if(0 != day){
				c.add(Calendar.DAY_OF_MONTH, day);
			}
			return sdf.format(c.getTime());
		}else{
			throw new Exception("日期格式不正确");
		}
	}
	

	/**
	 * 比较一个日期字符串是否在指定 日期区间
	 * 
	 * @param currTime
	 *            当前时间
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	private static boolean equalsTime(String currTime, String startTime,
			String endTime,String formater) {
		boolean flag = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formater);

			// 将 字符串时间 转换为 日期 类型
			Date currTimeDate = sdf.parse(currTime);
			Date startTimeDate = sdf.parse(startTime);
			Date endTimeDate = sdf.parse(endTime);
			// 如果当前时间 大于 指定时间，将返回大于 0 的数，小于则返回小于 0 的数，等于则返回 0
			int startFlag = currTimeDate.compareTo(startTimeDate);
			int endFlag = currTimeDate.compareTo(endTimeDate);

			// 第一种情况,与 起始时间 或者 结束时间相等
			// 第二种情况 在 两者之间
			if ((0 == startFlag || 0 == endFlag)
					|| (0 < startFlag && 0 > endFlag)) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 
	 * 获取系统日期，默认格式  yyyy-MM-dd
	 *
	 * @return    
	 * @throws
	 */
	public static String sysdate(){
		return sysdate("yyyy-MM-dd");
	}
	
	/**
	 * 获取系统日期
	 * @param formater
	 * @return
	 */
	public static String sysdate(String formater){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(c.getTime());
	}
	
	/**
	 * 获取当月的第一天，并格式化
	 * @param dateTime
	 * @param formater
	 * @return
	 */
	public static String firstDayOfMonth(String dateTime,String formater){
		String result = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formater);
			Date date = sdf.parse(dateTime);
			date.setDate(1);
			result = sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 获取当月的最后一天
	 *
	 * @param datetime
	 * @param formater
	 * @return    
	 * @throws
	 */
	 public static String lastDayOfMonth(String datetime,String formater) {
		 try
		 {
			SimpleDateFormat sdf = new SimpleDateFormat(formater);
			Date date = sdf.parse(datetime);
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        cal.set(Calendar.DAY_OF_MONTH, value);
	        datetime = sdf.format(cal.getTime());
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
	        return datetime;
	    }
	 
	 /**
		 * 返回格式化的当前日期，格式为yyyy-MM-dd
		 * 
		 * @return
		 */
		public static String getFullDate() {
			String formater = "yyyy-MM-dd";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			Date myDate = new Date();
			return format.format(myDate);
		}

		/**
		 * 返回格式化的当前日期，格式为yyyy-M-d
		 * 
		 * @return
		 */
		public static String getSimpleDate() {
			String formater = "yyyy-M-d";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			Date myDate = new Date();
			return format.format(myDate);
		}

		/**
		 * 返回本周的区间的格式化的日期对，格式为yyyy-MM-dd HH:mm:ss
		 * 
		 * @return
		 */
		public static String[] getSimpleDateRangeOfWeek() {
			String today = TimeUtils.getFullDate();
			int endWeekDay = TimeUtils.getWeekDay(today);
			// 以取出的第一个时间为准，向前推算第一个星期一的时间
			String startDate = TimeUtils.getSomeDate(today + " 00:00:00",
					-(endWeekDay - 1), "yyyy-MM-dd");
			String endDate = TimeUtils.getSomeDate(startDate, 6, "yyyy-MM-dd")
					.substring(0, 10)
					+ " 24:00:00";
			String DateRang[] = new String[2];
			DateRang[0] = startDate;
			DateRang[1] = endDate;
			return DateRang;
		}

		/**
		 * 返回格式化的间隔iDay天的日期
		 * 
		 * @param sDate
		 *            时间
		 * @param iDay
		 *            间隔天数
		 * @param formter
		 *            时间格式："yyyy-MM-dd"或"yyyy-M-d"
		 * @return
		 */
		public static String getSomeDate(String sDate, int iDay, String formter) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(formter);
				format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
				Date date = format.parse(sDate);
				long Time = (date.getTime() / 1000) + 60 * 60 * 24 * iDay;
				date.setTime(Time * 1000);
				return format.format(date);
			} catch (Exception ex) {
				return "";
			}
		}

		/**
		 * 计算两个日期间的天数差
		 * @param sDate1
		 * @param sDate2
		 * @param formter
		 * @return
		 * @exception return 0
		 */
		public static long getDaysFrom2Dates(String sDate1, String sDate2, String formter) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(formter);
				Date date = format.parse(sDate1);
				Date date1 = format.parse(sDate2);
				return (date.getTime()-date1.getTime())/(1000*60*60*24);
			} catch (Exception ex) {
				System.out.println("Error in TimeUtil.getDaysFrom2Dates,"+ex.toString());
				return 0L;
			}
		}
		
		/**
		 * 返回格式化的间隔iDay天的时间
		 * 
		 * @param sDate
		 *            时间
		 * @param iDay
		 *            间隔天数
		 * @param formter
		 *            时间格式："yyyy-MM-dd HH:mm:ss"或"yyyy-M-d H:m:s"
		 * @return
		 */
		public static String getSomeTime(String sDate, int iDay, String formter) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(formter);
				Date date = format.parse(sDate);
				long Time = (date.getTime() / 1000) + 60 * 60 * 24 * iDay;
				date.setTime(Time * 1000);
				return format.format(date);
			} catch (Exception ex) {
				return "";
			}
		}

		/**
		 * 求得某天是一周的第几天，以周一为第1天记起
		 * 
		 * @param strDate
		 * @return
		 */
		public static int getWeekDay(String strDate) {
			int strWeekDay = 0;
			SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date mydate = null;
			Calendar cal = Calendar.getInstance();
			try {
				mydate = myFormatter.parse(strDate);
				cal.setTime(mydate);
				strWeekDay = cal.get(Calendar.DAY_OF_WEEK);
				if (strWeekDay == 1)
					strWeekDay = 7;
				else
					strWeekDay = strWeekDay - 1;
			} catch (ParseException e) {
				System.out.println("Error in TimeUtil.getWeekDay()"
						+ e.getMessage());
			}
			return strWeekDay;
		}

		/**
		 * 判断第一个参数日期是否晚于第二个参数日期
		 * 
		 * @param sDate1
		 * @param sDate2
		 * @return
		 */
		public static boolean isDateLater(String sDate1, String sDate2) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date1 = format.parse(sDate1);
				Date date2 = format.parse(sDate2);
				if (date1.after(date2))
					return true;
				else
					return false;
			} catch (Exception ex) {
				return false;
			}
		}

		/**
		 * 根据年和月计算出所给的月份最后一天
		 * @param year 年
		 * @param month 月
		 * @return
		 */
		public static int getEndday(int year,int month){
			int endDay=0;
			if(month<1||month>12||year<1753||year>9999)
				return 0;
			switch(month){
				case 4:
					endDay = 30;
					break;
				case 6:
					endDay = 30;
					break;
				case 9:
					endDay = 30;
					break;
				case 11:
					endDay = 30;
					break;
				case 2:
					if (year % 4 == 0)
						endDay = 29;
					else
						endDay = 28;
					break;
				default:
					endDay = 31;
					break;
			}
			return endDay;
		}
		
		/**
		 * 判断是否为合法日期
		 * @param sDate 日期
		 * @param sFormat 格式
		 * @return
		 */
		public static boolean isDate(String sDate, String sFormat) {
			if (sDate == null)
				return false;
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
				dateFormat.setLenient(false);
				dateFormat.parse(sDate);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * 当前年份
		 * @return
		 */
		public static int getYear(){
			GregorianCalendar gc = new GregorianCalendar();
			return gc.get(GregorianCalendar.YEAR);
		}
		
		/**
		 * 当前月份
		 * @return
		 */
		public static int getMonth(){
			GregorianCalendar gc = new GregorianCalendar();
			return gc.get(GregorianCalendar.MONTH)+1;
		}
		
		/**
		 * 当前日
		 * @return
		 */
		public static int getDayOfMonth(){
			GregorianCalendar gc = new GregorianCalendar();
			return gc.get(GregorianCalendar.DAY_OF_MONTH);
		}

		/**
		 * 返回当前是一周的第几天，周日为第一天
		 * @return
		 */
		public static int getDayOfWeek(){
			GregorianCalendar gc = new GregorianCalendar();
			return gc.get(GregorianCalendar.DAY_OF_WEEK);
		}

		
		/**
		 * 格式化日期
		 * 
		 * @param date
		 * @return
		 */
		public static String formatDate(Date date, String formater) {
			try
			{
				SimpleDateFormat format = new SimpleDateFormat(formater);
				return format.format(date);
			}catch(Exception e){
				System.out.println("Format error :" + e.getMessage());
				return "";
			}
		}
		/**
		 * 格式化日期
		 * 
		 * @param date
		 * @return
		 */
		public static String formatDate(String date, String formater) {
			try
			{
				SimpleDateFormat format = new SimpleDateFormat(formater);
				Date fd = format.parse(date);
				return format.format(fd);
			}catch(Exception e){
				System.out.println("Format error :" + e.getMessage());
				return date;
			}
		}

		/**
		 * 返回格式化的当前时间，格式为yyyy-MM-dd HH:mm:ss
		 * 
		 * @return
		 */
		public static String getCurDateTime() {
			String formater = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date myDate = new Date();
			return format.format(myDate);
		}

		/**
		 * 返回上个月1号和本月1号的格式化的日期对
		 * 
		 * @param formater
		 *            日期格式："yyyy-M-d"或"yyyy-MM-dd"
		 * @return
		 */
		public static String[] getDatesRangeOfLastMonth(String formater) {
			String DateRang[] = new String[2];
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date myDate = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(myDate);
			calendar.set(calendar.get(GregorianCalendar.YEAR), calendar
					.get(GregorianCalendar.MONTH), 1);
			Date cDate = calendar.getTime();
			DateRang[1] = format.format(cDate);
			calendar.add(GregorianCalendar.MONTH, -1);
			cDate = calendar.getTime();
			DateRang[0] = format.format(cDate);
			return DateRang;
		}

		/**
		 * 返回startDate之后numberOfDays天的日期
		 * 
		 * @param startDate
		 * @param numberOfDays
		 * @return exception: return ""
		 */
		public static String getEndDate(java.sql.Date startDate, int numberOfDays) {
			try {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(startDate);
				calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
				Date endDate = calendar.getTime();
				return endDate.toString();
			} catch (Exception e) {
				System.out.println("Error:TimeUtil.getEndDate()," + e.getMessage());
				return "";
			}
		}

		
}
