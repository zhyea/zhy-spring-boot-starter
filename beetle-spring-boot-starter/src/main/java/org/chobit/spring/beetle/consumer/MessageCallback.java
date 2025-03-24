package org.chobit.spring.beetle.consumer;

/**
 * 消息回调接口
 *
 * @author robin
 * @since 2025/3/21 8:35
 */
public interface MessageCallback {


	void call(String messageId);

}
