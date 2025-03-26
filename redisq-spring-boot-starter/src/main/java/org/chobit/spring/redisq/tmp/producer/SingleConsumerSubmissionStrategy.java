package org.chobit.spring.redisq.tmp.producer;

import org.chobit.spring.redisq.tmp.MessageQueue;
import org.chobit.spring.redisq.tmp.model.Message;

/**
 * @author robin
 * @since 2025/3/20 23:07
 */
public class SingleConsumerSubmissionStrategy implements SubmissionStrategy {


	@Override
	public <T> void submit(MessageQueue queue, Message<T> message) {
		submit(queue, message, queue.defaultConsumerId());
	}


	@Override
	public <T> void submit(MessageQueue queue, Message<T> message, String consumer) {
		queue.enqueue(message, consumer);
	}


}
