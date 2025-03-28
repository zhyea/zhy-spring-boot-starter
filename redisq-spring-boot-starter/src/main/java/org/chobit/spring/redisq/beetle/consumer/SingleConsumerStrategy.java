package org.chobit.spring.redisq.beetle.consumer;

/**
 * 单线程消费策略
 *
 * @author robin
 * @since 2025/3/21 23:12
 */
public class SingleConsumerStrategy extends MultiConsumerStrategy {


	public SingleConsumerStrategy() {
		super(1);
	}

}
