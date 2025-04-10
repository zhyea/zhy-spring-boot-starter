package org.chobit.spring.redisq.beetle.producer;


import org.chobit.spring.redisq.beetle.Message;

/**
 * 生产消息回调
 *
 * @author robin
 * @since 2025/3/25 8:34
 */
public interface ProduceCallback {


	/**
	 * 消息生产完成回调
	 *
	 * @param message 消息
	 * @param e       异常
	 */
	void onCompletion(Message message, Exception e);

}
