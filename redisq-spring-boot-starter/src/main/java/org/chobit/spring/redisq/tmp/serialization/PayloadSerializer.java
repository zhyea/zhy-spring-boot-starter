package org.chobit.spring.redisq.tmp.serialization;

import org.springframework.data.redis.serializer.SerializationException;

/**
 * 消息体序列化处理
 *
 * @author robin
 * @since 2025/3/19 23:10
 */
public interface PayloadSerializer {


	/**
	 * 执行消息体序列化
	 *
	 * @param payload 消息体
	 * @param <T>     消息体类型
	 * @return 消息体序列化后的字符串
	 * @throws SerializationException 序列化过程中出现的异常
	 */
	<T> String serialize(T payload) throws SerializationException;


	/**
	 * 执行消息体反序列化
	 *
	 * @param payload     消息体序列化后的字符串
	 * @param payloadType 消息体类型
	 * @param <T>         消息体类型
	 * @return 消息体
	 * @throws SerializationException 反序列化过程中出现的异常
	 */
	<T> T deserialize(String payload, Class<T> payloadType) throws SerializationException;
}
