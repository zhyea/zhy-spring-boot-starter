package org.chobit.spring.redisq.beetle.queue;


import org.chobit.spring.redisq.beetle.Message;

/**
 * 消息出队入队策略
 *
 * @author robin
 */
public interface QueueStrategy {


	/**
	 * 消息入队操作
	 *
	 * @param queueName  入队的队列名称
	 * @param consumerId 指定的消费者ID
	 * @param messageId  消息ID
	 */
	void enqueue(String queueName, String consumerId, String messageId);


	/**
	 * 消息出队操作
	 *
	 * @param queueName  队列名称
	 * @param consumerId 消费者名称
	 * @return 消息
	 */
	Message dequeueNext(String queueName, String consumerId);


}
