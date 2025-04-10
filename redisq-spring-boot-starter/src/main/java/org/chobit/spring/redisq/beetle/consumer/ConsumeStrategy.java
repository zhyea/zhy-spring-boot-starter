package org.chobit.spring.redisq.beetle.consumer;

import org.chobit.spring.redisq.beetle.Message;

import java.util.concurrent.Callable;

/**
 * 消息消费策略
 *
 * @author robin
 * @since 2025/3/21 22:29
 */
public interface ConsumeStrategy {


	/**
	 * 开始消费
	 *
	 * @param topic    消息topic
	 * @param callback 消息回调
	 */
	void start(String topic, Callable<Message> callback);


	/**
	 * 停止消费
	 */
	void stop();

}
