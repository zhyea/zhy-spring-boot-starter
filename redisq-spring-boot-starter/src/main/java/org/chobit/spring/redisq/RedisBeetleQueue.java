package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;
import org.chobit.spring.redisq.beetle.queue.QueueStrategy;
import org.chobit.spring.redisq.beetle.queue.MessageCallback;

import java.util.List;

import static org.chobit.commons.utils.StrKit.isNotBlank;

/**
 * 基于redis实现的Beetle队列
 *
 * @author robin
 * @since 2025/3/26 7:36
 */
public class RedisBeetleQueue implements BeetleQueue {


	private final String topic;
	private final QueueStrategy queueStrategy;
	private final Operator operator;
	private final List<String> consumerIds;


	public RedisBeetleQueue(String topic,
	                        QueueStrategy queueStrategy,
	                        Operator operator,
	                        List<String> consumerIds) {

		assert isNotBlank(topic);
		assert null != queueStrategy;
		assert null != operator;
		assert null != consumerIds && !consumerIds.isEmpty();

		this.topic = topic;
		this.queueStrategy = queueStrategy;
		this.operator = operator;
		this.consumerIds = consumerIds;
	}


	@Override
	public String topic() {
		return topic;
	}


	@Override
	public void enqueue(Message message) {
		// 保存消息
		operator.addMessage(topic, message);
		// 添加到消费队列
		for (String consumerId : this.consumerIds) {
			queueStrategy.enqueue(topic, consumerId, message.getId());
		}
	}



	@Override
	public void dequeue(String consumerId, MessageCallback callback) {
		queueStrategy.dequeueNext(topic, consumerId, callback);
	}
}
