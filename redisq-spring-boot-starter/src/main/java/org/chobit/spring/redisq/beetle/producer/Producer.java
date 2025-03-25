package org.chobit.spring.redisq.beetle.producer;

/**
 * 消息生产者
 *
 * @author robin
 * @since 2025/3/25 8:15
 */
public interface Producer {


	/**
	 * 发送消息
	 *
	 * @param payload 消息体对象
	 * @param <T>     消息体类型
	 */
	<T> void send(T payload);


	<T> void send(T payload, ProduceCallback callback);

}
