package com.wechat.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wechat.commons.CommonProperties;
import com.wechat.core.model.EventNotice;
import com.wechat.core.model.SubscribeInfo;
import com.wechat.redis.RedisService;
import com.wechat.service.SubscribeInfoService;
import com.wechat.service.UserService;
import com.wechat.util.ConvertTimeUtil;
import com.wechat.util.EmojiUtil;
import com.wechat.util.HttpClientNewUtil;
import com.wechat.util.HttpClientUtil;
import com.wechat.util.ImageHelper;
import com.wechat.util.ImageUtils;
import com.wechat.util.WeChatApiUtil;
import com.wechat.util.XmlUtil;

/**
 * 用户关注或者取消以及在公众号进行其操作时，公众号推送消息后的一些操作
 * @author Administrator
 */
@RestController
@RequestMapping("/notice")
public class EventNoticeController {
	@Autowired
	private SubscribeInfoService subscribeInfoService;
	@Autowired
	private CommonProperties commonProperties;
	@Autowired
	private RedisService<String> redisService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/event")
	public void enventNotice(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String returnMsg = "success";
		//弃用(回调地址验签时使用)
//		String signature = request.getParameter("signature");
//		String nonce = request.getParameter("nonce");
//		String echostr = request.getParameter("echostr");
//		String timestamp = request.getParameter("timestamp");
//		
//		String currentSignature = Sha1Util.checkSHA1("39d70d1d11dc472dbe0f47c21e6f39f6", timestamp, nonce);
//		if(currentSignature.equalsIgnoreCase(signature)){
//			response.getWriter().write(echostr);
//			return ;
//		}
//		response.getWriter().write("验签失败");
//		return;
		
		//读取xml
		BufferedReader br = request.getReader();
		String str = "";
		String requestWholeStr = "";
		while((str = br.readLine()) != null){
			requestWholeStr += str;
		}
		if(StringUtils.isBlank(requestWholeStr)){
			response.getWriter().write("fail");
			return;
		}
		System.out.println("===============requestWholeStr=" + requestWholeStr);
		//转换xml
		JSONObject xmljson = XmlUtil.convertStringXmlToJson(requestWholeStr);
		EventNotice eventNotice = new Gson().fromJson(xmljson.toString(), EventNotice.class);
		String msgType = eventNotice.getMsgType();//消息类型
		String toUserName = eventNotice.getToUserName();//公众号id
		String fromUserName = eventNotice.getFromUserName();//即用户的open_id
		String eventKey = (null == eventNotice.getEventKey()) ? "":eventNotice.getEventKey().replace("qrscene_", "");//即创建二维码时的二维码scene_id,直接搜索关注或者关注官方二维码时此值为空
		String event = eventNotice.getEvent();
		System.out.println("===============event=" + event);
		
		if("subscribe".equals(event)){//关注
//			String responseMsg = this.getResponseMsg(fromUserName,toUserName);
//			response.getWriter().write(responseMsg);//微信5秒内必须收到回复，故先回复再处理
			
			//1.创建或更新关注表信息
			List<SubscribeInfo> subscribeInfoList = subscribeInfoService.querySubscribeInfo(eventNotice);
			if(null == subscribeInfoList || subscribeInfoList.isEmpty()){//首次关注
				this.createSubscribeInfo(fromUserName,eventKey);
			}else{//非首次关注直接更新状态
				SubscribeInfo waitUpdateSubscribeInfo = subscribeInfoList.get(0);
				waitUpdateSubscribeInfo.setStatus(1);
				waitUpdateSubscribeInfo.setUpdateTime(ConvertTimeUtil.getNowTimestamp());
				subscribeInfoService.update(waitUpdateSubscribeInfo);
			}
			
			//2.判定用户海报是否过期，如果过期则生成新海报
			SubscribeInfo subscribeInfo = this.judgePosterOverOrNot(fromUserName);
			
			//3.回复客服文字消息
			String accessToken = this.getAccessToken();
			String errcode = this.sendCustomerServiceMsg(fromUserName,accessToken);
			
			//4.文字发送成功之后发送海报
			if("0".equals(errcode)){
				StringBuffer sb = new StringBuffer();
				sb.append("<xml>");  
				sb.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");  
				sb.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");  
				sb.append("<CreateTime>" + System.currentTimeMillis() + "</CreateTime>");  
				sb.append("<MsgType><![CDATA[image]]></MsgType>");  
				sb.append("<Image><MediaId><![CDATA[" + subscribeInfo.getMediaId() + "]]></MediaId></Image>");  
				sb.append("</xml>");
				response.getWriter().write(sb.toString());
			}
			
			//5.实时计算用户推荐邀请人数
			if(StringUtils.isNotBlank(eventKey)){//为空则说明用户是通过官方渠道关注的二维码
				int inviteSubscribeNum = subscribeInfoService.countInviteSubscribeNum(eventKey);
				if(inviteSubscribeNum >= 5){
					userService.updateUserVip(eventKey);
				}
				//发送模板消息通知
				this.sendNoticeMsgToInviter(eventKey,inviteSubscribeNum);
			}
			return;
		}else if("unsubscribe".equals(event)){//取消关注
			List<SubscribeInfo> subscribeInfoList = subscribeInfoService.querySubscribeInfo(eventNotice);
			if(null != subscribeInfoList && !subscribeInfoList.isEmpty()){
				SubscribeInfo waitUpdateSubscribeInfo = subscribeInfoList.get(0);
				waitUpdateSubscribeInfo.setStatus(0);
				waitUpdateSubscribeInfo.setUpdateTime(ConvertTimeUtil.getNowTimestamp());
				subscribeInfoService.update(waitUpdateSubscribeInfo);
			}
			response.getWriter().write(returnMsg);
			return;
		}else if("click".equals(event)){//点击菜单，读呗目前暂时只有一个菜单，故没有任何逻辑，直接回复成功即可
			response.getWriter().write(returnMsg);
			return;
		}
		
		if("text".equals(msgType)){//客户回复文本消息
			String content = xmljson.getString("Content");
			StringBuffer sb = new StringBuffer();
			if(content.contains("畅读卡")){//发送图片
				SubscribeInfo subscribeInfo = this.judgePosterOverOrNot(fromUserName);
				//回复图片消息
				sb.append("<xml>");  
				sb.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");  
				sb.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");  
				sb.append("<CreateTime>" + System.currentTimeMillis() + "</CreateTime>");  
				sb.append("<MsgType><![CDATA[image]]></MsgType>");  
				sb.append("<Image><MediaId><![CDATA[" + subscribeInfo.getMediaId() + "]]></MediaId></Image>");  
				sb.append("</xml>");
				response.getWriter().write(sb.toString());
			}
	        return;
		}
	}
	
