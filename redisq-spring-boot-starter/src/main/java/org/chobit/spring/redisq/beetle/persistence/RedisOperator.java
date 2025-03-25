package org.chobit.spring.redisq.beetle.persistence;

import org.chobit.spring.redisq.beetle.Message;

/**
 * Redis操作类
 * @author robin
 * @since 2025/3/25 23:12
 */
public class RedisOperator implements Operator {




	@Override
	public String nextMessageId(String topic) {
		return "";
	}

	@Override
	public void addMessage(String topic, Message message) {
		assert null != message;
		String messageId = nextMessageId(topic);
		message.setId(messageId);
	}


}
