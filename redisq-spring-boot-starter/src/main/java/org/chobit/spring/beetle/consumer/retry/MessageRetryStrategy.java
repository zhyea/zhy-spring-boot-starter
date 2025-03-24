package org.chobit.spring.beetle.consumer.retry;

import org.chobit.spring.beetle.MessageQueue;
import org.chobit.spring.beetle.model.Message;

/**
 * 消息重试策略
 *
 * @author robin
 * @since 2025/3/21 22:04
 */
public interface MessageRetryStrategy<T> {



	void retry(Message<T> message, MessageQueue queue, String consumerId);

}
