package org.chobit.spring.redisq;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis客户端
 *
 * @author robin
 * @since 2025/3/25 23:23
 */
public interface RedisClient {


	/**
	 * 设置过期时间
	 *
	 * @param key               缓存key
	 * @param expirationTimeout 过期时间
	 * @param unit              时间单位
	 */
	void expire(String key, long expirationTimeout, TimeUnit unit);


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


	/**
	 * 移除并返回列表的第一个元素
	 *
	 * @param key            key
	 * @param timeoutSeconds 超时时间
	 * @return 移除的元素
	 */
	String leftPop(String key, long timeoutSeconds);


	/**
	 * 添加元素到set集合中
	 *
	 * @param key    key
	 * @param values 要添加的元素
	 */
	void sAdd(String key, String... values);


	/**
	 * 根据key获取set集合中的所有元素
	 *
	 * @param key key
	 * @return set集合中的所有元素
	 */
	Set<String> sMembers(String key);


	/**
	 * 用于同时将多个 field-value (字段-值)对设置到hash中
	 *
	 * @param key hash key
	 * @param map hash field/value集合
	 */
	void hmSet(String key, Map<String, String> map);


	/**
	 * 获取hash key对应的所有field/value集合
	 *
	 * @param key hash key
	 * @return key对应的所有field/value集合
	 */
	Map<String, String> hGetAll(String key);

}
