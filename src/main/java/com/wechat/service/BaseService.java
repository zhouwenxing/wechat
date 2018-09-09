package com.wechat.service;

import java.io.Serializable;
import java.util.List;

import com.wechat.core.model.base.DataBaseEntity;

public interface BaseService<T extends Serializable> {
	public DataBaseEntity create(T entity);

	public List<T> create(List<T> enetities);

	public DataBaseEntity update(T entity);

	public List<T> update(List<T> entities);

	public DataBaseEntity createOrUpdate(T entity);
	
	public List<T> createOrUpdate(List<T> entities);

	public T findOne(String id);
	
	public boolean exists(String id);

	public long count(T entity);

	public void delete(String id);

	public void delete(T entity);

	public void delete(List<T> entities);

	public void deleteAll();
	
	public List<T> findAll();
	
	public List<T> find(T entity);
	
	public List<T> findPage(int page, int rows);

}
