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

	private MessageProcessor processor;


	public MessageConsumer(String consumerId,
	                       BeetleQueue queue,
	                       ConsumeStrategy consumeStrategy,
	                       MessageRetryStrategy retryStrategy) {
		this.consumerId = consumerId;
		this.queue = queue;
		this.consumeStrategy = consumeStrategy;
		this.retryStrategy = retryStrategy;
	}


	public void start() {
		logger.info("Starting beetle consumer: {}", consumerId);

		String topic = queue.topic();

		assert isNotBlank(topic);

		consumeStrategy.start(topic, () -> {
			try {
				processNextMessage();
			} catch (Exception e) {
				logger.error("Error occurred while consuming message.", e);
			}
		});
	}


	private void processNextMessage() {
		Message message = null;
		try {
			message = queue.dequeue(consumerId);
			processor.process(message);
		} catch (Throwable e) {
			retryStrategy.retry(message, queue, consumerId);
		}
	}


	@PreDestroy
	public void stop() {
		logger.info("Stopping beetle consumer: {}", consumerId);
		consumeStrategy.stop();
	}


	public void setProcessor(MessageProcessor processor) {
		this.processor = processor;
	}
}
