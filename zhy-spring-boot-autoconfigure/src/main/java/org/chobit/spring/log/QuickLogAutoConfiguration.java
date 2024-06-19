package org.chobit.spring.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * @author robin
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class QuickLogAutoConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(QuickLogAutoConfiguration.class);

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public QuickLogInterceptor quickLogInterceptor() {
        return new QuickLogInterceptor();
    }


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
