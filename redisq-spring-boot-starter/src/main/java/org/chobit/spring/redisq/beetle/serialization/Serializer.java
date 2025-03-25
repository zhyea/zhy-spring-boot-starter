package org.chobit.spring.redisq.beetle.serialization;

/**
 * 消息体序列化
 *
 * @author robin
 * @since 2025/3/25 8:18
 */
public interface Serializer {


	/**
	 * 执行序列化
	 *
	 * @param payload 消息体
	 * @param <T>     消息体类型
	 * @return 序列化后的字符串
	 */
	<T> String serialize(T payload);


}
