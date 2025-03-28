package org.chobit.spring.redisq.beetle.consumer;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.consumer.retry.MessageRetryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认消费者
 *
 * @author robin
 * @since 2025/3/28 7:51
 */
public class DefaultConsumer implements Consumer {


	private static final Logger logger = LoggerFactory.getLogger(DefaultConsumer.class);


	private final String consumerId;
	private final BeetleQueue queue;
	private final ConsumeStrategy consumeStrategy;
	private final MessageRetryStrategy retryStrategy;


	public DefaultConsumer(String consumerId,
	                       BeetleQueue queue,
	                       ConsumeStrategy consumeStrategy,
	                       MessageRetryStrategy retryStrategy) {
		this.consumerId = consumerId;
		this.queue = queue;
		this.consumeStrategy = consumeStrategy;
		this.retryStrategy = retryStrategy;
	}


	@Override
	public void consume(Message message) {

	}
}
