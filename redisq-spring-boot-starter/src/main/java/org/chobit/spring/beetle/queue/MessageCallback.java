package org.chobit.spring.beetle.queue;


/**
 * 用于消息出队后被处理前调用
 *
 * @author robin
 */
public interface MessageCallback {


	/**
	 * 处理消息
	 *
	 * @param messageId 消息ID
	 */
	void handle(String messageId);

}
