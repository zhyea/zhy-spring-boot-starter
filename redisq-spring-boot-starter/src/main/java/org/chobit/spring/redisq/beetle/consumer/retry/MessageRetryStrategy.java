package org.chobit.spring.redisq.beetle.consumer.retry;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;

/**
 * 消息重试策略
 *
 * @author robin
 * @since 2025/3/28 7:58
 */
public interface MessageRetryStrategy {


	void retry(Message message, BeetleQueue queue, String consumerId);

}