	/**
	 * 发送客服消息
	 * @param openid
	 * @param accessToken
	 * @return errcode
	 * @throws Exception
	 */
	private String sendCustomerServiceMsg(String openid, String accessToken) throws Exception {
		String sendCustomerServiceMsgUrl = MessageFormat.format(commonProperties.getSendCustomerServiceMsg(),accessToken);//发送客服消息url
		
		System.out.println("===sendCustomerServiceMsgUrl==" + sendCustomerServiceMsgUrl);
		String content = "“我这辈子遇到的聪明人没有一个不是每天读书的，没有！一个都没有！”\n"
				+ "——理查.芒格（巴菲特合伙人）\n\n"
				+ "读呗APP，专注高效阅读，让你每年多读100本书！\n"
				+ "10000+本精选好书；1000+本浓缩书籍；500+份主题书单；\n\n"
				+ "回复“畅读卡”获取专属海报，分享并邀请5位好友扫码关注读呗公众号，即可获得价值198元畅读年卡！\n"
				+ "点击链接“http://dwz.cn/6R2jcn”或者菜单下载APP，开启畅读！";
		
		JSONObject json = new JSONObject();
		json.put("content", content);//支持发送小程序
		
    	JSONObject requestJson = new JSONObject();
    	requestJson.put("touser", openid);
    	requestJson.put("msgtype", "text");
    	requestJson.put("text", json.toString());
    	
    	String result = HttpClientNewUtil.httpPostWithJson(requestJson,sendCustomerServiceMsgUrl);
		JSONObject returnJson = JSONObject.fromObject(result);
		System.out.println("=================returnJson_sendCustomerServiceMsg=" + returnJson.toString());
		String errcode = returnJson.getString("errcode");
		return errcode;
		
	}

