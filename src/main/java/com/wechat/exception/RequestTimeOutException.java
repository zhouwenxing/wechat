package com.wechat.exception;

public class RequestTimeOutException  extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestTimeOutException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public RequestTimeOutException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public RequestTimeOutException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RequestTimeOutException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
