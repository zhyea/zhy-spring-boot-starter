package org.chobit.spring.redisq.beetle.producer;

import org.chobit.spring.redisq.beetle.Message;

/**
 * 消息生产者
 *
 * @author robin
 * @since 2025/3/25 8:15
 */
public interface Sender {


	/**
	 * 发送消息
	 *
	 * @param payload 消息体对象
	 * @param <T>     消息体类型
	 * @return 发送的消息
	 */
	<T> Message send(T payload);


	/**
	 * 发送消息
	 *
	 * @param payload  消息体对象
	 * @param callback 消息发送后的回调实现
	 * @param <T>      消息体类型
	 */
	<T> void send(T payload, ProduceCallback callback);

}
