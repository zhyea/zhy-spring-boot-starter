package org.chobit.spring.redisq.beetle;

/**
 * 消息队列数据存储抽象
 *
 * @author robin
 * @since 2025/3/25 7:13
 */
public interface BeetleQueue {


	/**
	 * 获取消息队列topic
	 *
	 * @return 消息队列topic
	 */
	String topic();


	/**
	 * 添加消息到消息队列中
	 *
	 * @param message 消息
	 */
	void enqueue(Message message);


}
