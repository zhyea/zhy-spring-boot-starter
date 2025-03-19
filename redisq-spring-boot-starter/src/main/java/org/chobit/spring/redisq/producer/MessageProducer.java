package org.chobit.spring.redisq.producer;

/**
 * 消息生产者
 *
 * @author robin
 */
public interface MessageProducer {


	MessageSender create();


}
