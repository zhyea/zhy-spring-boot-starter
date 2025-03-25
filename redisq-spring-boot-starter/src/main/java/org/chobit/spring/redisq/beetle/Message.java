package org.chobit.spring.redisq.beetle;

import java.time.LocalDateTime;

/**
 * 队列消息
 *
 * @author robin
 * @since 2025/3/25 7:19
 */
public class Message {


	/**
	 * 消息ID
	 */
	private String id;


	/**
	 * 消息创建时间
	 */
	private LocalDateTime createTime;


	/**
	 * 消息剩余有效时间，单位秒
	 */
	private Long ttlSeconds;


	/**
	 * 消息消费重试次数
	 */
	private int retryCount;


	/**
	 * 消息体
	 */
	private String body;


}
