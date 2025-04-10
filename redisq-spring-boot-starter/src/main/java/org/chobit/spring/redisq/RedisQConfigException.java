package org.chobit.spring.redisq;

/**
 * RedisQ配置异常
 *
 * @author zhangrui
 * @since 2025/4/1
 */
public class RedisQConfigException extends RuntimeException {


	/**
	 * 构造函数
	 *
	 * @param message 异常信息
	 */
	public RedisQConfigException(String message) {
		super(message);
	}

}
