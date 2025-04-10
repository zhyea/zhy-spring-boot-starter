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
	 * 消息序列化器
	 */
	private Class<? extends Serializer> serializer = JacksonSerializer.class;

	/**
	 * 最大重试次数
	 */
	private Integer maxRetry = 3;


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
	 * 获取消息序列化器
	 *
	 * @return 消息序列化器
	 */
	public Class<? extends Serializer> getSerializer() {
		return serializer;
	}

	/**
	 * 设置消息序列化器
	 *
	 * @param serializer 消息序列化器
	 */
	public void setSerializer(Class<? extends Serializer> serializer) {
		this.serializer = serializer;
	}

	/**
	 * 获取最大重试次数
	 *
	 * @return 最大重试次数
	 */
	public Integer getMaxRetry() {
		return maxRetry;
	}

	/**
	 * 设置最大重试次数
	 *
	 * @param maxRetry 最大重试次数
	 */
	public void setMaxRetry(Integer maxRetry) {
		this.maxRetry = maxRetry;
	}
}
