package org.chobit.spring.redisq.beetle.constants;

/**
 * 重试策略
 *
 * @author robin
 * @since 2025/3/30 12:04
 */
public enum RetryStrategy {


	/**
	 * 不重试
	 */
	NO,


	/**
	 * 多次重试
	 */
	MULTI,
	;

}
