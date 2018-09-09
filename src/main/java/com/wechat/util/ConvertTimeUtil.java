package com.wechat.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 转换时间
 * 时间间隔的计算(以前的时间与现在的时间间隔，类似于新浪微博显示发表时间的方式)
 */
public class ConvertTimeUtil{

	/**
	 * 转化时间
	 * @param datatime - 时间字符串
	 * @return
	 * @throws Exception
	 */
	public static String exec(String datatime) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		ParsePosition pos = new ParsePosition(0);  
		Date date = (Date) sd.parse(datatime, pos);  
		if(date==null){
			try {
				Thread.sleep(50);
				date = new Date();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return exec(date.getTime());
	}
	
	public static String exec(long datatime) throws Exception {
		String interval = null;  
		//用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔  
				long time = new Date().getTime() - datatime;// 得出的时间间隔是毫秒  
				if(time/1000 < 10 && time/1000 >= 0) {  
					//如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒  
					interval ="刚刚";  
				}else if(time/1000 < 60 && time/1000 > 0) {  
					//如果时间间隔小于60秒则显示多少秒前  
					int se = (int) ((time%60000)/1000);  
					interval = se + "秒前";  
				}else if(time/60000 < 60 && time/60000 > 0) {  
					//如果时间间隔小于60分钟则显示多少分钟前  
					int m = (int) ((time%3600000)/60000);//得出的时间间隔的单位是分钟  
					interval = m + "分钟前";  
				}else if(time/3600000 < 24 && time/3600000 >= 0) {  
					//如果时间间隔小于24小时则显示多少小时前  
					int h = (int) (time/3600000);//得出的时间间隔的单位是小时  
					interval = h + "小时前";  
				}else if(time/3600000 < 72 && time/3600000 >= 24){//间隔时间大于1天小于3天
					interval = "1天前"; 
				} else if(time/3600000 < 168 && time/3600000 >= 72){//间隔时间大于3天小于7天
					interval = "3天前"; 
				}else if(time/3600000 < 720 && time/3600000 >= 168){//间隔时间大于7天小于30天
					interval = "一星期前"; 
				}else if(time/3600000 < 2160 && time/3600000 >= 720){//间隔时间大于30天小于90天
					interval = "一月前"; 
				}else if(time/3600000 < 8760 && time/3600000 >= 2160){//间隔时间大于90天且在一年内，显示月日
					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");  
					interval = sdf.format(datatime);  
				}else if(time/3600000 >= 8760){//间隔时间大于一年，显示年月日
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
					interval = sdf.format(datatime);   
				}
		        return interval;  
	}
	/**
	 * 获取系统时间
	 * @return dateString
	 */
	public static String getNowDate(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	/**
	 * 获得时间戳格式
	 * @return
	 */
	public static String getDateNum(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取当前Timestamp
	 * @return Timestamp
	 */
	public static Timestamp getNowTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 获取当前Date
	 * @return Date
	 */
	public static java.sql.Date getNowDate(){
		return new java.sql.Date(System.currentTimeMillis());
	}
	
	/**
	 * 添加时间
	 * @param time - 待相加日期 
	 * @param day-相加天数
	 */
	public static Timestamp addDate(Timestamp time, int dayNum){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历
		if(null != time){
			cal.setTime(time);
		}
		cal.add(Calendar.DAY_OF_MONTH, dayNum);//取当前日期的dayNum天
		return new Timestamp(cal.getTimeInMillis());
	}
	
	/**
	 * 计算日期差
	 * @param dateStr1
	 * @param dateStr2
	 * @return 相差天数
	 * @throws ParseException 
	 */
	public static int daysBetween(String dateStr1,String dateStr2) throws ParseException{    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Date date1 = sdf.parse(dateStr1);  
        Date date2 = sdf.parse(dateStr2);   
        Calendar cal = Calendar.getInstance();    
        cal.setTime(date1);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(date2);    
        long time2 = cal.getTimeInMillis();         
        long betweenDays=(time2-time1)/(1000*3600*24);  
        return Integer.parseInt(String.valueOf(betweenDays));           
    }    
	
	
}
