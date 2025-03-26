package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;

/**
 * 基于redis实现的Beetle队列
 *
 * @author robin
 * @since 2025/3/26 7:36
 */
public class RedisBeetleQueue  implements BeetleQueue {


	private final Operator operator;


	public RedisBeetleQueue(Operator operator) {
		this.operator = operator;
	}


	@Override
	public String topic() {
		return "";
	}

	@Override
	public void enqueue(Message message) {

	}
}
