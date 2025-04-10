package org.chobit.spring.redisq.beetle.consumer.retry;

/**
 * 消息重试异常
 *
 * @author robin
 * @since 2025/3/21 22:01
 */
public class MessageRetryException extends RuntimeException {

	/**
	 * 构造函数
	 *
	 * @param message 异常信息
	 * @param cause   异常原因
	 */
	public MessageRetryException(String message, Throwable cause) {
		super(message, cause);
	}

}
