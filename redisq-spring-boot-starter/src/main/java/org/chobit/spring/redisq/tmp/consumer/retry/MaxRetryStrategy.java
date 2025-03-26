package org.chobit.spring.redisq.tmp.consumer.retry;

import org.chobit.spring.redisq.tmp.MessageQueue;
import org.chobit.spring.redisq.tmp.model.Message;
import org.chobit.spring.redisq.tmp.persistence.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最大次数重试策略
 *
 * @author robin
 * @since 2025/3/21 22:08
 */
public class MaxRetryStrategy<T> implements MessageRetryStrategy<T> {


	private static final Logger logger = LoggerFactory.getLogger(MaxRetryStrategy.class);


	private final int maxRetryCount;
	private final RedisOperator redisOperator;

	public MaxRetryStrategy(int maxRetryCount, RedisOperator redisOperator) {
		this.maxRetryCount = maxRetryCount;
		this.redisOperator = redisOperator;
	}


	@Override
	public void retry(Message<T> message, MessageQueue queue, String consumerId) {
		int currentRetryCount = message.getRetryCount();
		if (currentRetryCount >= (this.maxRetryCount - 1)) {
			logger.debug("Max retries {} reached for message with ID [{}] on queue [{}]",
					this.maxRetryCount, message.getId(), queue.getQueueName());
			return;
		}

		message.setRetryCount(++currentRetryCount);
		queue.enqueue(message, consumerId);
	}

}
