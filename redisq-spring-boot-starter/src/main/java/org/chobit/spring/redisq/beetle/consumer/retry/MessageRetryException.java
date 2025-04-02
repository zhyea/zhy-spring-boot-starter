package org.chobit.spring.redisq.beetle.consumer.retry;

/**
 * 消息重试异常
 *
 * @author robin
 * @since 2025/3/21 22:01
 */
public class MessageRetryException extends RuntimeException {

	public MessageRetryException(String message, Throwable cause) {
		super(message, cause);
	}

}
