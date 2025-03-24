package org.chobit.spring.redisq;

import org.chobit.spring.redisq.model.Message;
import org.chobit.spring.redisq.persistence.RedisOperator;
import org.chobit.spring.redisq.queue.MessageCallback;
import org.chobit.spring.redisq.queue.QueueStrategy;

import java.util.Collection;

/**
 * 消息队列主体
 *
 * @author robin
 * @since 2025/3/20 23:37
 */
public class RedisQueue implements MessageQueue {


	private final RedisOperator redisOperator;
	private final String queueName;
	private final QueueStrategy queueStrategy;
	private String defaultConsumerId;


	public RedisQueue(RedisOperator redisOperator, String queueName, QueueStrategy queueStrategy) {
		this.redisOperator = redisOperator;
		this.queueName = queueName;
		this.queueStrategy = queueStrategy;
		this.defaultConsumerId = DEFAULT_CONSUMER_ID;
	}

	private static final String DEFAULT_CONSUMER_ID = "default";


	@Override
	public String getQueueName() {
		return queueName;
	}


	@Override
	public String defaultConsumerId() {
		return DEFAULT_CONSUMER_ID;
	}


	@Override
	public Collection<String> currentConsumerIds() {
		return redisOperator.getRegisteredConsumers(queueName);
	}


	@Override
	public long size() {
		return this.sizeForConsumer(this.defaultConsumerId());
	}


	@Override
	public long sizeForConsumer(String consumerId) {
		return redisOperator.getQueueSizeForConsumer(queueName, consumerId);
	}


	@Override
	public void empty() {
		redisOperator.emptyQueue(queueName);
	}


	@Override
	public <T> void enqueue(Message<T> message, String... consumers) {
		redisOperator.addMessage(queueName, message);
		for (String consumer : consumers) {
			queueStrategy.enqueue(queueName, consumer, message.getId());
		}
	}


	@Override
	public void dequeue(String consumer, MessageCallback callback) {
		queueStrategy.dequeueNext(queueName, consumer, callback);
	}


	public void setDefaultConsumerId(String defaultConsumerId) {
		this.defaultConsumerId = defaultConsumerId;
	}
}
