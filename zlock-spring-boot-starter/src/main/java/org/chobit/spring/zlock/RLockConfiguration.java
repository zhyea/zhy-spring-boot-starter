package org.chobit.spring.zlock;

import org.chobit.spring.dlock.interceptor.AnnotationRLockOperationSource;
import org.chobit.spring.dlock.interceptor.BeanFactoryRLockOperationSourceAdvisor;
import org.chobit.spring.dlock.interceptor.RLockInterceptor;
import org.chobit.spring.dlock.interceptor.RLockOperationSource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import java.util.List;

import static jodd.util.StringUtil.isNotBlank;

/**
 * @author rui.zhang
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnClass({Redisson.class, RedissonClient.class, RedisProperties.class})
@EnableConfigurationProperties(RedisProperties.class)
public class RLockConfiguration {


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RedissonClient redissonClient(RedisProperties properties) {
        int timeout = 10000;
        if (null != properties.getTimeout()) {
            timeout = (int) properties.getTimeout().toMillis();
        }

        Config cfg = new Config();

        if (null != properties.getCluster()) {
            List<String> nodes = properties.getCluster().getNodes();
            ClusterServersConfig csc = cfg.useClusterServers()
                    .addNodeAddress(convert(nodes))
                    .setConnectTimeout(timeout);
            if (isNotBlank(properties.getPassword())) {
                csc.setPassword(properties.getPassword());
            }
        } else {
            String protocol = "redis://";
            if (properties.isSsl()) {
                protocol = "rediss://";
            }
            SingleServerConfig ssc = cfg.useSingleServer()
                    .setAddress(protocol + properties.getHost() + ":" + properties.getPort())
                    .setConnectTimeout(timeout)
                    .setDatabase(properties.getDatabase());
            if (isNotBlank(properties.getPassword())) {
                ssc.setPassword(properties.getPassword());
            }
        }

        return Redisson.create(cfg);
    }


    private String[] convert(List<String> nodes) {
        String[] result = new String[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(i);
            if (node.startsWith("redis://") || node.startsWith("rediss://")) {
                result[i] = node;
            } else {
                result[i] = "redis://" + node;
            }
        }
        return result;
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RLockOperationSource redLockOperationSource() {
        return new AnnotationRLockOperationSource();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RLockInterceptor redLockInterceptor(RLockOperationSource redLockOperationSource, RedissonClient redissonClient) {
        RLockInterceptor interceptor = new RLockInterceptor();
        interceptor.setRedLockOperationSource(redLockOperationSource);
        interceptor.setRedissonClient(redissonClient);
        return interceptor;
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryRLockOperationSourceAdvisor redLockAdvisor(RLockInterceptor redLockInterceptor,
                                                                 RLockOperationSource redLockOperationSource) {
        BeanFactoryRLockOperationSourceAdvisor advisor = new BeanFactoryRLockOperationSourceAdvisor();
        advisor.setRedLockOperationSource(redLockOperationSource);
        advisor.setAdvice(redLockInterceptor);
        return advisor;
    }


}
