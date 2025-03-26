package org.chobit.spring.redisq;

/**
 * Redis客户端
 *
 * @author robin
 * @since 2025/3/25 23:23
 */
public interface RedisClient {


	void set(String key, String value, long ttl);


	/**
	 * 递增, 步长为1
	 *
	 * @param key 键
	 * @return 增加后的值
	 */
	Long increment(String key);

	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return 增加后的值
	 */
	Long increment(String key, long delta);


	/**
	 * 将值添加到列表的尾部
	 *
	 * @param key   key
	 * @param value value
	 */
	void rightPush(String key, String value);


}
