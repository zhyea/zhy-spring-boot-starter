package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;

import java.util.List;

/**
 * 基于redis实现的Beetle队列
 *
 * @author robin
 * @since 2025/3/26 7:36
 */
public class RedisBeetleQueue implements BeetleQueue {


	private final Operator operator;
	private final List<String> consumerIds;


	public RedisBeetleQueue(Operator operator, List<String> consumerIds) {

		assert null != consumerIds && !consumerIds.isEmpty();

		this.operator = operator;
		this.consumerIds = consumerIds;
	}


	@Override
	public String topic() {
		return "";
	}


	@Override
	public void enqueue(Message message) {
		operator.addMessage(topic(), message);
		for (String consumerId : this.consumerIds) {

		}
	}
}
