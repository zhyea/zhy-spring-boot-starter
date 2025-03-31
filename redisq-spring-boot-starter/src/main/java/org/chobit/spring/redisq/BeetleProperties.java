package org.chobit.spring.redisq;

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
	 * 消息出队超时时间，单位毫秒
	 */
	private Long dequeueTimeout = TimeUnit.SECONDS.toMillis(30);

	/**
	 * 消费者配置
	 */
	private List<ConsumeConfig> consumer;


	/**
	 * 生产者配置
	 */
	private ProduceConfig producer;


	public Long getDequeueTimeout() {
		return dequeueTimeout;
	}

	public void setDequeueTimeout(Long dequeueTimeout) {
		this.dequeueTimeout = dequeueTimeout;
	}

	public List<ConsumeConfig> getConsumer() {
		return consumer;
	}

	public void setConsumer(List<ConsumeConfig> consumer) {
		this.consumer = consumer;
	}

	public ProduceConfig getProducer() {
		return producer;
	}

	public void setProducer(ProduceConfig producer) {
		this.producer = producer;
	}
}
