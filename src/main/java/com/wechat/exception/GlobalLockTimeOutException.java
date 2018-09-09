package com.wechat.exception;

public class GlobalLockTimeOutException extends RuntimeException {
	
	  private static final long serialVersionUID = 5781958965016402002L;  
	  
	    /** 
	     * 创建RedisLockException 
	     * @param msg 
	     */  
	    public GlobalLockTimeOutException(String msg) {  
	        super(msg);  
	    }  
	      
	    /** 
	     * 创建RedisLockException 
	     * @param msg 
	     * @param cause 
	     */  
	    public GlobalLockTimeOutException(String msg, Throwable cause) {  
	        super(msg, cause);  
	    }  
}
