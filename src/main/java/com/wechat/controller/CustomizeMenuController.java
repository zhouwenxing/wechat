package com.wechat.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.wechat.commons.CommonProperties;
import com.wechat.redis.RedisService;
import com.wechat.util.HttpClientNewUtil;
import com.wechat.util.HttpClientUtil;

/**
 * 自定义菜单接口类
 * @author Administrator
 */
@RestController
@RequestMapping("/customize/menu")
public class CustomizeMenuController {
	@Autowired
	private CommonProperties commonProperties;
	@Autowired
	private RedisService<String> redisService;
	
	
	@RequestMapping(value = "/create")
	public void addCustomerServiceAccount(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.获取access_token
		String accessToken = this.getAccessToken(); 
		System.out.println("==============accessToken=" + accessToken);
		
		String createMenuUrl = MessageFormat.format(commonProperties.getCreateMenuUrl(),accessToken);//创建自定义菜单url
		
		JSONObject json1 = new JSONObject();
		json1.put("type", "view");
		json1.put("name", "下载app");
		json1.put("key", "down_load_dubei_app");
		json1.put("url", "http://dwz.cn/6R2jcn");
		
		JSONArray jsonArr = new JSONArray();
		jsonArr.add(json1);
    	
		JSONObject json = new JSONObject();
    	json.put("button", jsonArr);
    	
    	String result = HttpClientNewUtil.httpPostWithJson(json,createMenuUrl);
		JSONObject returnJson = JSONObject.fromObject(result);
		System.out.println("=================returnJson=" + returnJson.toString());
		
		String errcode = returnJson.getString("errcode");
		if("0".equals(errcode)){
			//......
		}
		response.getWriter().write(returnJson.getString("errmsg"));
		return;
	}
	
	@RequestMapping(value = "/delete")
	public void deleteCustomerServiceAccount(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.获取access_token
		String accessToken = this.getAccessToken(); 
		System.out.println("==============accessToken=" + accessToken);
		
		String createMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken;//删除自定义菜单url
		JSONObject json = new JSONObject();
    	
    	String result = HttpClientNewUtil.httpPostWithJson(json,createMenuUrl);
		JSONObject returnJson = JSONObject.fromObject(result);
		response.getWriter().write(returnJson.getString("errmsg"));
		return;
	}
	
	@RequestMapping(value = "/msg/send")
	public void sendCustomerServiceMsg(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.获取access_token
		String accessToken = this.getAccessToken(); 
		System.out.println("==============accessToken=" + accessToken);
		
		String sendCustomerServiceMsg = MessageFormat.format(commonProperties.getSendCustomerServiceMsg(),accessToken);//发送客服消息url
		
		JSONObject json = new JSONObject();
		json.put("content", "哈哈，发消息咯");//支持发送小程序
		Map<String,String> map = new HashMap<String, String>();
		
    	map.put("touser", "openid");
    	map.put("msgtype", "text");
    	map.put("text", json.toString());
		String result = HttpClientUtil.sendSSLGetRequest(sendCustomerServiceMsg,map);
		JSONObject returnJson = JSONObject.fromObject(result);
		System.out.println("=================returnJson=" + returnJson.toString());
		
		String errmsg = returnJson.getString("errmsg");
		response.getWriter().write(errmsg);
		return;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取access_token
	 * @throws Exception
	 */
	private String getAccessToken() throws Exception {
		String accessToken = redisService.get("wechat_access_token"); 
		if(StringUtils.isEmpty(accessToken)){
	    	String accessTokenUrl = commonProperties.getAccessTokenUrl();
	    	String appId = commonProperties.getAppId();
	    	String appSecret = commonProperties.getAppSecret();
	    	Map<String,String> map = new HashMap<String, String>();
	    	map.put("grant_type", "client_credential");
	    	map.put("appid", appId);
	    	map.put("secret", appSecret);
			String result = HttpClientUtil.sendSSLGetRequest(accessTokenUrl,map);
			accessToken = JSONObject.fromObject(result).getString("access_token");
			System.out.println("==============access_token=" + accessToken);
			redisService.set("wechat_access_token", accessToken);
			redisService.expireSeconds("wechat_access_token", 7000);
		}
		return accessToken;
	}
	
	

}
