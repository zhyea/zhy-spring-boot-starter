package org.chobit.spring.redisq.beetle.serialization;

/**
 * 反序列化接口
 *
 * @author robin
 * @since 2025/3/25 8:21
 */
public interface Deserializer {

	/**
	 * 反序列化
	 *
	 * @param payload 序列化后的消息体字符串
	 * @param clazz   消息类型
	 * @param <T>     消息体类型
	 * @return 消息对象
	 */
	<T> T deserialize(String payload, Class<T> clazz);

}
