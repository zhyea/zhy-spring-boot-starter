package org.chobit.spring.redisq.producer;

/**
 * 消息生产者接口
 *
 * @author robin
 * @since 2025-03-20
 */
public interface MessageProducer<T> {


	MessageSender create(T payload);


	void submit(T payload);

}
