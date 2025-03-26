package org.chobit.spring.redisq.tmp.consumer;

import org.chobit.spring.redisq.tmp.MessageQueue;
import org.chobit.spring.redisq.tmp.consumer.retry.MessageRetryStrategy;
import org.chobit.spring.redisq.tmp.consumer.retry.NoRetryStrategy;
import org.chobit.spring.redisq.tmp.consumer.retry.RetryableMessageException;
import org.chobit.spring.redisq.tmp.model.Message;
import org.chobit.spring.redisq.tmp.persistence.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 消费者
 *
 * @author robin
 * @since 2025/3/24 22:31
 */
public class MessageConsumer<T> implements InitializingBean, DisposableBean {


	private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);


	private final MessageQueue queue;
	private final String consumerId;
	private final MessageListener<T> messageListener;
	private final RedisOperator redisOperator;
	private final Class<T> payloadType;


	private final ConsumeStrategy consumeStrategy = new SingleThreadConsumeStrategy();
	private MessageRetryStrategy<T> retryStrategy = new NoRetryStrategy<>();
	private ConnectionFailureHandler connectionFailureHandler = new DefaultConnectionFailureHandler(logger);


	public MessageConsumer(MessageQueue queue,
	                       String consumerId,
	                       MessageListener<T> messageListener,
	                       RedisOperator redisOperator) {
		this.queue = queue;
		this.consumerId = consumerId;
		this.messageListener = messageListener;
		this.redisOperator = redisOperator;
		this.payloadType = null;
	}


	public void startConsumer() {
		String queueName = queue.getQueueName();

		consumeStrategy.start(queueName, () -> {

		});
	}


	public void stopConsumer() {
		consumeStrategy.stop();
	}


	private void processNextMessage() {
		queue.dequeue(consumerId, messageId -> {
			Message<T> message = redisOperator.loadMessageById(queue.getQueueName(), consumerId, payloadType);
			try{
				this.handleMessage(message);
			}catch(RetryableMessageException e){
				retryStrategy.retry(message, queue, consumerId);
			} catch (Throwable t) {
				this.handleExceptionWhileProcessingMessage(message, t);
			}
		});
	}


	private void handleMessage(Message<T> message) throws RetryableMessageException {
		messageListener.onMessage(message);
	}


	protected void handleExceptionWhileProcessingMessage(Message<T> message, Throwable t) {
		logger.error("Exception while handling message with Id [{}]", message, t);

	}


	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void destroy() throws Exception {
		this.stopConsumer();
	}
}
