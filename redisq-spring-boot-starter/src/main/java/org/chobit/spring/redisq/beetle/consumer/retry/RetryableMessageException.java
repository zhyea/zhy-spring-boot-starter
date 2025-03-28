package org.chobit.spring.redisq.beetle.consumer.retry;

/**
 * 可重试消息异常
 *
 * @author robin
 * @since 2025/3/21 22:01
 */
public class RetryableMessageException extends RuntimeException {


	public RetryableMessageException() {
		super();
	}

	public RetryableMessageException(String message) {
		super(message);
	}

	public RetryableMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
