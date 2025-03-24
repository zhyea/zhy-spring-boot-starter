package org.chobit.spring.beetle.consumer;

import org.springframework.data.redis.RedisConnectionFailureException;

/**
 * redis连接失败处理器
 *
 * @author robin
 * @since 2025/3/21 23:14
 */
public interface ConnectionFailureHandler {


	void onFail(RedisConnectionFailureException e);

}
