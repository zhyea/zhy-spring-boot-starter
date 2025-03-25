package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.serialization.Deserializer;
import org.chobit.spring.redisq.beetle.serialization.Serializer;

/**
 * 消息队列配置
 *
 * @author robin
 * @since 2025/3/25 7:30
 */
public class BeetleProperties {


	/**
	 * 消息保留时间，单位秒，默认为1天
	 */
	private Long retentionSeconds;


	/**
	 * 消息体序列化类
	 */
	private Serializer serializer;


	/**
	 * 消息体反序列化类
	 */
	private Deserializer deserializer;


}
