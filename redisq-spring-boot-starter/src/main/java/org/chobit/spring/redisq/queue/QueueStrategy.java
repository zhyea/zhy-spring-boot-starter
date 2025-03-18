package org.chobit.spring.redisq.queue;

/**
 * 出队入队策略
 */
public interface QueueStrategy {


	void dequeueNext(String topicName, String consumerId);


}
