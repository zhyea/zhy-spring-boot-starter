package org.chobit.spring.redisq.consumer;

import org.chobit.spring.redisq.consumer.retry.RetryableMessageException;
import org.chobit.spring.redisq.model.Message;

/**
 * 消息监听器
 *
 * @author robin
 * @since 2025/3/21 22:21
 */
public interface MessageListener<T> {


	void onMessage(Message<T> message) throws RetryableMessageException;

}
