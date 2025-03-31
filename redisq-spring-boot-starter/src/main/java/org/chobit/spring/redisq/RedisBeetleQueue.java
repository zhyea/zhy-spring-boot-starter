package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;
import org.chobit.spring.redisq.beetle.queue.QueueStrategy;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

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


	@PostConstruct
	public void initialize() {
		operator.registerConsumerId(topic, consumerIds);
	}


	@Override
	public void enqueue(Message message) {
		// 保存消息
		operator.addMessage(topic, message);
		// 添加到消费队列
		Set<String> consumerIds = operator.getRegisteredConsumerIds(topic);
		for (String consumerId : consumerIds) {
			queueStrategy.enqueue(topic, consumerId, message.getId());
		}
	}


	@Override
	public Message dequeue(String consumerId) {
		return queueStrategy.dequeueNext(topic, consumerId);
	}
}
