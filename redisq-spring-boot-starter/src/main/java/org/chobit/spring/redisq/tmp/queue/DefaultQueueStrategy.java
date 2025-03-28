package org.chobit.spring.redisq.tmp.queue;

import org.chobit.spring.redisq.tmp.persistence.RedisOperator;
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


	private final RedisOperator redisOperator;

	public DefaultQueueStrategy(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}


	@Override
	public void enqueue(String queueName, String consumerId, String messageId) {
		logger.debug("Enqueuing message ID [{}] to queue [{}({})]", messageId, queueName, consumerId);
		redisOperator.enqueueMessageAtTail(queueName, consumerId, messageId);
	}


	@Override
	public void dequeueNext(String queueName, String consumerId, MessageCallback callback) {
		String messageId = redisOperator.dequeueMessageFromHead(queueName, consumerId, dequeueTimeoutSeconds);
		if (isNotBlank(messageId)) {
			logger.debug("Dequeued message ID [{}] from queue [{}({})]", messageId, queueName, consumerId);
			callback.handle(messageId);
		}
	}


	public void setDequeueTimeoutSeconds(long dequeueTimeoutSeconds) {
		this.dequeueTimeoutSeconds = dequeueTimeoutSeconds;
	}
}
