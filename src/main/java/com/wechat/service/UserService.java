package com.wechat.service;


import com.wechat.core.model.User;

public interface UserService extends BaseService<User> {

	/**
	 * 根据uninion查询用户vip信息
	 * @param unionid - 微信唯一标识
	 * @return
	 */
	User queryUserVipInfo(String unionid);

	/**
	 * 更新用户畅读卡信息
	 * @param unionid
	 */
	void updateUserVip(String unionid);

}
