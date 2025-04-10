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


	/**
	 * 消息重试
	 *
	 * @param message    消息
	 * @param queue      队列
	 * @param consumerId 消费者ID
	 * @param t          异常
	 */
	void retry(Message message, BeetleQueue queue, String consumerId, Throwable t);

}
