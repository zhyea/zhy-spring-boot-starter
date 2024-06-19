package org.chobit.spring.trace;


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
public class TraceClewAutoConfiguration {


    @Bean
    public TraceClewStarter traceClewStarter() {
        return new TraceClewStarter();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TraceClewInterceptor traceClewInterceptor() {
        return new TraceClewInterceptor();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TraceClewPointcutSourceAdvisor traceClewAdvisor(TraceClewInterceptor traceClewInterceptor) {
        TraceClewPointcutSourceAdvisor advisor = new TraceClewPointcutSourceAdvisor();
        advisor.setAdvice(traceClewInterceptor);
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE);
        return advisor;
    }

}
