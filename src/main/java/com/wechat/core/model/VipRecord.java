package com.wechat.core.model;

import java.sql.Timestamp;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.wechat.core.model.base.BaseEntity;

/**
 * 用户开通会员记录表
 */
@Table(name="dubei_vip_record")
public class VipRecord extends BaseEntity {
	private static final long serialVersionUID = -1715541031480844996L;
	private String userId;//用户id
	@NotNull(message = "请选择购买畅读卡类型")
	private Integer vipType;//1-月卡(30天) 2-季卡(90天) 3-半年(180天) 4-一年(360天)
	private String obtainType;//获得方式：buy-购买 invite-邀请
	private Timestamp createTime;//开通vip时间
	private String createUid;//开通人。本人开通createUid=userId，非本人则createUid!=userId
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getVipType() {
		return vipType;
	}
	public void setVipType(Integer vipType) {
		this.vipType = vipType;
	}
	public String getObtainType() {
		return obtainType;
	}
	public void setObtainType(String obtainType) {
		this.obtainType = obtainType;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getCreateUid() {
		return createUid;
	}
	public void setCreateUid(String createUid) {
		this.createUid = createUid;
	}
}
