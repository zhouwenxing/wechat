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
import com.wechat.commons.CommonProperties;
import com.wechat.redis.RedisService;
import com.wechat.util.HttpClientUtil;

/**
 * 客服相关接口
 * @author Administrator
 */

@RestController
@RequestMapping("/customer/service")
public class CustomerServiceController {
	@Autowired
	private CommonProperties commonProperties;
	@Autowired
	private RedisService<String> redisService;
	
	
	
	@RequestMapping(value = "/account/add")
	public void addCustomerServiceAccount(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.获取access_token
		String accessToken = this.getAccessToken(); 
		System.out.println("==============accessToken=" + accessToken);
		
		String addCustomerServiceUrl = MessageFormat.format(commonProperties.getAddCustomerServiceUrl(),accessToken);//添加客服url
		Map<String,String> map = new HashMap<String, String>();
    	map.put("kf_account", "dubei_service_01");
    	map.put("nickname", "读呗客服01");
    	map.put("password", "dubei2018");
		String result = HttpClientUtil.sendSSLGetRequest(addCustomerServiceUrl,map);
		JSONObject returnJson = JSONObject.fromObject(result);
		System.out.println("=================returnJson=" + returnJson.toString());
		
		String errmsg = returnJson.getString("errmsg");
		response.getWriter().write(errmsg);
		return;
	}
	
	
	@RequestMapping(value = "/msg/send")
	public void sendCustomerServiceMsg(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.获取access_token
		String accessToken = this.getAccessToken(); 
		System.out.println("==============accessToken=" + accessToken);
		
		String sendCustomerServiceMsgUrl = MessageFormat.format(commonProperties.getSendCustomerServiceMsg(),accessToken);//发送客服消息url
		
		JSONObject json = new JSONObject();
		json.put("content", "哈哈，发消息咯");//支持发送小程序
		Map<String,String> map = new HashMap<String, String>();
		
    	map.put("touser", "openid");
    	map.put("msgtype", "text");
    	map.put("text", json.toString());
		String result = HttpClientUtil.sendSSLGetRequest(sendCustomerServiceMsgUrl,map);
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