	/**
	 * 获取回复信息
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
//	private String getResponseMsg(String fromUserName, String toUserName) {
//		StringBuffer sb = new StringBuffer();
//		//长连接：http://a.app.qq.com/o/simple.jsp?pkgname=com.hxjf.dubei
//		//短连接：http://dwz.cn/6R2jcn
//		String content = "“我这辈子遇到的聪明人没有一个不是每天读书的，没有！一个都没有！”\n"
//				+ "——理查.芒格（巴菲特合伙人）\n\n"
//				+ "读呗APP，专注高效阅读，让你每年多读100本书！\n"
//				+ "10000+本精选好书；1000+本浓缩书籍；500+份主题书单；\n\n"
//				+ "回复“畅读卡”获取专属海报，分享并邀请5位好友扫码关注读呗公众号，即可获得价值198元畅读年卡！\n"
//				+ "点击链接“http://dwz.cn/6R2jcn”下载APP，开启畅读！";
//		sb.append("<xml>");  
//		sb.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");  
//		sb.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");  
//		sb.append("<CreateTime>" + System.currentTimeMillis() + "</CreateTime>");  
//		sb.append("<MsgType><![CDATA[text]]></MsgType>");  
//		sb.append("<Content><![CDATA[" + content + "]]></Content>");  
//		sb.append("</xml>"); 
//		return sb.toString();
//	}

	/**
	 * 创建关注信息
	 * @param fromUserName - openId
	 * @param eventKey - recommendId
	 * @throws Exception 
	 */
	private SubscribeInfo createSubscribeInfo(String fromUserName, String eventKey) throws Exception {
		SubscribeInfo subscribeInfo = new SubscribeInfo();
		subscribeInfo.setOpenId(fromUserName);
		subscribeInfo.setStatus(1);
		subscribeInfo.setRecommendId(eventKey);//如果为空，则说明不是扫描带参数的二维码关注的
		subscribeInfo.setCreateTime(ConvertTimeUtil.getNowTimestamp());
		
		String accessToken = this.getAccessToken();
//		String accessToken = "PBqFyn_E7qycuz9lNavC5E5f4dvfwzz8xBlacZ3uuudWqYEO_uJNegB8GdVmYryU7f3d32QK-QAfywUEiXAtbtDYfsbbS8W8as5nkGIhwszPPAH16UYw5-wrfDnwpyZcNWPeAHAJSL";
		
		String wechatUserInfoUrl = MessageFormat.format(commonProperties.getWechatUserInfoUrl(),accessToken,fromUserName);
		Map<String,String> map = this.getWechatUserInfo(wechatUserInfoUrl,accessToken,fromUserName);
		String unionid = map.get("unionid");
		String nickName = map.get("nickname");
//		String unionid = "oLhVP07d5oj9b1-Q6iOvaXZ8IUGI";
		System.out.println("=================unionid=" + unionid);
		subscribeInfo.setUnionid(unionid);
		subscribeInfo.setNickName(nickName);
		
		//上传素材并生成素材id
		String mediaId = this.getMediaIdInfo(unionid, accessToken);
		subscribeInfo.setMediaId(mediaId);
		subscribeInfo.setMediaCreateTime(ConvertTimeUtil.getNowTimestamp());
		
		subscribeInfoService.create(subscribeInfo);
		return subscribeInfo;
	}

