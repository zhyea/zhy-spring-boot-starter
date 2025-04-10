package org.chobit.spring.redisq.beetle.queue;

import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.persistence.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.chobit.commons.utils.StrKit.isNotBlank;


/**
 * 默认的先入先出队列策略
 *
 * @author robin
 */
public class DefaultQueueStrategy implements QueueStrategy {


	private static final Logger logger = LoggerFactory.getLogger(DefaultQueueStrategy.class);


	private final Operator operator;


	/**
	 * 构造器
	 *
	 * @param operator 消息队列操作者
	 */
	public DefaultQueueStrategy(Operator operator) {
		this.operator = operator;
	}


	@Override
	public void enqueue(String queueName, String consumerId, String messageId) {
		logger.debug("Enqueuing message ID [{}] to queue [{}({})]", messageId, queueName, consumerId);
		operator.enqueueMessageAtTail(queueName, consumerId, messageId);
	}


	@Override
	public Message dequeueNext(String queueName, String consumerId) {
		String messageId = operator.dequeueMessageFromHead(queueName, consumerId, 100L);
		if (isNotBlank(messageId)) {
			logger.debug("Dequeued message id [{}] from queue [{}({})]", messageId, queueName, consumerId);
			return operator.loadMessage(queueName, messageId);
		}
		return null;
	}
}
