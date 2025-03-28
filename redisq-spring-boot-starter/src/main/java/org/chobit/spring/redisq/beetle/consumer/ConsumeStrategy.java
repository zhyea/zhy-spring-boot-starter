package org.chobit.spring.redisq.beetle.consumer;

/**
 * 消息消费策略
 *
 * @author robin
 * @since 2025/3/21 22:29
 */
public interface ConsumeStrategy {


	void start(String queueName, Runnable callback);


	void stop();

}
