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
	private Long waitTimeoutMillis;

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


	/**
	 * 获取topic
	 *
	 * @return topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * 设置topic
	 *
	 * @param topic topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * 获取消费者ID
	 *
	 * @return 消费者ID
	 */
	public String getConsumerId() {
		return consumerId;
	}

	/**
	 * 设置消费者ID
	 *
	 * @param consumerId 消费者ID
	 */
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	/**
	 * 获取消费超时时间，单位毫秒
	 *
	 * @return 消费超时时间，单位毫秒
	 */
	public Long getWaitTimeoutMillis() {
		return waitTimeoutMillis;
	}

	/**
	 * 设置消费超时时间，单位毫秒
	 *
	 * @param waitTimeoutMillis 消费超时时间，单位毫秒
	 */
	public void setWaitTimeoutMillis(Long waitTimeoutMillis) {
		this.waitTimeoutMillis = waitTimeoutMillis;
	}

	/**
	 * 获取消费线程数
	 *
	 * @return 消费线程数
	 */
	public Integer getConsumeThreads() {
		return consumeThreads;
	}

	/**
	 * 设置消费线程数
	 *
	 * @param consumeThreads 消费线程数
	 */
	public void setConsumeThreads(Integer consumeThreads) {
		this.consumeThreads = consumeThreads;
	}

	/**
	 * 获取消费处理器ID
	 *
	 * @return 消费处理器ID
	 */
	public String getProcessor() {
		return processor;
	}

	/**
	 * 设置消费处理器ID
	 *
	 * @param processor 消费处理器ID
	 */
	public void setProcessor(String processor) {
		this.processor = processor;
	}

	/**
	 * 获取消费重试配置
	 *
	 * @return 消费重试配置
	 */
	public RetryStrategyEnum getRetry() {
		return retry;
	}

	/**
	 * 设置消费重试配置
	 *
	 * @param retry 消费重试配置
	 */
	public void setRetry(RetryStrategyEnum retry) {
		this.retry = retry;
	}
}
