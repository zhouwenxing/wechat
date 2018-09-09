package com.wechat.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService<T>{
	
	@Resource(name = "redisTemplate")
	private RedisTemplate<String, T> redisTemplate;
	
	@Resource(name = "redisTemplate")
	 private ListOperations<String, T> listOps;
	
	@Resource(name = "redisTemplate")
	 private ValueOperations<String, T> valuetOps;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String,String,T> hashOps;
	
	@Resource(name = "redisTemplate")
	 private RedisOperations<Object,Object> redisOps;
	
	public void set(String key, T value) {
		valuetOps.set(key,value);
	}
	
	public T get(String key) {
		return valuetOps.get(key);
	}
	
	/**安全的，原子操作
	 * @param key
	 * @return
	 */
	public Long increment(String key) {
		return valuetOps.increment(key, 1);
	}
	
	/**
	 * 对于并发锁控制够用了
	 */
	public Boolean setIfAbsent(String key, T value) {
		return valuetOps.setIfAbsent(key, value);
	 }
	
	public Boolean setIfAbsent(String key, T value, int seconds) {
		return expireSeconds(key, seconds) && valuetOps.setIfAbsent(key, value);
	 }
	
	/**返回该Key的原有值
	 * @param key
	 * @param value
	 * @return
	 */
	public T getAndSet(String key, T value) {
		return valuetOps.getAndSet(key, value);
	 } 
	
	public Long valueLength(String key) {
		return valuetOps.size(key);
	 }
	
	/** 
	* 压栈 
	*
	* @param key 
	* @param value 插入后链表中元素的数量
	* @return 
	*/
	 public Long lpush(String key, T value) {
		return listOps.leftPush(key, value);
	 }

	 /** 
	* 出栈 
	*
	* @param key 
	* @return 
	*/
	 public T lpop(String key) {
		return listOps.leftPop(key);
	 }

	 /** 
	* 入队 
	*
	* @param key 
	* @param value 插入后 链表中元素的数量
	* @return 
	*/
	 public Long rPush(String key, T value) {
		return listOps.rightPush(key, value);
	 }

	 /** 
	* 出队 
	*
	* @param key 
	* @return 
	*/
	 public T lPop(String key) {
		return listOps.rightPop(key);
	 }

	 /** 
	* 栈/队列长 
	*
	* @param key 
	* @return 
	*/
	 public Long listLength(String key) {
		return listOps.size(key);
	 }

	 /** 
	* 范围检索 
	*
	* @param key 
	* @param start 
	* @param end 
	* @return 
	*/
	 public List<T> range(String key, int start, int end) {
		return listOps.range(key, start, end);
	 }

	 /** 
	* 移除 
	*
	* @param key 
	* @param i 
	* @param value 
	*/
	 public void remove(String key, long i, T value) {
		listOps.remove(key, i, value);
	 }

	 /** 
	* 检索 
	*
	* @param key 
	* @param index 
	* @return 
	*/
	 public T index(String key, long index) {
		return listOps.index(key, index);
	 }

	 /** 
	* 置值 
	*
	* @param key 
	* @param index 
	* @param value 
	*/
	 public void set(String key, long index, T value) {
		listOps.set(key, index, value);
	 }

	 /** 
	* 裁剪 
	*
	* @param key 
	* @param start 
	* @param end 
	*/
	 public void trim(String key, long start, int end) {
		listOps.trim(key, start, end);
	 }
	 
	 
	 public void put(String key, String entryKey, T entryValue){
	 	hashOps.put(key, entryKey, entryValue);
	 }
	 
	 public T get(String key, String entryKey){
		 return hashOps.get(key, entryKey);
	}
	 
	 public Map<String,T> getMap(String key){
	 	 return hashOps.entries(key);
	 }
	 
	 public void delete(String key, String entryKey){
	 	 hashOps.delete(key, entryKey);
	 }
	 
	 public Long hashSize(String key){
	 	 return hashOps.size(key);
	 }
	 
	 /**安全的，原子操作
	 * @param key
	 * @return
	 */
	public Long increment(String key, String entryKey) {
		return hashOps.increment(key, entryKey, 1);
	}
	 
	 public void put(String key, Map<String, T> entry){
		 hashOps.putAll(key, entry);
	 }
	
	/**
	 * 设置多少分钟失效
	 * @param key
	 * @param minutes
	 */
	public Boolean expireMinutes(String key,int minutes){
		return redisOps.expire(key,minutes,TimeUnit.MINUTES);
	}
	/**
	 * 设置多少秒失效
	 * @param key
	 * @param minutes
	 */
	public Boolean expireSeconds(String key,int seconds){
		return redisOps.expire(key,seconds,TimeUnit.SECONDS);
	}
	
	public Boolean expireDays(String key,int days){
		return redisOps.expire(key,days,TimeUnit.DAYS);
	}
	
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	/**
	 * 获取符合条件的key的集合
	 * @param pattern 正则表达式
	 * @return 符合条件的key的集合
	 */
	public Set<String> getKeys(String pattern) {
		return redisTemplate.keys(pattern);
	}
}
