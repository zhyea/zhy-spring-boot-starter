package org.chobit.spring.redisq;

import org.chobit.spring.redisq.model.Message;
import org.chobit.spring.redisq.queue.MessageCallback;

import java.util.Collection;
import java.util.Collections;

/**
 * @author robin
 * @since 2025/3/20 23:37
 */
public class RedisQueue implements MessageQueue{
	@Override
	public String getQueueName() {
		return "";
	}

	@Override
	public String getDefaultConsumerId() {
		return "";
	}

	@Override
	public Collection<String> getCurrentConsumerIds() {
		return Collections.emptyList();
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public long getSizeForConsumer(String consumerId) {
		return 0;
	}

	@Override
	public void empty() {

	}

	@Override
	public <T> void enqueue(Message<T> message, String... consumers) {

	}

	@Override
	public void dequeue(String consumer, MessageCallback callback) {

	}
}
