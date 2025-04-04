package org.chobit.spring.redisq.beetle.config;

import org.chobit.spring.redisq.beetle.constants.RetryStrategyEnum;

/**
 * 消费者配置
 *
 * @author robin
 * @since 2025/3/30 12:09
 */
public class ConsumeConfig {


    /**
     * 要消费的topic
     */
    private String topic;

    /**
     * 消费者ID
     */
    private String consumerId;

    /**
     * 消费超时时间，单位毫秒
     */
    private Long consumeTimeout;

    /**
     * 消费线程数
     */
    private Integer consumeThreads = 1;

    /**
     * 消费处理器ID
     */
    private String processor;

    /**
     * 消费重试配置
     */
    private RetryStrategyEnum retry;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Long getConsumeTimeout() {
        return consumeTimeout;
    }

    public void setConsumeTimeout(Long consumeTimeout) {
        this.consumeTimeout = consumeTimeout;
    }

    public Integer getConsumeThreads() {
        return consumeThreads;
    }

    public void setConsumeThreads(Integer consumeThreads) {
        this.consumeThreads = consumeThreads;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public RetryStrategyEnum getRetry() {
        return retry;
    }

    public void setRetry(RetryStrategyEnum retry) {
        this.retry = retry;
    }
}
