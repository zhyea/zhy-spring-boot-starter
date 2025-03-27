package org.chobit.spring.redisq.beetle.serialization;

/**
 * 序列化反序列化异常
 *
 * @author robin
 * @since 2025/3/27 8:35
 */
public class SerializationException extends RuntimeException {


	public SerializationException(String message) {
		super(message);
	}


	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}


}
