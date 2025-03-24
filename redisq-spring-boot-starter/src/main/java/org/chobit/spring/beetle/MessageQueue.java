package org.chobit.spring.beetle;

import org.chobit.spring.beetle.model.Message;
import org.chobit.spring.beetle.queue.MessageCallback;

import java.util.Collection;


public interface MessageQueue {


	/**
	 * 获取队列名称
	 *
	 * @return 队列名称
	 */
	String getQueueName();


	/**
	 * 获取默认消费者ID
	 *
	 * @return 默认消费者ID
	 */
	String defaultConsumerId();


	/**
	 * 获取当前消费者ID
	 *
	 * @return 当前消费者ID
	 */
	Collection<String> currentConsumerIds();


	/**
	 * 获取队列大小
	 *
	 * @return 队列大小
	 */
	long size();


	/**
	 * 获取队列大小
	 *
	 * @param consumerId 消费者ID
	 * @return 队列大小
	 */
	long sizeForConsumer(String consumerId);


	/**
	 * 清空队列
	 */
	void empty();


	/**
	 * 消息入队
	 *
	 * @param message   消息
	 * @param consumers 消费者ID
	 */
	<T> void enqueue(Message<T> message, String... consumers);


	/**
	 * 消息出队
	 *
	 * @param consumer 消费者ID
	 * @param callback 消费后的回调
	 */
	void dequeue(String consumer, MessageCallback callback);
}
