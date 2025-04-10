package org.chobit.spring.redisq;

import org.chobit.commons.utils.Collections2;
import org.chobit.spring.redisq.beetle.config.ConsumeConfig;
import org.chobit.spring.redisq.beetle.config.ProduceConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * beetle消息队列配置
 *
 * @author robin
 * @since 2025/3/25 7:30
 */
@ConfigurationProperties(prefix = "redisq")
public class BeetleProperties {

	/**
	 * 消息缓存时长
	 */
	private Long ttlSeconds = TimeUnit.DAYS.toSeconds(1);

	/**
	 * 消费者配置
	 */
	private List<ConsumeConfig> consumer;

	/**
	 * 生产者配置
	 */
	private List<ProduceConfig> producer;


	/**
	 * 获取消息缓存时长
	 *
	 * @return 消息缓存时长
	 */
	public Long getTtlSeconds() {
		return ttlSeconds;
	}

	/**
	 * 设置消息缓存时长
	 *
	 * @param ttlSeconds 消息缓存时长
	 */
	public void setTtlSeconds(Long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}

	/**
	 * 获取消费者配置
	 *
	 * @return 消费者配置
	 */
	public List<ConsumeConfig> getConsumer() {
		return consumer;
	}

	/**
	 * 设置消费者配置
	 *
	 * @param consumer 消费者配置
	 */
	public void setConsumer(List<ConsumeConfig> consumer) {
		this.consumer = consumer;
	}

	/**
	 * 获取生产者配置
	 *
	 * @return 生产者配置
	 */
	public List<ProduceConfig> getProducer() {
		return producer;
	}

	/**
	 * 设置生产者配置
	 *
	 * @param producer 生产者配置
	 */
	public void setProducer(List<ProduceConfig> producer) {
		this.producer = producer;
	}


	/**
	 * 消费者总数
	 *
	 * @return 消费者总数
	 */
	public int totalConsumers() {
		return Collections2.isEmpty(this.consumer) ? 0 : this.consumer.size();
	}
}