	/**
	 * 判断用户生成的海报是否过期，如果过期则重新生成
	 * @param openId
	 * @throws Exception 
	 */
	private SubscribeInfo judgePosterOverOrNot(String openId) throws Exception {
		SubscribeInfo subscribeInfo = subscribeInfoService.queryNewOneSubscribe(openId);
		if(null != subscribeInfo){
			if(StringUtils.isNotBlank(subscribeInfo.getMediaId())){
				Timestamp mediaCreateTime = subscribeInfo.getMediaCreateTime();
				Timestamp mediaOverTime = ConvertTimeUtil.addDate(mediaCreateTime,3);//3天时间过期
				if(mediaOverTime.compareTo(ConvertTimeUtil.getNowTimestamp()) <= 0){//过期后重新生成一张海报
					String accessToken = this.getAccessToken();
					String wechatUserInfoUrl = MessageFormat.format(commonProperties.getWechatUserInfoUrl(),accessToken,openId);
					Map<String,String> map = this.getWechatUserInfo(wechatUserInfoUrl,accessToken,openId);
					String unionid = map.get("unionid");
					subscribeInfo.setUnionid(unionid);
					subscribeInfo.setUnionid(unionid);
					subscribeInfo.setNickName(map.get("nickname"));
					
					//上传素材并生成素材id
					String mediaId = this.getMediaIdInfo(unionid, accessToken);
					subscribeInfo.setMediaId(mediaId);
					subscribeInfo.setMediaCreateTime(ConvertTimeUtil.getNowTimestamp());
					subscribeInfoService.update(subscribeInfo);
				}
				System.out.println("==============media_id=" + subscribeInfo.getMediaId());
			}else{
				String accessToken = this.getAccessToken();
				String mediaId = this.getMediaIdInfo(openId, accessToken);
				subscribeInfo.setMediaId(mediaId);
				subscribeInfo.setMediaCreateTime(ConvertTimeUtil.getNowTimestamp());
				subscribeInfoService.update(subscribeInfo);
			}
		}else{
			return this.createSubscribeInfo(openId,"");//此处一般是老用户,evenKey直接写死为空
		}
		return subscribeInfo;
	}

