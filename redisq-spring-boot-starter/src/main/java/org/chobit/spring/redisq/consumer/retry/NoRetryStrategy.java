package org.chobit.spring.redisq.consumer.retry;

import org.chobit.spring.redisq.MessageQueue;
import org.chobit.spring.redisq.model.Message;

/**
 * 不做重试
 *
 * @author robin
 * @since 2025/3/21 22:06
 */
public class NoRetryStrategy<T> implements MessageRetryStrategy<T> {


	@Override
	public void retry(Message<T> message, MessageQueue queue, String consumerId) {
		// no-op
	}

}
