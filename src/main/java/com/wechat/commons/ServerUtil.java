package com.wechat.commons;

import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * 服务器工具类
 */
public class ServerUtil {
	/**
	 * 判断一个字符串是否是纯数字
	 * @param str 带判断字符串
	 * @return true/false
	 */
	public static boolean isNum(String str){
		String reg ="\\d+\\.{0,1}\\d*";
		boolean isDigits = str.matches(reg);
		return isDigits;
	}
	
	/**
	 * 判断特殊字符
	 * @param str 
	 * @return true-含特殊字符 false-不含特殊字符
	 */
	public static boolean specialChar(String str){
		String specialChar = "[ \\~\\!\\/\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\=\\+\\\\\\|\\[\\{\\}\\]\\;\\:\\\'\\\"\\,\\<\\.\\>\\/\\?]";
		return Pattern.compile(specialChar).matcher(str).find();
	}
	
	/**
	 * @param numerval 
	 * @param numdefine 取几位数字 加","
	 * @return
	 */
	public String getNumberString(String numerval,int numdefine){
		//int numdefine=3;
		String maxvalue = String.valueOf(numerval);
		String minvalue="";
		int temp = maxvalue.length()%numdefine;
		if(0!=temp){
			maxvalue = maxvalue.substring(0, temp);
			minvalue =   String.valueOf(numerval).substring(temp,String.valueOf(numerval).length());
		}else{
			minvalue = maxvalue;
		}
		String resultval = "";
		int fornum = minvalue.length()/numdefine;
		for(int i=0;i<fornum;i++){
			resultval += minvalue.substring(i*numdefine, (i*numdefine)+numdefine)+",";
		}
		if(0!=temp){
			resultval = maxvalue+","+resultval;
		}
		resultval = resultval.endsWith(",")?resultval.substring(0, resultval.length()-1):resultval;
		//System.out.println(resultval);
		return resultval;
	}
	
	/**
	 * 获取客户端真实IP地址
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request){
		String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if(forwarded != null){
                    forwarded = forwarded.split(",")[0];
                }
                ip = forwarded;
            }
        }
		return ip;
	}
	
	/**
	 * 将list转为sql查询格式字符串:('1','2')
	 * @param list 
	 * @return 
	 */
	public static String listToSqlString(List<String> list){
		if(null == list || list.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer("(");
		for(String str:list){
			sb.append("'").append(str).append("'").append(",");
		}
		String s = sb.substring(0, sb.length()-1) + ")";
		return s;
	}
}
