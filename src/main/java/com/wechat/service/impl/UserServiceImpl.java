package com.wechat.service.impl;

import java.sql.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wechat.commons.CommonStatus;
import com.wechat.core.mapper.UserMapper;
import com.wechat.core.model.User;
import com.wechat.core.model.VipRecord;
import com.wechat.mybatis.MyMapper;
import com.wechat.service.UserService;
import com.wechat.service.VipRecordService;
import com.wechat.util.DateUtil;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PlatformTransactionManager ptm;
	@Autowired
	private VipRecordService vipRecordService;

	@Override
	protected MyMapper<User> getMapper() {
		return userMapper;
	}

	/**
	 * 根据uninion查询用户vip信息
	 * @param unionid - 微信唯一标识
	 * @return
	 */
	@Override
	public User queryUserVipInfo(String unionid) {
		return userMapper.getUserVipInfo(unionid);
	}

	/**
	 * 更新用户畅读卡信息
	 * @param unionid
	 */
	@Override
	public void updateUserVip(String unionid) {
		User user = this.queryUserVipInfo(unionid);
		if(null != user){
			int inviteFlag = user.getInviteFlag();
			if(0 == inviteFlag){//0表示尚未通过邀请得到会员
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				TransactionStatus ts = ptm.getTransaction(def);// 开启编程式事务
				try{
					//1.更新用户畅读卡时间
					user = countUserVipOverTime(user, 365);
					user.setVipStatus(CommonStatus.IS_VIP);
					user.setInviteFlag(1);
					this.update(user);
					//2.生成获取畅读卡记录
					VipRecord vipRecord = new VipRecord();
					vipRecord.setUserId(user.getId());
					vipRecord.setVipType(CommonStatus.VIP_ONE_YEAR);
					vipRecord.setObtainType("invite");
					vipRecordService.create(vipRecord);
					ptm.commit(ts);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("邀请获得畅读卡失败，用户unionid：{}",unionid);
					ptm.rollback(ts);
				}
			}
		}
	}
	
	/**
	 * 计算用户vip过期时间
	 * @param user
	 * @param buyVipDays
	 * @return
	 */
	private User countUserVipOverTime(User user, int buyVipDays) {
		int vipStatus = user.getVipStatus();
		Date vipNewOverDate = null;
		if(CommonStatus.IS_VIP == vipStatus){//当前是会员
			Date vipOverTime = user.getVipOverTime();
			vipNewOverDate = DateUtil.addDays(vipOverTime,buyVipDays);
		}else{//不是会员或者会员过期了
			vipNewOverDate = DateUtil.addDays(new Date(System.currentTimeMillis()),buyVipDays-1);//当天算一天
		}
		user.setVipOverTime(vipNewOverDate);
		return user;
	}
	@Test
	public void test(){
		Date vipNewOverDate = DateUtil.addDays(new Date(System.currentTimeMillis()),365-1);
		System.out.println(vipNewOverDate);
	}
}
