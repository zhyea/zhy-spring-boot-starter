package org.chobit.spring.redisq.producer;

import org.chobit.spring.redisq.MessageQueue;
import org.chobit.spring.redisq.model.Message;

/**
 * @author robin
 * @since 2025/3/20 23:07
 */
public class SingleConsumerSubmissionStrategy implements SubmissionStrategy {


	@Override
	public <T> void submit(MessageQueue queue, Message<T> message) {
		submit(queue, message, queue.getDefaultConsumerId());
	}


	@Override
	public <T> void submit(MessageQueue queue, Message<T> message, String consumer) {
		queue.enqueue(message, consumer);
	}


}
