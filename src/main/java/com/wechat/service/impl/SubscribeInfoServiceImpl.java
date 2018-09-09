package com.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.core.mapper.SubscribeInfoMapper;
import com.wechat.core.model.EventNotice;
import com.wechat.core.model.SubscribeInfo;
import com.wechat.mybatis.MyMapper;
import com.wechat.service.SubscribeInfoService;

@Service
@Transactional
public class SubscribeInfoServiceImpl extends BaseServiceImpl<SubscribeInfo> implements SubscribeInfoService{
	
	@Autowired
	private SubscribeInfoMapper subscribeInfoMapper;
	
	@Override
	protected MyMapper<SubscribeInfo> getMapper() {
		return subscribeInfoMapper;
	}
	
	

	/**
	 * 获取用户关注信息
	 * @param eventNotice - 
	 * @return
	 */
	@Override
	public List<SubscribeInfo> querySubscribeInfo(EventNotice eventNotice) {
		return subscribeInfoMapper.getSubscribeInfo(eventNotice);
	}


	/**
	 * 根据用户openId获取用户关注的最新一条记录
	 * @param openId
	 * @return
	 */
	@Override
	public SubscribeInfo queryNewOneSubscribe(String openId) {
		return subscribeInfoMapper.getNewOneSubscribe(openId);
	}


	/**
	 * 计算当前微信用户已经邀请好友关注读呗二维码人数
	 * @param unionid - 微信用户唯一标识
	 * @return
	 */
	@Override
	public int countInviteSubscribeNum(String unionid) {
		return subscribeInfoMapper.countInviteSubscribeNum(unionid);
	}


	/**
	 * 根据unionid查询微信用户相关信息(open_id)
	 * @param unionid
	 * @return
	 */
	@Override
	public SubscribeInfo queryByUnionid(String unionid) {
		return subscribeInfoMapper.getByUnionid(unionid);
	}

	

}
