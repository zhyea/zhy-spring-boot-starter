package org.chobit.spring.redisq.beetle;

/**
 * 消息队列异常
 *
 * @author robin
 * @since 2025/3/25 22:49
 */
public class BeetleException extends RuntimeException {


	/**
	 * 构造函数
	 *
	 * @param message 异常信息
	 */
	public BeetleException(String message) {
		super(message);
	}


	/**
	 * 构造函数
	 *
	 * @param message 异常信息
	 * @param cause   异常
	 */
	public BeetleException(String message, Throwable cause) {
		super(message, cause);
	}

}
