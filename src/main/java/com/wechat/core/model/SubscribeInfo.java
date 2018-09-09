package com.wechat.core.model;

import java.sql.Timestamp;
import javax.persistence.Table;
import com.wechat.core.model.base.BaseEntity;

/**
 * 用户关注订阅信息
 */
@Table(name="dubei_subscribe_info")
public class SubscribeInfo extends BaseEntity {
	private static final long serialVersionUID = 7102657468069363819L;
	private String openId;
	private String unionid;
	private String nickName;//昵称
	private Integer status;//状态 0-取消关注 1-关注
	private Timestamp updateTime;
	private String recommendId;//推荐人(openId)
	private String mediaId;//上传微信的临时素材id
	private Timestamp mediaCreateTime;//上传临时素材时间

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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public Timestamp getMediaCreateTime() {
		return mediaCreateTime;
	}
	public void setMediaCreateTime(Timestamp mediaCreateTime) {
		this.mediaCreateTime = mediaCreateTime;
	}
	
}
