package org.chobit.spring.redisq.beetle.config;

import org.chobit.spring.redisq.beetle.serialization.JacksonSerializer;
import org.chobit.spring.redisq.beetle.serialization.Serializer;

/**
 * 生产配置
 *
 * @author robin
 * @since 2025/3/25 23:16
 */
public class ProduceConfig {


	/**
	 * 要写入的topic
	 */
	private String topic;

	/**
	 * 消息缓存有效时间，单位秒
	 */
	Long ttlSeconds;

	/**
	 * 消息序列化器
	 */
	private Class<? extends Serializer> serializer = JacksonSerializer.class;


	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Long getTtlSeconds() {
		return ttlSeconds;
	}

	public void setTtlSeconds(Long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}

	public Class<? extends Serializer> getSerializer() {
		return serializer;
	}

	public void setSerializer(Class<? extends Serializer> serializer) {
		this.serializer = serializer;
	}
}
