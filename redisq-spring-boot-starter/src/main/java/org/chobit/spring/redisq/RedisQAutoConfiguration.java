package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redisq 自动配置
 *
 * @author robin
 * @since 2025/3/30 16:33
 */
@ConditionalOnBean(name = "redisqConnectionFactory")
@ConditionalOnClass(RedisConnectionFactory.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(BeetleProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RedisQAutoConfiguration {


	/**
	 * redisq 上下文
	 *
	 * @param connFactory connFactory
	 * @param properties  properties
	 * @return RedisQContext实例
	 * @throws Exception 异常
	 */
	@DependsOn("redisqConnectionFactory")
	@ConditionalOnBean(name = "redisqConnectionFactory")
	@Bean
	public RedisQContext redisqContext(@Qualifier("redisqConnectionFactory") RedisConnectionFactory connFactory,
	                                   BeetleProperties properties) throws Exception {
		StringRedisTemplate redisTemplate = new StringRedisTemplate(connFactory);
		return new RedisQContext(properties, redisTemplate);
	}


	/**
	 * 消息生产者
	 *
	 * @param context redisq 上下文
	 * @return MessageProducer实例
	 */
	@Conditional(OnNonEmptyProducerCondition.class)
	@Bean
	public MessageProducer messageProducer(RedisQContext context) {
		return context.getProducer();
	}


}
