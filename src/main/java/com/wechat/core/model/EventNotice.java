package com.wechat.core.model;

import java.io.Serializable;


public class EventNotice implements Serializable{
	private static final long serialVersionUID = -6203572833396252118L;
	
	private String ToUserName;//开发者微信号-即公众号原始id
	private String FromUserName;//发送方帐号（一个OpenID）
	private Integer CreateTime;//消息创建时间 （整型）
	private String MsgType;//消息类型，event
	private String Event;//事件类型:SCAN(浏览)/ENTER(进入会话)/LOCATION(地理位置)/subscribe(订阅)/unsubscribe(取消订阅)
	private String EventKey;//事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id,直接搜索关注或者关注官方二维码时此值为空
	private String Ticket;//二维码的ticket，可用来换取二维码图片
	private String Latitude;//地理位置纬度，事件类型为LOCATION的时存在
	private String Longitude;//地理位置经度，事件类型为LOCATION的时存在
	private String Precision;//地理位置精度，事件类型为LOCATION的时存在
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		this.ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String FromUserName) {
		this.FromUserName = FromUserName;
	}
	public Integer getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Integer CreateTime) {
		this.CreateTime = CreateTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String MsgType) {
		this.MsgType = MsgType;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String Event) {
		this.Event = Event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String EventKey) {
		this.EventKey = EventKey.replace("qrscene_", "");
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String Ticket) {
		this.Ticket = Ticket;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String Latitude) {
		this.Latitude = Latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String Longitude) {
		this.Longitude = Longitude;
	}
	public String getPrecision() {
		return Precision;
	}
	public void setPrecision(String Precision) {
		this.Precision = Precision;
	}
}
