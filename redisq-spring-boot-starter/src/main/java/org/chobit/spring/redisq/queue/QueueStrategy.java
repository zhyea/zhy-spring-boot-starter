package org.chobit.spring.redisq.queue;


/**
 * 出队入队策略
 *
 * @author robin
 */
public interface QueueStrategy {


    void dequeueNext(String topicName, String consumerId);


    void enqueue(String topicName, String consumerId, String messageId);

}
