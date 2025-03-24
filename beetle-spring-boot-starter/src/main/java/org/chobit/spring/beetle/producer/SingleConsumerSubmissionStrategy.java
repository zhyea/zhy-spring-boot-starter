package org.chobit.spring.beetle.producer;

import org.chobit.spring.beetle.MessageQueue;
import org.chobit.spring.beetle.model.Message;

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
