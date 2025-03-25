package org.chobit.spring.redisq.beetle.producer;

import org.chobit.spring.redisq.beetle.BeetleException;
import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.config.ProduceConfig;
import org.chobit.spring.redisq.beetle.serialization.Serializer;

import static java.lang.String.format;

/**
 * 默认生产者
 *
 * @author robin
 * @since 2025/3/25 22:25
 */
public class DefaultProducer implements Producer {


	private final BeetleQueue queue;
	private final Serializer serializer;
	private final ProduceConfig produceConfig;


	public DefaultProducer(BeetleQueue queue,
	                       Serializer serializer,
	                       ProduceConfig produceConfig) {
		this.queue = queue;
		this.serializer = serializer;
		this.produceConfig = produceConfig;
	}

	@Override
	public <T> void send(T payload) {
		if(null == payload){
			throw new BeetleException(format("payload is null, topic: [%s]", queue.topic()));
		}

		String body = serializer.serialize(payload);
		Message message = new Message();
		message.setBody(body);
		message.setTtlSeconds(produceConfig.getTtlSeconds());
		message.setCreateTime(System.currentTimeMillis());

		queue.enqueue(message);
	}


	@Override
	public <T> void send(T payload, ProduceCallback callback) {

	}
}
