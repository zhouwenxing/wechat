package com.wechat.core.model;

import java.sql.Date;
import javax.persistence.Table;
import com.wechat.core.model.base.BaseEntity;

/**
 * 用户表
 */
@Table(name="dubei_user")
public class User extends BaseEntity {
	private static final long serialVersionUID = -8252228352491888726L;
	
	private String openId;//open_id
	private String unionid;//微信唯一标识id
	private String telephone;//手机号
	private String imageId;//头像
	private String inviteNum;//邀请码
	private String nickName;//昵称
	private Integer sex;//性别(前缀)：1-男 2-女
	private String tag;//标签：最多三个
	private String intro;//一句话简介：30字以内
	private Integer readDuration;//阅读时长：秒
	private String recommendId;//邀请人id
	private Integer vipStatus;//畅读卡状态(前缀)：1-未开通 2-开通中 3-已过期
	private Date vipOverTime;//畅读卡过期时间
	private Integer inviteFlag;//0-暂未通过邀请获得vip 1-已通过邀请获得vip
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getInviteNum() {
		return inviteNum;
	}
	public void setInviteNum(String inviteNum) {
		this.inviteNum = inviteNum;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getReadDuration() {
		return readDuration;
	}
	public void setReadDuration(Integer readDuration) {
		this.readDuration = readDuration;
	}
	public String getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
	public Integer getVipStatus() {
		return vipStatus;
	}
	public void setVipStatus(Integer vipStatus) {
		this.vipStatus = vipStatus;
	}
	public Date getVipOverTime() {
		return vipOverTime;
	}
	public void setVipOverTime(Date vipOverTime) {
		this.vipOverTime = vipOverTime;
	}
	public Integer getInviteFlag() {
		return inviteFlag;
	}
	public void setInviteFlag(Integer inviteFlag) {
		this.inviteFlag = inviteFlag;
	}
}
