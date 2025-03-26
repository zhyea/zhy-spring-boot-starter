package org.chobit.spring.redisq.tmp.consumer;

/**
 * 单线程消费策略
 *
 * @author robin
 * @since 2025/3/21 23:12
 */
public class SingleThreadConsumeStrategy extends MultiThreadConsumeStrategy {


	public SingleThreadConsumeStrategy() {
		super(1);
	}

}
