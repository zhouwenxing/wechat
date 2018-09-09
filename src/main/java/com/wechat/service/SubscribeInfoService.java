package com.wechat.service;

import java.util.List;

import com.wechat.core.model.EventNotice;
import com.wechat.core.model.SubscribeInfo;

public interface SubscribeInfoService extends BaseService<SubscribeInfo> {
	
	/**
	 * 获取用户关注信息
	 * @param eventNotice - 
	 * @return
	 */
	List<SubscribeInfo> querySubscribeInfo(EventNotice eventNotice);

	/**
	 * 根据用户openId获取用户关注的最新一条记录
	 * @param openId
	 * @return
	 */
	SubscribeInfo queryNewOneSubscribe(String openId);

	/**
	 * 计算当前微信用户已经邀请好友关注读呗二维码人数
	 * @param unionid - 微信用户唯一标识
	 * @return
	 */
	int countInviteSubscribeNum(String unionid);

	/**
	 * 根据unionid查询微信用户相关信息(open_id)
	 * @param unionid
	 * @return
	 */
	SubscribeInfo queryByUnionid(String unionid);

}
