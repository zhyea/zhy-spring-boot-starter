package org.chobit.spring.redisq;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis客户端实现类
 *
 * @author robin
 * @since 2025/3/30 17:01
 */
public class RedisClientImpl implements RedisClient {


	private final StringRedisTemplate redisTemplate;


	public RedisClientImpl(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void expire(String key, long expirationTimeout, TimeUnit unit) {
		redisTemplate.expire(key, expirationTimeout, unit);
	}

	@Override
	public Long increment(String key) {
		return this.increment(key, 1L);
	}

	@Override
	public Long increment(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public void rightPush(String key, String value) {
		redisTemplate.opsForList().rightPush(key, value);
	}

	@Override
	public String leftPop(String key, long timeoutMillis) {
		return redisTemplate.opsForList().leftPop(key, timeoutMillis, TimeUnit.MILLISECONDS);
	}

	@Override
	public void sAdd(String key, String... values) {
		redisTemplate.opsForSet().add(key, values);
	}

	@Override
	public Set<String> sMembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	@Override
	public void hmSet(String key, Map<String, String> map) {
		redisTemplate.opsForHash().putAll(key, map);
	}

	@Override
	public Map<String, String> hGetAll(String key) {
		HashOperations<String, String, String> ops = redisTemplate.opsForHash();
		return ops.entries(key);
	}
}