	/**
	 * 发送通知消息至邀请人
	 * @param unionid - 微信唯一id
	 * @param inviteSubscribeNum - 已邀请人数
	 * @throws Exception 
	 */
	private void sendNoticeMsgToInviter(String unionid, int inviteSubscribeNum) throws Exception {
		SubscribeInfo subscribeInfo = subscribeInfoService.queryByUnionid(unionid);
		if(null != subscribeInfo){
			JSONObject requestJson = new JSONObject();
			requestJson.put("touser",subscribeInfo.getOpenId());//接收者openid
			requestJson.put("template_id","mY3rD-Ej1FUHpcWo2VI2bWRnQPyyFgtarh4H9Jnhais");//模板ID
			
			JSONObject dataJson = new JSONObject();
			
			JSONObject firstJson = new JSONObject();
			firstJson.put("value","邀请好友提醒\n您已邀请1名好友");
			firstJson.put("color","#FF6347");
			dataJson.put("first",firstJson);
			
			JSONObject keyword1Json = new JSONObject();
			keyword1Json.put("value","邀请好友5名即可畅享读呗畅读卡一年");
			keyword1Json.put("color","#173177");
			dataJson.put("keyword1",keyword1Json);
			
			JSONObject keyword2Json = new JSONObject();
			keyword2Json.put("value","当前已邀请好友" + inviteSubscribeNum + "名");
			keyword2Json.put("color","#173177");
			dataJson.put("keyword2",keyword2Json);
			
			JSONObject keyword3Json = new JSONObject();
			keyword3Json.put("value",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			keyword3Json.put("color","#173177");
			dataJson.put("keyword3",keyword3Json);
			
			String remark = "哇~好棒哦~继续努力，读呗畅读年卡唾手可得~";
			if(inviteSubscribeNum >= 5){
				remark = "哇~好棒哦~您已经完成邀请5名好友，获得了价值198元畅读年卡一张，请登录读呗APP查看畅读卡~";
			}
			JSONObject remarkJson = new JSONObject();
			remarkJson.put("value",remark);
			remarkJson.put("color","#FF6347");
			dataJson.put("remark",remarkJson);
			
			requestJson.put("data", dataJson);
			
			String accessToken = this.getAccessToken();
			String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
			
			String result = HttpClientNewUtil.httpPostWithJson(requestJson,url);
			JSONObject returnJson = JSONObject.fromObject(result);
			System.out.println("=================returnJson=" + returnJson.toString());
		}
	}

	/**
	 * 获取素材id信息
	 * @param unionid - 用户唯一标识
	 * @param accessToken 
	 * @param openId 
	 * @return
	 */
	private String getMediaIdInfo(String unionid, String accessToken) throws Exception {
		String ticket = this.getTicket(unionid,accessToken);
		//1.生成二维码
		String obtainTempQrCodeUrl = commonProperties.getObtainTempQrCodeUrl();
		obtainTempQrCodeUrl = obtainTempQrCodeUrl + "?ticket=" + URLEncoder.encode(ticket, "UTF-8");
		
		InputStream is = null;
		OutputStream os = null;
		String filePath = "/usr/local/dubei/images/QrCode/temp" + new SimpleDateFormat("/yyyyMM/").format(new Date());
		try {
			URL url = new URL(obtainTempQrCodeUrl);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5 * 1000);
			is = con.getInputStream();
			byte[] bs = new byte[1024];//1K的数据缓冲
			int len;//读取到的数据长度
			
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			filePath = filePath + unionid + ".png";
			os = new FileOutputStream(filePath);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 try {
				if(null != is){
					is.close();
				}
				if(null != os){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//2.压缩二维码到指定大小
		String reduceQrCodePath = "/usr/local/dubei/images/QrCode/reduce/" + unionid + ".png";
		ImageHelper.scaleImageWithParams(filePath, reduceQrCodePath, 190, 190, false, "png");
		//3.二维码贴至海报
		String haiBaoPath = "/usr/local/dubei/images/QrCode/hb/" + unionid + ".png";
        ImageUtils newImageUtils = new ImageUtils();
        // 构建叠加层
        BufferedImage buffImg = ImageUtils.watermark(new File("/usr/local/dubei/images/QrCode/original_hb.png"), new File(reduceQrCodePath), 280, 950, 1.0f);
        // 输出贴图后图片
        newImageUtils.generateWaterFile(buffImg, haiBaoPath);
		//4.海报上传至微信
		JSONObject o = WeChatApiUtil.uploadMedia(new File(haiBaoPath), accessToken, "image");
		System.out.println(o.toString());
		String mediaId = o.getString("media_id");
		return mediaId;
	}
	
	
	/**
	 * 获取ticket，用户生成二维码
	 * @param unionid
	 * @param accessToken
	 * @return
	 */
	private String getTicket(String unionid, String accessToken) {
		//1.获取ticket
		String generateTempQrCodeUrl = commonProperties.getGenerateTempQrCodeUrl();
		JSONObject json3 = new JSONObject();
		json3.put("expire_seconds", "2592000");
    	//二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
		json3.put("action_name", "QR_STR_SCENE");
    	
    	JSONObject json = new JSONObject();
    	json.put("scene_str", unionid);
    	
    	JSONObject json2 = new JSONObject();
    	json2.put("scene", JSON.toJSONString(json));
    	generateTempQrCodeUrl = generateTempQrCodeUrl+"?access_token=" + accessToken;
    	json3.put("action_info", JSON.toJSONString(json2));
		String result = HttpClientNewUtil.httpPostWithJson(json3,generateTempQrCodeUrl);
		JSONObject returnJson = JSONObject.fromObject(result);
		System.out.println("=================ticket_return=" + returnJson.toString());
		String ticket = returnJson.getString("ticket");
		return ticket;
	}

	/**
	 * 获取用户基本 信息,(暂时只有：unionid和nickname)
	 * @param wechatUserInfoUrl - 获取用户信息链接
	 * @param accessToken 
	 * @param openId 
	 * @return
	 */
	private Map<String,String> getWechatUserInfo(String wechatUserInfoUrl, String accessToken, String openId) throws Exception {
		Map<String,String> map = new HashMap<>();
		map.put("access_token", accessToken);
		map.put("openid", openId);
		map.put("lang", "zh_CN");
		String result = HttpClientUtil.sendSSLGetRequest(wechatUserInfoUrl,map);
		System.out.println("=============result=" + result);
		String unionid = JSONObject.fromObject(result).getString("unionid");
		String nickname = JSONObject.fromObject(result).getString("nickname");
		
		Map<String,String> returnMap = new HashMap<>();
		returnMap.put("unionid", unionid);
		returnMap.put("nickname", EmojiUtil.filterEmoji(nickname));
		return returnMap;
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
			redisService.set("wechat_access_token", accessToken);
			redisService.expireSeconds("wechat_access_token", 7000);
		}
		System.out.println("==============access_token=" + accessToken);
		return accessToken;
	}
	

	@RequestMapping(value = "/test")
	public void test() throws Exception{
		String unionid = "oLhVP07d5oj9b1-Q6iOvaXZ8IUGI";
		//发送模板消息通知
		SubscribeInfo subscribeInfo = subscribeInfoService.queryByUnionid(unionid);
		if(null != subscribeInfo){
			JSONObject requestJson = new JSONObject();
			requestJson.put("touser",subscribeInfo.getOpenId());//接收者openid
			requestJson.put("template_id","mY3rD-Ej1FUHpcWo2VI2bWRnQPyyFgtarh4H9Jnhais");//模板ID
			
			JSONObject dataJson = new JSONObject();
			
			JSONObject firstJson = new JSONObject();
			firstJson.put("value","邀请好友提醒\n您已邀请1名好友");
			firstJson.put("color","#FF6347");
			dataJson.put("first",firstJson);
			
			JSONObject keyword1Json = new JSONObject();
			keyword1Json.put("value","邀请好友5名即可畅享读呗畅读卡一年");
			keyword1Json.put("color","#173177");
			dataJson.put("keyword1",keyword1Json);
			
			JSONObject keyword2Json = new JSONObject();
			keyword2Json.put("value","当前已邀请好友" + 3 + "名");
			keyword2Json.put("color","#173177");
			dataJson.put("keyword2",keyword2Json);
			
			JSONObject keyword3Json = new JSONObject();
			keyword3Json.put("value",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			keyword3Json.put("color","#173177");
			dataJson.put("keyword3",keyword3Json);
			
			requestJson.put("data", dataJson);
			
			String accessToken = "vpdCzJdbkTJOCafwjZmSZttxgXk6SnUF2H9YpxyHXvMbcTE7DcjLavbtcf55xLC2aX4Pn_oef0P-MTJrfH5k6TDiWfysggH8sr-A1B61HbFvZrZe3YLGz_vqVlO4mbGFVBJcAEAAGO";
			String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
			
			String result = HttpClientNewUtil.httpPostWithJson(requestJson,url);
			JSONObject returnJson = JSONObject.fromObject(result);
			System.out.println("=================returnJson=" + returnJson.toString());
			
		}
		
	}
	
}
