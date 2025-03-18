package org.chobit.spring.redisq;

import org.chobit.spring.redisq.model.Message;
import org.chobit.spring.redisq.queue.MessageCallback;

import java.util.Collection;

public interface MessageQueue {


    /**
     * 获取队列名称
     *
     * @return 队列名称
     */
    String getTopicName();

    /**
     * 获取默认消费者ID
     *
     * @return 默认消费者ID
     */
    String getDefaultConsumerId();

    /**
     * 获取当前消费者ID
     *
     * @return 当前消费者ID
     */
    Collection<String> getCurrentConsumerIds();

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    long getSize();


    /**
     * 获取队列大小
     *
     * @param consumerId 消费者ID
     * @return 队列大小
     */
    long getSizeForConsumer(String consumerId);


    void empty();


    <T> void enqueue(Message<T> message, String... consumers);


    void dequeue(String consumer, MessageCallback callback);
}
