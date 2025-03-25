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
	 * @param topic   消息topic
	 * @param payload 消息体对象
	 * @param <T>     消息体类型
	 */
	<T> void send(String topic, T payload);


	<T> void send(String topic, T payload, ProduceCallback callback);

}
