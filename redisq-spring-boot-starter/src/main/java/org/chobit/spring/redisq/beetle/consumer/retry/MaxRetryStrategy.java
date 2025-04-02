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


	@Override
	public void retry(Message message, BeetleQueue queue, String consumerId, Throwable t) {

		if (message.getMaxRetryCount() <= 0) {
			// 消息不需要重试
			String msg = String.format("Message with id [%s] for consumer [%s] on queue [%s] is not allowed to be retried.",
					message.getId(), consumerId, queue.topic());
			throw new MessageRetryException(msg, t);
		}

		int leftRetryCount = message.getLeftRetryCount();

		if (leftRetryCount <= 0) {
			String msg = String.format("Max retries %d reached for message with id [%s] for consumer [%s] on queue [%s]",
					message.getMaxRetryCount(), message.getId(), consumerId, queue.topic());
			logger.debug(msg);
			throw new MessageRetryException(msg, t);
		}

		message.setLeftRetryCount(++leftRetryCount);
		queue.enqueue(message);
	}
}
