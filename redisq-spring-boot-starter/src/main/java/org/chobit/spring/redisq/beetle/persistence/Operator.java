package org.chobit.spring.redisq.beetle.persistence;

import org.chobit.spring.redisq.beetle.Message;

import java.util.List;
import java.util.Set;

/**
 * 持久化操作
 *
 * @author robin
 * @since 2025/3/25 22:27
 */
public interface Operator {


	/**
	 * 注册消费者ID
	 *
	 * @param topic       消息topic
	 * @param consumerIds 消费者ID集合
	 */
	void registerConsumerId(String topic, List<String> consumerIds);


	/**
	 * 获取已注册的消费者ID
	 *
	 * @param topic 消息topic
	 * @return 消费者ID集合
	 */
	Set<String> getRegisteredConsumerIds(String topic);


	/**
	 * 生成消息id
	 *
	 * @param topic 消息topic
	 * @return 消息id
	 */
	String nextMessageId(String topic);


	/**
	 * 添加消息
	 *
	 * @param topic   消息topic
	 * @param message 消息
	 */
	void addMessage(String topic, Message message);


	/**
	 * 入队消息
	 *
	 * @param topic      消息topic
	 * @param consumerId 消费者ID
	 * @param messageId  消息id
	 */
	void enqueueMessageAtTail(String topic, String consumerId, String messageId);


	/**
	 * 出队消息
	 *
	 * @param topic          消息topic
	 * @param consumerId     消费者ID
	 * @param timeoutSeconds 超时时间
	 * @return 消息id
	 */
	String dequeueMessageFromHead(String topic, String consumerId, long timeoutSeconds);


	/**
	 * 加载消息
	 *
	 * @param topic     队列topic
	 * @param messageId 消息id
	 * @return 消息
	 */
	Message loadMessage(String topic, String messageId);
}
