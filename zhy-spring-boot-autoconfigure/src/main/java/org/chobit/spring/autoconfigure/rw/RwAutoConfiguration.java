package org.chobit.spring.autoconfigure.rw;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 封装返回值自动配置类
 *
 * @author robin
 */
@ConditionalOnMissingBean(RwAutoConfiguration.class)
@ConditionalOnClass({ResponseBodyAdvice.class, HttpMessageConverter.class})
@ConditionalOnProperty(name = "rw.enabled", matchIfMissing = true)
@EnableConfigurationProperties(RwProperties.class)
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RwAutoConfiguration {



	@ConditionalOnMissingBean(ResponseWrapperAdvice.class)
	@Bean
	public ResponseWrapperAdvice responseWrapperAdvice(RwProperties rwProperties) {
		return new ResponseWrapperAdvice(rwProperties);
	}


	@ConditionalOnProperty(name = "rw.wrapExcept", matchIfMissing = true)
	@ConditionalOnMissingBean(ApiExceptionAdvisor.class)
	@Bean
	public ApiExceptionAdvisor apiExceptionAdvisor(RwProperties rwProperties){
		return new ApiExceptionAdvisor(rwProperties);
	}


}
