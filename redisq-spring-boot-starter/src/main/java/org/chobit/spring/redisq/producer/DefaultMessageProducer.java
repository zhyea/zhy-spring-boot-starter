package org.chobit.spring.redisq.producer;


import org.chobit.spring.redisq.MessageQueue;
import org.chobit.spring.redisq.model.Message;
import org.chobit.spring.redisq.persistence.RedisOperator;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.chobit.commons.utils.StrKit.isNotBlank;

/**
 * 默认消息生产者
 *
 * @author robin
 */
public class DefaultMessageProducer<T> implements MessageProducer<T>, InitializingBean {


	private final RedisOperator redisOperator;
	private final MessageQueue queue;
	private SubmissionStrategy submissionStrategy;


	public DefaultMessageProducer(RedisOperator redisOperator, MessageQueue queue) {
		this.redisOperator = redisOperator;
		this.queue = queue;
	}


	private static final long DEFAULT_TIME_TO_LIVE = 1;
	private static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.DAYS;


	@Override
	public MessageSender<T> create(T payload) {
		return new DefaultMessageSender(payload);
	}


	@Override
	public void submit(T payload) {
		create(payload).submit();
	}


	private void submit(Message<T> message) {
		submissionStrategy.submit(queue, message);
	}


	private void submit(Message<T> message, String consumer) {
		submissionStrategy.submit(queue, message, consumer);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if (null == submissionStrategy) {
			submissionStrategy = new MultiConsumerSubmissionStrategy(redisOperator);
		}
	}


	private class DefaultMessageSender implements MessageSender<T> {


		private final T payload;
		private long timeToLive;
		private TimeUnit timeToLiveUnit;
		private String targetConsumer;
		private SubmissionStrategy submissionStrategy;


		public DefaultMessageSender(T payload) {
			this.payload = payload;
			this.timeToLive = DEFAULT_TIME_TO_LIVE;
			this.timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;
		}

		@Override
		public MessageSender<T> withTimeToLive(long time, TimeUnit timeUnit) {
			this.timeToLive = time;
			this.timeToLiveUnit = timeUnit;
			return this;
		}

		@Override
		public MessageSender<T> withTargetConsumer(String consumerId) {
			this.targetConsumer = consumerId;
			return this;
		}

		@Override
		public void submit() {
			long ttlSeconds = timeToLiveUnit.toSeconds(timeToLive);

			Message<T> message = new Message<>();
			message.setCreation(LocalDateTime.now());
			message.setPayload(payload);
			message.setTimeToLiveSeconds(ttlSeconds);

			redisOperator.addMessage(queue.getQueueName(), message);
			if (isNotBlank(targetConsumer)) {
				DefaultMessageSender.this.submit(message, targetConsumer);
			}
		}
	}

	// ------------
}
