package org.chobit.spring.redisq.beetle.producer;

import org.chobit.spring.redisq.beetle.BeetleException;
import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.Message;
import org.chobit.spring.redisq.beetle.config.ProduceConfig;
import org.chobit.spring.redisq.beetle.serialization.Serializer;

import static java.lang.String.format;

/**
 * 消息发送者
 *
 * @author robin
 * @since 2025/3/30 21:51
 */
public class MessageSender implements Sender {

	private final BeetleQueue queue;
	private final Serializer serializer;
	private final ProduceConfig produceConfig;


	public MessageSender(BeetleQueue queue,
	                     Serializer serializer,
	                     ProduceConfig produceConfig) {
		assert null != queue;
		assert null != serializer;

		this.queue = queue;
		this.serializer = serializer;
		this.produceConfig = produceConfig;
	}


	@Override
	public <T> Message send(T payload) {
		if (null == payload) {
			throw new BeetleException(format("payload is null, cannot be sent, topic: [%s]", queue.topic()));
		}

		String body = serializer.serialize(payload);
		Message message = new Message();
		message.setBody(body);
		message.setTtlSeconds(produceConfig.getTtlSeconds());
		message.setCreateTime(System.currentTimeMillis());

		queue.enqueue(message);

		return message;
	}


	@Override
	public <T> void send(T payload, ProduceCallback callback) {
		Message message = null;
		try {
			message = this.send(payload);
		} catch (Exception e) {
			callback.onCompletion(null, e);
		}
		callback.onCompletion(message, null);
	}

}
