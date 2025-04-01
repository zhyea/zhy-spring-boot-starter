package org.chobit.spring.redisq.beetle.consumer;

import org.chobit.spring.redisq.beetle.Message;

/**
 * 消息处理接口
 *
 * @author robin
 * @since 2025/3/29 9:30
 */
public interface IProcessor {


	/**
	 * 消息处理
	 *
	 * @param message 消息
	 */
	void process(Message message);

}
