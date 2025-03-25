package org.chobit.spring.redisq.beetle.config;

/**
 * 生产配置
 *
 * @author robin
 * @since 2025/3/25 23:16
 */
public class ProduceConfig {


	Long ttlSeconds;


	public Long getTtlSeconds() {
		return ttlSeconds;
	}

	public void setTtlSeconds(Long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}
}
