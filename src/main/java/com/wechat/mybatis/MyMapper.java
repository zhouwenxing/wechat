package com.wechat.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 此接口不能被扫描到，否则会报错
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
