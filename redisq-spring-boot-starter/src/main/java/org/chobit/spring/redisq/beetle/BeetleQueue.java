package org.chobit.spring.redisq.beetle;

import org.chobit.spring.redisq.beetle.queue.MessageCallback;

/**
 * 消息队列数据存储抽象
 *
 * @author robin
 * @since 2025/3/25 7:13
 */
public interface BeetleQueue {


	/**
	 * 获取消息队列topic
	 *
	 * @return 消息队列topic
	 */
	String topic();


	/**
	 * 添加消息到消息队列中
	 *
	 * @param message 消息
	 */
	void enqueue(Message message);


	/**
	 * 消息出队
	 *
	 * @param consumerId 消费者ID
	 * @param callback   回调函数
	 */
	void dequeue(String consumerId, MessageCallback callback);


}
