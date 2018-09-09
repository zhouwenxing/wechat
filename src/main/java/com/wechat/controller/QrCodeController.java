package com.wechat.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wechat.commons.CommonProperties;
import com.wechat.redis.RedisService;
import com.wechat.util.HttpClientNewUtil;
import com.wechat.util.HttpClientUtil;

@RestController
@RequestMapping("/qrCode")
public class QrCodeController {
	@Autowired
	private RedisService<String> redisService;
	@Autowired
	private CommonProperties commonProperties;
	
	@RequestMapping(value = "/temp/get")
	public void getTempQrCode(HttpServletRequest request,String userId) throws Exception {
		//1.获取access_token
		String accessToken = redisService.get("wechat_access_token");; 
		if(StringUtils.isEmpty(accessToken)){
	    	String accessTokenUrl = commonProperties.getAccessTokenUrl();
	    	String appId = commonProperties.getAppId();
	    	String appSecret = commonProperties.getAppSecret();
	    	Map<String,String> map1 = new HashMap<String, String>();
	    	map1.put("grant_type", "client_credential");
	    	map1.put("appid", appId);
	    	map1.put("secret", appSecret);
			String result = HttpClientUtil.sendSSLGetRequest(accessTokenUrl,map1);
			accessToken = JSONObject.fromObject(result).getString("access_token");
			redisService.set("wechat_access_token", accessToken);
			redisService.expireSeconds("wechat_access_token", 7000);
		}
		System.out.println("==============accessToken=" + accessToken);
		
		String url = "https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=" + accessToken;
		JSONObject requestJson = new JSONObject();
		requestJson.put("is_add_friend_reply_open",1);//
		requestJson.put("is_autoreply_open",0);//模板ID
		
		String content = "“我这辈子遇到的聪明人没有一个不是每天读书的，没有！一个都没有！”\n"
				+ "——理查.芒格（巴菲特合伙人）\n\n"
				+ "读呗APP，专注高效阅读，让你每年多读100本书！\n"
				+ "10000+本精选好书；1000+本浓缩书籍；500+份主题书单；\n\n"
				+ "回复“畅读卡”获取专属海报，分享并邀请5位好友扫码关注读呗公众号，即可获得价值198元畅读年卡！\n"
				+ "点击链接“http://dwz.cn/6R2jcn”下载APP-APP，开启畅读！";
		
		JSONObject firstJson = new JSONObject();
		firstJson.put("type","text");
		firstJson.put("content",content);
		requestJson.put("add_friend_autoreply_info",firstJson);
		
		String result = HttpClientNewUtil.httpPostWithJson(requestJson,url);
		JSONObject returnJson = JSONObject.fromObject(result);
		System.out.println("=================returnJson=" + returnJson.toString());
		
	}
	
	@Test
	public void test() throws Exception {
		String url = "http://icode.renren.com/getcode.do";
		Map<String,String> map = new HashMap<String, String>();
//		map.put("rnd", System.currentTimeMillis() + "");
		map.put("opt", "1");
		map.put("mn", "13530798206");
		map.put("icode_type", "web_reg");
		map.put("_rtk", "b14633e7");
		
		
		String result = HttpClientUtil.sendSSLGetRequest(url,map);
		System.out.println(result);
	}
}
