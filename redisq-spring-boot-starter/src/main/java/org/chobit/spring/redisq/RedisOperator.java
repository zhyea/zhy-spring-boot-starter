package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;

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
		String key = Keys.keyForNextId(topic);
		Long id = redisClient.increment(key);

		assert null != id;

		return id.toString();
	}


	@Override
	public void addMessage(String topic, Message message) {
		assert null != message;
		String messageId = nextMessageId(topic);
		message.setId(messageId);
	}


}
