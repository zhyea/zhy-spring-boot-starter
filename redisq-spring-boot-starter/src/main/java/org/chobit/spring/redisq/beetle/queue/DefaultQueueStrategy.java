package org.chobit.spring.redisq.beetle.queue;

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


	/**
	 * 默认的dequeue超时时间
	 */
	private long dequeueTimeoutSeconds = 1L;


	private final Operator operator;

	public DefaultQueueStrategy(Operator operator) {
		this.operator = operator;
	}


	@Override
	public void enqueue(String queueName, String consumerId, String messageId) {
		logger.debug("Enqueuing message ID [{}] to queue [{}({})]", messageId, queueName, consumerId);
		operator.enqueueMessageAtTail(queueName, consumerId, messageId);
	}


	@Override
	public void dequeueNext(String queueName, String consumerId, MessageCallback callback) {
		String messageId = operator.dequeueMessageFromHead(queueName, consumerId, dequeueTimeoutSeconds);
		if (isNotBlank(messageId)) {
			logger.debug("Dequeued message ID [{}] from queue [{}({})]", messageId, queueName, consumerId);
			callback.handle(messageId);
		}
	}


	public void setDequeueTimeoutSeconds(long dequeueTimeoutSeconds) {
		this.dequeueTimeoutSeconds = dequeueTimeoutSeconds;
	}
}
