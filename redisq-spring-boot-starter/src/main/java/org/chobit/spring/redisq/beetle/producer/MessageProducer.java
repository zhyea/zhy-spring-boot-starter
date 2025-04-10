package org.chobit.spring.redisq.beetle.producer;

import org.chobit.spring.redisq.beetle.Message;

import java.util.Map;

/**
 * 默认生产者
 *
 * @author robin
 * @since 2025/3/25 22:25
 */
public class MessageProducer {


	private final Map<String, Sender> senders;

	/**
	 * 构造器
	 *
	 * @param senders 发送者
	 */
	public MessageProducer(Map<String, Sender> senders) {
		this.senders = senders;
	}


	/**
	 * 发送消息
	 *
	 * @param topic   主题
	 * @param payload 消息体
	 * @param <T>     消息体类型
	 * @return 消息
	 */
	public <T> Message produce(String topic, T payload) {
		Sender sender = senders.get(topic);
		if (null == sender) {
			throw new IllegalArgumentException("No sender found for topic: " + topic);
		}
		return sender.send(payload);
	}


	/**
	 * 发送消息
	 *
	 * @param topic    主题
	 * @param payload  消息体
	 * @param callback 回调
	 */
	public <T> void produce(String topic, T payload, ProduceCallback callback) {
		Sender sender = senders.get(topic);
		if (null == sender) {
			throw new IllegalArgumentException("No sender found for topic: " + topic);
		}
		sender.send(payload, callback);
	}


}
