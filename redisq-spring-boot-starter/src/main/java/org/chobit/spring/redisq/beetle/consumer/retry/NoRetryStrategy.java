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
	public void retry(Message message, BeetleQueue queue, String consumerId) {
		// no-op
	}
}
