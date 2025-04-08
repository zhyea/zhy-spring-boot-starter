package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;
import org.chobit.spring.redisq.beetle.queue.QueueStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.QueryTimeoutException;

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

	private static final Logger logger = LoggerFactory.getLogger(RedisBeetleQueue.class);

	private final String topic;
	private final QueueStrategy queueStrategy;
	private final Operator operator;

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

        operator.ensureRegisterConsumerIds(topic, consumerIds);
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
		Set<String> consumerIds = operator.getRegisteredConsumerIds(topic);
		for (String consumerId : consumerIds) {
			queueStrategy.enqueue(topic, consumerId, message.getId());
		}
	}


	@Override
	public Message dequeue(String consumerId) {
		try{
			return queueStrategy.dequeueNext(topic, consumerId);
		}catch (QueryTimeoutException e){
			logger.debug("Query redis timeout while dequeueing message from queue [{}({})]", topic, consumerId, e);
			return null;
		}catch (Exception e){
			logger.error("Error occurred while dequeueing message from queue [{}({})]", topic, consumerId, e);
			return null;
 		}
	}
}
