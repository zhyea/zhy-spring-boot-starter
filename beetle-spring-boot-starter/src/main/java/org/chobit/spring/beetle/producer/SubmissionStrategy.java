package org.chobit.spring.beetle.producer;

import org.chobit.spring.beetle.MessageQueue;
import org.chobit.spring.beetle.model.Message;

/**
 * 消息提交策略
 *
 * @author robin
 * @since 2025/3/20 23:05
 */
public interface SubmissionStrategy {


	<T> void submit(MessageQueue queue, Message<T> message);


	<T> void submit(MessageQueue queue, Message<T> message, String consumer);

}
