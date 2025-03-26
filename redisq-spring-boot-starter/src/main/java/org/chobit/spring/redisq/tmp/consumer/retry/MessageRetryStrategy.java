package org.chobit.spring.redisq.tmp.consumer.retry;

import org.chobit.spring.redisq.tmp.MessageQueue;
import org.chobit.spring.redisq.tmp.model.Message;

/**
 * 消息重试策略
 *
 * @author robin
 * @since 2025/3/21 22:04
 */
public interface MessageRetryStrategy<T> {



	void retry(Message<T> message, MessageQueue queue, String consumerId);

}
