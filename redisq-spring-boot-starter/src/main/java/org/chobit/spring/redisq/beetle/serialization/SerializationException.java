package org.chobit.spring.redisq.beetle.serialization;

/**
 * 序列化反序列化异常
 *
 * @author robin
 * @since 2025/3/27 8:35
 */
public class SerializationException extends RuntimeException {


	/**
	 * 构造函数
	 *
	 * @param message 异常信息
	 */
	public SerializationException(String message) {
		super(message);
	}


	/**
	 * 构造函数
	 *
	 * @param message 异常信息
	 * @param cause   异常原因
	 */
	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}


}
