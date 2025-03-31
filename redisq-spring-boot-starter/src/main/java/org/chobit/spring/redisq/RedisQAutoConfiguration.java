package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.queue.DefaultQueueStrategy;
import org.chobit.spring.redisq.beetle.queue.QueueStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redisq 自动配置
 *
 * @author robin
 * @since 2025/3/30 16:33
 */
@ConditionalOnClass(RedisTemplate.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(BeetleProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RedisQAutoConfiguration {


	@ConditionalOnBean(name = "redisqTemplate")
	@Bean
	public RedisClient redisClient(@Qualifier("redisqTemplate")
	                               RedisTemplate<String, String> redisTemplate) {
		return new RedisClientImpl(redisTemplate);
	}


	@Bean
	public RedisOperator redisOperator(RedisClient redisClient) {
		return new RedisOperator(redisClient);
	}


	@Bean
	public QueueStrategy queueStrategy(RedisOperator operator, BeetleProperties properties) {
		return new DefaultQueueStrategy(operator, properties.getDequeueTimeout());
	}


}
