package org.chobit.spring.redisq.beetle.persistence;

import org.chobit.spring.redisq.beetle.Message;

/**
 * 持久化操作
 *
 * @author robin
 * @since 2025/3/25 22:27
 */
public interface Operator {


	String nextMessageId(String topic);


	void addMessage(String topic, Message message);


	void enqueueMessageAtTail(String topic, String consumerId, String messageId);


	String dequeueMessageFromHead(String topic, String consumerId, long timeoutSeconds);
}
