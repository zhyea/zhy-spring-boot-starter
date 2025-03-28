package org.chobit.spring.redisq.beetle.consumer;

import org.chobit.spring.redisq.beetle.Message;

/**
 * 消费者接口
 *
 * @author robin
 * @since 2025/3/27 23:28
 */
public interface Consumer {


	void consume(Message message);
}
