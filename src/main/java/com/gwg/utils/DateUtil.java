package com.gwg.utils;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class DateUtil {
	private DateUtil() {
	}

	public static final int DAY = 1;
	public static final int HOUR = 2;
	public static final int MINITE = 3;
	public static final int SECOND = 4;
	public static final int MONTH = 5;

	public static final int DAY_SECOND = 24 * 60 * 60;

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final DateFormat date_format = new SimpleDateFormat("yyyy/M/d", Locale.ENGLISH);
	private static final DateFormat time_format = new SimpleDateFormat("yyyy/M/d hh:mm:ss", Locale.ENGLISH);

	public static final String TIME_FORMAT_1 = "yyyyMMddHHmmss";

	public static String defFormatDate(Date date) {
		return date_format.format(date);
	}

	public static String defFormatTime(Date date) {
		return time_format.format(date);
	}

	public static String dateFormat(Date date) {
		return dateFormat(date, DEFAULT_DATE_FORMAT);
	}

	public static String dateFormat(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String str = sdf.format(date);
		return str;
	}
	public static String DF_yyyyMMddHHmmss() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(Calendar.getInstance().getTime());
		return str;
	}

	/**
	 * 
	 * @Title: currentDate
	 * @Description: 获得当前时间
	 * @param @return 参数
	 * @return Date 返回类型
	 * @throws
	 */
	public static Date currentDate() {
		return new Date();
	}

	public static Date dateFormat(String dateStr) {
		return dateFormat(dateStr, DEFAULT_DATE_FORMAT);
	}

	public static Date dateFormat(String dateStr, String format) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}

		return date;
	}

	/**
	 * 获取延迟时间
	 * 
	 * @return
	 */
	public static long getDelaytime(int delayhour) {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millsecond = cal.get(Calendar.MILLISECOND);

		long delta = (delayhour - hour) * 3600000 - minute * 60000 - second * 1000 - millsecond;
		if (delta < 0) {
			delta += 86400000;
		}
		return delta;
	}

	/**
	 * 获取延迟时间
	 * 
	 * @return
	 */
	public static long getWeekDelaytime(int delayhour) {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millsecond = cal.get(Calendar.MILLISECOND);

		long delta = (delayhour - hour) * 3600000 - minute * 60000 - second * 1000 - millsecond;
		int weekDay = getWeekDay();

		int day = 7 - weekDay + 1;

		if (delta < 0) {
			delta += 86400000 * day;
		}
		return delta;
	}

	/**
	 * 获取下次刷新时间
	 * 
	 * @return
	 */
	public static long getNextTime(int delayhour) {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millsecond = cal.get(Calendar.MILLISECOND);

		long delta = (delayhour - hour) * 3600000 - minute * 60000 - second * 1000 - millsecond;
		if (delta < 0) {
			delta += 86400000;
		}
		return System.currentTimeMillis() + delta;
	}

	// 每天4点的时候更新数据
	public static boolean isTheSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);

		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.DATE) == c2.get(Calendar.DATE));
	}

	public static boolean isToday(Date lastDate) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(lastDate);
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.DATE) == c2.get(Calendar.DATE));
	}

	public static boolean isYesterday(Date lastDate) {
		return isYesterday(lastDate, new Date());
	}

	public static boolean isYesterday(Date lastDate, Date date) {
		Calendar last = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		last.setTime(lastDate);
		now.setTime(date);
		last.add(Calendar.DAY_OF_MONTH, 1);
		return (last.get(Calendar.YEAR) == now.get(Calendar.YEAR)) && (last.get(Calendar.MONTH) == now.get(Calendar.MONTH))
				&& (last.get(Calendar.DATE) == now.get(Calendar.DATE));
	}

	public static Date getZeroTime() {
		return getBeginTime(0);
	}

	public static Date getZeroTime(Date date) {
		return getBeginTime(date, 0);
	}

	public static Date getBeginTime(int hour) {
		Calendar begin = Calendar.getInstance();

		begin.set(Calendar.HOUR_OF_DAY, hour);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		return begin.getTime();
	}

	public static Date getBeginTime(Date date, int hour) {
		Calendar begin = Calendar.getInstance();
		begin.setTime(date);
		begin.set(Calendar.HOUR_OF_DAY, hour);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		return begin.getTime();
	}

	public static Date getEndTime() {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.HOUR_OF_DAY, 23);
		begin.set(Calendar.MINUTE, 59);
		begin.set(Calendar.SECOND, 59);
		begin.set(Calendar.MILLISECOND, 999);
		return begin.getTime();
	}

	public static Date getEndTime(Date date) {
		Calendar begin = Calendar.getInstance();
		begin.setTime(date);
		begin.set(Calendar.HOUR_OF_DAY, 23);
		begin.set(Calendar.MINUTE, 59);
		begin.set(Calendar.SECOND, 59);
		begin.set(Calendar.MILLISECOND, 999);
		return begin.getTime();
	}

	public static boolean isExpired(Date last, int coolSecond) {
		if (null == last || coolSecond <= 0) {
			return true;
		}
		Date coolPassDate = addTime(SECOND, coolSecond, last);

		return coolPassDate.before(new Date());
	}

	public static boolean isExpired(Date last) {
		if (null == last) {
			return true;
		}
		Date now = new Date();

		return last.before(now);
	}

	public static Date addTime(int param, int add) {
		return addTime(param, add, new Date());
	}

	public static Date addTime(int param, int add, Date date) {
		if (null == date) {
			return null;
		}
		Calendar begin = Calendar.getInstance();
		begin.setTime(date);

		if (DAY == param)
			begin.add(Calendar.DATE, add);
		if (HOUR == param)
			begin.add(Calendar.HOUR_OF_DAY, add);
		if (MINITE == param)
			begin.add(Calendar.MINUTE, add);
		if (SECOND == param)
			begin.add(Calendar.SECOND, add);
		if (MONTH == param) {
			begin.add(Calendar.MONTH, add);
		}

		return begin.getTime();
	}

	public static long getSecondsToGoFromNow(Date fromDate) {
		if (null == fromDate) {
			return -1L;
		}
		Date now = new Date();
		if (now.before(fromDate)) {
			return 0L;
		}
		return (now.getTime() - fromDate.getTime()) / 1000L;
	}

	public static long getSecondsToGo(Date fromDate, Date toDate) {
		if ((null == fromDate) || (null == toDate)) {
			return -1L;
		}
		if (toDate.before(fromDate)) {
			return 0L;
		}
		return (toDate.getTime() - fromDate.getTime()) / 1000L;
	}

	public static int getSeconds() {
		return (int) (System.currentTimeMillis() / 1000L);
	}

	public static long getMillis() {
		return System.currentTimeMillis();
	}

	public static int getWeekDay() {
		Calendar cal = Calendar.getInstance();

		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week == 0)
			week = 7;
		return week;
	}

	public static int getMonthOfDay() {
		Calendar cal = Calendar.getInstance();

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHourOfDay() {
		Calendar cal = Calendar.getInstance();

		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static void main(String[] args) throws Exception{
		//System.out.println(DateUtil.addTime(DateUtil.MINITE, -7));


		//生成订单号
		Set<String> orderNoSet = new HashSet<>();//Set自动去重复
        for(int i = 1; i<=66; i++){
        	Thread.sleep(1000);
			String orderNo = DateUtil.dateFormat(new Date(), "YYYYMMddHHmmssSSSS");
			System.out.println(orderNo);
			orderNoSet.add(orderNo);
		}
		if(orderNoSet.size() == 66){
			System.out.println("生成订单号没有重复");
		}

		//MD5 批量生成
		/*String str ="47f5ab8b2e97e18e4fb9e57718eb82af10001web代付2018092617265602691537945576000";
		String expect = "1867e8122f967d50636e4cfe0f1ecd08";
		String encyptStr = MD5(str);
		System.out.println(org.apache.commons.lang.StringUtils.equalsIgnoreCase(expect, encyptStr));*/

	}

	/**
	 * MD5 16进制 和 32 进制加密
	 * @param sourceStr
	 * @return
	 */
	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
			System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}


}

