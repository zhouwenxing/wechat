package com.wechat.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wechat.core.model.EventNotice;
import com.wechat.core.model.SubscribeInfo;
import com.wechat.mybatis.MyMapper;

public interface SubscribeInfoMapper extends MyMapper<SubscribeInfo> {

	/**
	 * 获取用户关注信息
	 * @param eventNotice - 
	 * @return
	 */
	List<SubscribeInfo> getSubscribeInfo(@Param("eventNotice") EventNotice eventNotice);

	/**
	 * 根据用户openId获取用户关注的最新一条记录
	 * @param openId
	 * @return
	 */
	SubscribeInfo getNewOneSubscribe(@Param("openId") String openId);

	/**
	 * 计算当前微信用户已经邀请好友关注读呗二维码人数
	 * @param unionid - 微信用户唯一标识
	 * @return
	 */
	int countInviteSubscribeNum(@Param("unionid") String unionid);

	/**
	 * 根据unionid查询微信用户相关信息(如：open_id)
	 * @param unionid
	 * @return
	 */
	SubscribeInfo getByUnionid(@Param("unionid") String unionid);

}
