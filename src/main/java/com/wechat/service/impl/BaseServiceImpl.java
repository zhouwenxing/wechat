package com.wechat.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.PageHelper;
import com.wechat.core.model.base.BaseEntity;
import com.wechat.core.model.base.DataBaseEntity;
import com.wechat.mybatis.MyMapper;
import com.wechat.service.BaseService;
import com.wechat.util.ConvertTimeUtil;
import com.wechat.util.IdGen;

@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
	
	protected abstract MyMapper<T> getMapper();

	@Override
	public DataBaseEntity create(T entity) {
		Assert.notNull(entity);
		String id = entity.getId();
		if(StringUtils.isBlank(id)){
			id = IdGen.uuid();
		}
		entity.setId(id);
		Timestamp createTime = (Timestamp) ObjectUtils.defaultIfNull(entity.getCreateTime(),ConvertTimeUtil.getNowTimestamp());
		entity.setCreateTime(createTime);
		int count = getMapper().insertSelective(entity);
		return new DataBaseEntity(count>0,id,entity);
	}

	@Override
	public List<T> create(List<T> entities) {
		Assert.notEmpty(entities);
		Timestamp time = ConvertTimeUtil.getNowTimestamp();
		for (T t : entities) {
			Assert.notNull(t);
			String id = (String) ObjectUtils.defaultIfNull(t.getId(),IdGen.uuid());
			t.setId(id);
			Timestamp createTime = (Timestamp) ObjectUtils.defaultIfNull(t.getCreateTime(),time);
			t.setCreateTime(createTime);
		}
		getMapper().insertList(entities);
		return entities;
	}

	@Override
	public DataBaseEntity update(T entity) {
		Assert.notNull(entity);
		Assert.notNull(entity.getId());
		int count = getMapper().updateByPrimaryKeySelective(entity);
		T existing = getMapper().selectByPrimaryKey(entity.getId());
		return new DataBaseEntity(count>0,null==existing?"":existing.getId(),existing);
	}

	@Override
	public List<T> update(List<T> entities) {
		Assert.notEmpty(entities);
		for (T t : entities) {
			update(t);
		}
		return entities;
	}

	@Override
	public DataBaseEntity createOrUpdate(T entity) {
		Assert.notNull(entity);
		int count = 0;
		if (entity.getId() != null && findOne(entity.getId()) != null) {
			count = getMapper().updateByPrimaryKeySelective(entity);
        } else {
        	entity.setId(IdGen.uuid());
        	count=getMapper().insert(entity);
        }
		return new DataBaseEntity(count>0,entity.getId(),entity);
	}

	@Override
	public List<T> createOrUpdate(List<T> entities) {
		Assert.notEmpty(entities);
		for (T t : entities) {
			createOrUpdate(t);
		}
		return entities;
	}

	
	@Override
	public T findOne(String id) {
		Assert.notNull(id);
		return getMapper().selectByPrimaryKey(id);
	}

	@Override
	public boolean exists(String id) {
		Assert.notNull(id);
		return findOne(id) != null;
	}

	@Override
	public long count(T entity) {
		return getMapper().selectCount(entity);
	}
	
	@Override
	public List<T> find(T entity){
		return getMapper().select(entity);
	}

	@Override
	public void delete(String id) {
		Assert.notNull(id);
		getMapper().deleteByPrimaryKey(id);
	}

	@Override
	public void delete(T entity) {
		Assert.notNull(entity);
		getMapper().deleteByPrimaryKey(entity.getId());
	}

	@Override
	public void delete(List<T> entities) {
		Assert.notEmpty(entities);
		for (T t : entities) {
			delete(t);
		}
	}

	@Override
	public void deleteAll() {
		delete(findAll());		
	}
	
	@Override
	public List<T> findAll() {
        return getMapper().selectAll();
	}

	public List<T> findPage(int page, int rows) {
        PageHelper.startPage(page, rows, "id");
        return getMapper().selectAll();
	}

	public List<T> search(T entity) {
		Assert.notNull(entity);
		if (entity.getPage() != null && entity.getRows() != null) {
            PageHelper.startPage(entity.getPage(), entity.getRows(), "id");
        }
		return getMapper().select(entity);
	}

}
