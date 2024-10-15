package org.chobit.spring.autoconfigure.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * @author robin
 */
@ConditionalOnMissingBean(QuickLogAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class QuickLogAutoConfiguration {


	private static final Logger logger = LoggerFactory.getLogger(QuickLogAutoConfiguration.class);


	@ConditionalOnMissingBean(QuickLogInterceptor.class)
	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public QuickLogInterceptor quickLogInterceptor() {
		return new QuickLogInterceptor();
	}


	@ConditionalOnMissingBean(QuickLogPointcutSourceAdvisor.class)
	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public QuickLogPointcutSourceAdvisor quickLogSource(QuickLogInterceptor quickLogInterceptor) {
		QuickLogPointcutSourceAdvisor advisor = new QuickLogPointcutSourceAdvisor();
		advisor.setAdvice(quickLogInterceptor);
		advisor.setOrder(Ordered.LOWEST_PRECEDENCE - 16);

		logger.debug("QuickLog has been enabled!");

		return advisor;
	}

}
