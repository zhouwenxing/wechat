package com.wechat.core.model.base;

public class DataBaseEntity {
	private boolean success;//是否成功
	private String id;//返回id
	private Object entity;//返回对象
	
	public DataBaseEntity(boolean success,String id){
		this.success=success;
		this.id = id;

	}
	
	public DataBaseEntity(boolean success,String id,Object entity){
		this.id = id;
		this.success=success;
		this.entity = entity;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}
}
