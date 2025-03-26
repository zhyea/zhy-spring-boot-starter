package org.chobit.spring.redisq.tmp.consumer;

import org.chobit.spring.redisq.tmp.consumer.retry.RetryableMessageException;
import org.chobit.spring.redisq.tmp.model.Message;

/**
 * 消息监听器
 *
 * @author robin
 * @since 2025/3/21 22:21
 */
public interface MessageListener<T> {


	void onMessage(Message<T> message) throws RetryableMessageException;

}
