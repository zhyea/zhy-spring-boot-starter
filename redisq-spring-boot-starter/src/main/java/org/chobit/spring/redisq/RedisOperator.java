package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.chobit.commons.utils.StrKit.isBlank;
import static org.chobit.spring.redisq.Keys.*;

/**
 * Redis操作类
 *
 * @author robin
 * @since 2025/3/25 23:12
 */
public class RedisOperator implements Operator {


	private final RedisClient redisClient;


	public RedisOperator(RedisClient redisClient) {
		assert null != redisClient;
		this.redisClient = redisClient;
	}


	@Override
	public String nextMessageId(String topic) {
		String key = keyForNextId(topic);
		Long id = redisClient.increment(key);

		assert null != id;

		return id.toString();
	}


	@Override
	public void addMessage(String topic, Message message) {
		assert null != message;
		assert null != message.getTtlSeconds();

		message.setId(nextMessageId(topic));

		Map<String, String> map = MessageConverter.toMap(message);
		String messageKey = keyForMessage(topic, message.getId());
		redisClient.hmSet(messageKey, map);
		redisClient.expire(messageKey, message.getTtlSeconds(), TimeUnit.SECONDS);
	}


	@Override
	public void enqueueMessageAtTail(String topic, String consumerId, String messageId) {
		if(isBlank(messageId)){
			throw new IllegalArgumentException("Message must have been persisted before being enqueued.");
		}
		String key = keyForQueueConsumer(topic, consumerId);

		redisClient.rightPush(key, messageId);
	}


	@Override
	public String dequeueMessageFromHead(String topic, String consumerId, long timeoutSeconds) {
		String key = keyForQueueConsumer(topic, consumerId);

		return "";
	}


}
