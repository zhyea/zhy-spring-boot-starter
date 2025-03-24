package org.chobit.spring.beetle.producer;

import org.chobit.commons.utils.Collections2;
import org.chobit.spring.beetle.MessageQueue;
import org.chobit.spring.beetle.model.Message;
import org.chobit.spring.beetle.persistence.RedisOperator;

import java.util.Collection;

/**
 * @author robin
 * @since 2025/3/20 23:18
 */
public class MultiConsumerSubmissionStrategy extends SingleConsumerSubmissionStrategy{


	private final RedisOperator redisOperator;


	public MultiConsumerSubmissionStrategy(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}


	@Override
	public <T> void submit(MessageQueue queue, Message<T> message) {
		Collection<String> allConsumers = redisOperator.getRegisteredConsumers(queue.getQueueName());
		if(Collections2.isEmpty(allConsumers)){
			super.submit(queue, message);
		}else{
			queue.enqueue(message, allConsumers.toArray(new String[0]));
		}
	}
}
