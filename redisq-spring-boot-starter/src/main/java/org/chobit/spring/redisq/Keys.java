package org.chobit.spring.redisq;

import static java.lang.String.format;

/**
 * 消息队列用到的各种key
 *
 * @author robin
 * @since 2025/3/26 7:53
 */
public final class Keys {

	/**
	 * 消息队列对应key模式
	 */
	private static final String PATTERN_QUEUE = "beetle.queue.%s";

	/**
	 * 消息ID对应key模式
	 */
	private static final String PATTERN_NEXT_MESSAGE_ID = "beetle.nextId.%s";


	/**
	 * 获取消息队列对应的key
	 *
	 * @param topicName topic 名称
	 * @return key
	 */
	public static String keyForQueue(String topicName) {
		return format(PATTERN_QUEUE, topicName);
	}


	/**
	 * 下一条消息ID对应的key
	 *
	 * @param topicName topic名称
	 * @return key
	 */
	public static String keyForNextId(String topicName) {
		return format(PATTERN_NEXT_MESSAGE_ID, topicName);
	}

}
