package com.wechat.core.mapper;

import org.apache.ibatis.annotations.Param;

import com.wechat.core.model.User;
import com.wechat.mybatis.MyMapper;

public interface UserMapper extends MyMapper<User> {

	/**
	 * 根据uninion查询用户vip信息
	 * @param unionid - 微信唯一标识
	 * @return
	 */
	User getUserVipInfo(@Param("unionid") String unionid);

}
