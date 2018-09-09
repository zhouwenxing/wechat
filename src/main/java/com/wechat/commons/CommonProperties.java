package com.wechat.commons;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(locations = "classpath:conf/prop.yml")
public class CommonProperties {
	/**
	 * 图片相关
	 */
	private String imageUploadPath;//图片上传相对路径
	private String imagePathPrefix;//图片上传根路径
	private String imageSize;//图片大小限制
	private String imageResizeWidth;//缩略图宽
	private String imageResizeHeight;//缩略图高 
	private String imageUrlPrefix;//用户头像前缀
	
	
	/**
	 * 微信相关
	 */
	private String appId;
	private String appSecret;
	private String accessTokenUrl;//获取access_token url
	private String generateTempQrCodeUrl;//生成临时二维码url
	private String obtainTempQrCodeUrl;//获取临时二维码url
	private String wechatUserInfoUrl;//获取微信用户信息url
	
	private String addCustomerServiceUrl;//添加客服url
	private String sendCustomerServiceMsg;//发送客服消息
	
	private String createMenuUrl;//添加菜单接口
	
	public String getImageUploadPath() {
		return imageUploadPath;
	}
	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}
	public String getImagePathPrefix() {
		return imagePathPrefix;
	}
	public void setImagePathPrefix(String imagePathPrefix) {
		this.imagePathPrefix = imagePathPrefix;
	}
	public String getImageSize() {
		return imageSize;
	}
	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}
	public String getImageResizeWidth() {
		return imageResizeWidth;
	}
	public void setImageResizeWidth(String imageResizeWidth) {
		this.imageResizeWidth = imageResizeWidth;
	}
	public String getImageResizeHeight() {
		return imageResizeHeight;
	}
	public void setImageResizeHeight(String imageResizeHeight) {
		this.imageResizeHeight = imageResizeHeight;
	}
	public String getImageUrlPrefix() {
		return imageUrlPrefix;
	}
	public void setImageUrlPrefix(String imageUrlPrefix) {
		this.imageUrlPrefix = imageUrlPrefix;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}
	public String getGenerateTempQrCodeUrl() {
		return generateTempQrCodeUrl;
	}
	public void setGenerateTempQrCodeUrl(String generateTempQrCodeUrl) {
		this.generateTempQrCodeUrl = generateTempQrCodeUrl;
	}
	public String getObtainTempQrCodeUrl() {
		return obtainTempQrCodeUrl;
	}
	public void setObtainTempQrCodeUrl(String obtainTempQrCodeUrl) {
		this.obtainTempQrCodeUrl = obtainTempQrCodeUrl;
	}
	public String getWechatUserInfoUrl() {
		return wechatUserInfoUrl;
	}
	public void setWechatUserInfoUrl(String wechatUserInfoUrl) {
		this.wechatUserInfoUrl = wechatUserInfoUrl;
	}
	
	
	public String getAddCustomerServiceUrl() {
		return addCustomerServiceUrl;
	}
	public void setAddCustomerServiceUrl(String addCustomerServiceUrl) {
		this.addCustomerServiceUrl = addCustomerServiceUrl;
	}
	public String getSendCustomerServiceMsg() {
		return sendCustomerServiceMsg;
	}
	public void setSendCustomerServiceMsg(String sendCustomerServiceMsg) {
		this.sendCustomerServiceMsg = sendCustomerServiceMsg;
	}
	
	public String getCreateMenuUrl() {
		return createMenuUrl;
	}
	public void setCreateMenuUrl(String createMenuUrl) {
		this.createMenuUrl = createMenuUrl;
	}
	
	
}
