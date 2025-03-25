package org.chobit.spring.redisq.beetle.persistence;

/**
 * Redis客户端
 *
 * @author robin
 * @since 2025/3/25 23:23
 */
public interface RedisClient {


	void set(String key, String value, long ttl);


}
