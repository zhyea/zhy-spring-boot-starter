package org.chobit.spring.redisq.beetle.consumer.retry;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;

/**
 * 不做重试
 *
 * @author robin
 * @since 2025/3/28 8:00
 */
public class NoRetryStrategy implements MessageRetryStrategy {

	@Override
	public void retry(Message message, BeetleQueue queue, String consumerId, Throwable t) {
		// 消息不需要重试
		String msg = String.format("Message with id [%s] for consumer [%s] on queue [%s] is applied no-retry strategy.",
				message.getId(), consumerId, queue.topic());
		throw new MessageRetryException(msg, t);
	}

	private static final NoRetryStrategy instance = new NoRetryStrategy();

	public static NoRetryStrategy getInstance() {
		return instance;
	}
}
