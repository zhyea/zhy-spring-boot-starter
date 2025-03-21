package org.chobit.spring.autoconfigure.trace;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * @author robin
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TraceClewProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class TraceClewAutoConfiguration {


    @Bean
    public TraceClewStarter traceClewStarter() {
        return new TraceClewStarter();
    }


    @ConditionalOnMissingBean(InheritableTraceHolder.class)
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public InheritableTraceHolder inheritableTraceHolder() {
        return new InheritableTraceHolder();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TraceClewInterceptor traceClewInterceptor(InheritableTraceHolder inheritableTraceHolder, TraceClewProperties properties) {
        return new TraceClewInterceptor(inheritableTraceHolder, properties);
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
