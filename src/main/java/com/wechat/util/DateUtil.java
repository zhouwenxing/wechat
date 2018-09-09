package com.wechat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类
 */
public class DateUtil {
	
	
	/**
	 * 比较两个日期大小
	 * @param dateString1 - 日期字符串1
	 * @param dateString2 - 日期字符串2
	 * @return -1,0,N 分表表示dateString1<dateString2,dateString1=dateString2,dateString1-dateString2=N
	 * @throws Exception
	 */
	public static int compareDate(String dateString1,String dateString2) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(dateString1);//日期1
			date2 = sdf.parse(dateString2);//日期2
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar1 = Calendar.getInstance();//日历1
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();//日历2
		calendar2.setTime(date2);
		
		
		
		return 0;
		
	}

	/**
	 * 给日期新增天数
	 * @param date - 日期
	 * @param addDays - 新增天数
	 */
	public static java.sql.Date addDays(java.sql.Date date, int addDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, addDays);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

}
