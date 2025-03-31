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

	public MessageProducer(Map<String, Sender> senders) {
		this.senders = senders;
	}


	public <T> Message produce(String topic, T payload) {
		Sender sender = senders.get(topic);
		if (null == sender) {
			throw new IllegalArgumentException("No sender found for topic: " + topic);
		}
		return sender.send(payload);
	}


	public <T> void produce(String topic, T payload, ProduceCallback callback) {
		Sender sender = senders.get(topic);
		if (null == sender) {
			throw new IllegalArgumentException("No sender found for topic: " + topic);
		}
		sender.send(payload, callback);
	}


}
