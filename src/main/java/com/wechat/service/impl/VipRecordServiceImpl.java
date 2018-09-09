package com.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wechat.core.mapper.VipRecordMapper;
import com.wechat.core.model.VipRecord;
import com.wechat.mybatis.MyMapper;
import com.wechat.service.VipRecordService;

@Service
@Transactional
public class VipRecordServiceImpl extends BaseServiceImpl<VipRecord> implements VipRecordService {
	@Autowired
	private VipRecordMapper vipRecordMapper;
	
	@Override
	protected MyMapper<VipRecord> getMapper() {
		return vipRecordMapper;
	}	
}
