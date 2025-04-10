package org.chobit.spring.redisq.beetle.consumer;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.consumer.retry.MessageRetryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

import static org.chobit.commons.utils.StrKit.isNotBlank;

/**
 * 默认消费者
 *
 * @author robin
 * @since 2025/3/28 7:51
 */
public class MessageConsumer {


	private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);


	private final String consumerId;
	private final BeetleQueue queue;
	private final ConsumeStrategy consumeStrategy;
	private final MessageRetryStrategy retryStrategy;

	private IProcessor processor;


	/**
	 * 构造器
	 *
	 * @param consumerId      消费者ID
	 * @param queue           队列
	 * @param consumeStrategy 消费策略
	 * @param retryStrategy   重试策略
	 */
	public MessageConsumer(String consumerId,
	                       BeetleQueue queue,
	                       ConsumeStrategy consumeStrategy,
	                       MessageRetryStrategy retryStrategy) {
		this.consumerId = consumerId;
		this.queue = queue;
		this.consumeStrategy = consumeStrategy;
		this.retryStrategy = retryStrategy;
	}


	/**
	 * 启动消费者
	 */
	public void start() {
		logger.info("Starting beetle consumer: {}", consumerId);

		String topic = queue.topic();

		assert isNotBlank(topic);

		consumeStrategy.start(topic, () -> {
			try {
				return processNextMessage();
			} catch (Exception e) {
				logger.error("Error occurred while consuming message.", e);
			}
			return null;
		});
	}


	private Message processNextMessage() {
		Message message = null;
		try {
			message = queue.dequeue(consumerId);
		} catch (Exception e) {
			logger.error("Dequeue message for consumer [{}] failed.", consumerId, e);
			throw e;
		}

		try {
			processor.process(message);
		} catch (Throwable e) {
			logger.error("Error occurred while processing message [{}]", message, e);
			retryStrategy.retry(message, queue, consumerId, e);
		}

		return message;
	}


	/**
	 * 停止消费者
	 */
	@PreDestroy
	public void stop() {
		logger.info("Stopping beetle consumer: {}", consumerId);
		consumeStrategy.stop();
		logger.info("Beetle consumer [{}] has been stopped.", consumerId);
	}


	public void setProcessor(IProcessor processor) {
		this.processor = processor;
	}
}
