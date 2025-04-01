package org.chobit.spring.redisq.beetle.consumer.retry;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最大重试策略
 *
 * @author robin
 * @since 2025/3/28 8:02
 */
public class MaxRetryStrategy implements MessageRetryStrategy {


	private static final Logger logger = LoggerFactory.getLogger(MaxRetryStrategy.class);


	private final int maxRetryCount;

	public MaxRetryStrategy(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}


	@Override
	public void retry(Message message, BeetleQueue queue, String consumerId) {
		int currentRetryCount = message.getLeftRetryCount();
		if (currentRetryCount >= (this.maxRetryCount - 1)) {
			logger.debug("Max retries {} reached for message with ID [{}] on queue [{}]",
					this.maxRetryCount, message.getId(), queue.topic());
			return;
		}

		message.setLeftRetryCount(++currentRetryCount);
		queue.enqueue(message);
	}
}
