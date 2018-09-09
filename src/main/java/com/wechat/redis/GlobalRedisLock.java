package com.wechat.redis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wechat.commons.SimpleLockFactory;
import com.wechat.exception.GlobalLockTimeOutException;
import com.wechat.util.SpringContextUtils;

public class GlobalRedisLock {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalRedisLock.class);
	
	private static long maxWaitMillis = 60000;
	private static final String split = "____";

	@SuppressWarnings("unused")
	private static String host = null;
	
	@SuppressWarnings("unchecked")
	private RedisService<String> redisService = (RedisService<String>)SpringContextUtils.getBeanByClass(RedisService.class);
	private final int maxLockMillis = 30000;
	private final long sleepTimeMillis = 3000;
	private final String key;

	static {
		InetAddress localHost = null;
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		host = (UUID.randomUUID() + localHost.getHostAddress()).replace(split, "");
	}
	
	public GlobalRedisLock(String key){
		this.key = key;
	}
	
	/**获取redis锁
	 * @return
	 */
	public boolean lock() {
		Lock lock = SimpleLockFactory.getInstance(key);
		long startTime = System.currentTimeMillis();
		try {
			if (lock.tryLock(maxWaitMillis, TimeUnit.MILLISECONDS)&&lockRedis(startTime)) {// 抢占本地锁,获得redis锁
				return true;
			} else {
				throw new Exception("get redis suo fail");
			}
		} catch (InterruptedException e) { 
			logger.error("获取本地锁被打断",e);
		} catch (GlobalLockTimeOutException e) {
			long costTime = System.currentTimeMillis() - startTime; 
			logger.error(MessageFormat.format("GlobalLockTimeOut ERROR,costTimeMillis={0}",costTime),e.getMessage());
		} catch (Exception e) { 
			logger.error("获取本地锁失败",e);
		} finally{
			lock.unlock();
		}
		return false;
	}
	
	/**
	 * redis解锁
	 */
	public void unlock() {
		String redisThread = redisService.get(key);
		if (Thread.currentThread().toString().equals(redisThread)) {
			unlockRedisLock();
		} else {
			logger.info("thread do not get lock ,can not unlock. key={},redisThread={},current thrad={}" ,
					key,redisThread,Thread.currentThread());
		}

	} 
	
	/**锁实现方式，无解锁通知，故采用自旋等待
	 * @param startTime
	 * @return
	 */
	private boolean lockRedis(long startTime) { 
		int tryCount = 0;
		while (true) {
			tryCount++;
			if(redisService.setIfAbsent(key, Thread.currentThread().toString(), maxLockMillis)){//保证分布式锁有timeOut
				return true;
			} else if(tryCount<4) {// 加锁失败,阻塞调用线程,前三次尝试加锁都会进行超时设置
				try {
					Thread.sleep(sleepTimeMillis);
				} catch (InterruptedException e) {
					logger.error("获取redis锁时线程睡眠失败！");
				}
				if (System.currentTimeMillis() - startTime > maxWaitMillis) {// 超时
					throw new GlobalLockTimeOutException(getErrorString(tryCount));
				}
				continue;
			} else {
				throw new GlobalLockTimeOutException(getErrorString(tryCount));
			}
		}
	}

	private String getErrorString(int tryCount) {
		return MessageFormat.format("GlobalLockTimeOut ERROR .maxLockMillis={0},key={1},retryCount={2},sleepTimeMillis={3}",
				maxLockMillis,key,tryCount,sleepTimeMillis);
	} 
	
	private void unlockRedisLock() {
		redisService.delete(key);
	}

	public int getmaxLockMillis() {
		return maxLockMillis;
	}
	


}
