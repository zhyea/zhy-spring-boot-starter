package org.chobit.spring.redisq.beetle;

/**
 * 消息队列异常
 *
 * @author robin
 * @since 2025/3/25 22:49
 */
public class BeetleException extends RuntimeException {


	public BeetleException(String message) {
		super(message);
	}


	public BeetleException(String message, Throwable cause) {
		super(message, cause);
	}

}
