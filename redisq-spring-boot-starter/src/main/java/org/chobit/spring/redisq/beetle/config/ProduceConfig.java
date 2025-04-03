package org.chobit.spring.redisq.beetle.config;

import org.chobit.spring.redisq.beetle.serialization.JacksonSerializer;
import org.chobit.spring.redisq.beetle.serialization.Serializer;

/**
 * 生产配置
 *
 * @author robin
 * @since 2025/3/25 23:16
 */
public class ProduceConfig {


    /**
     * 要写入的topic
     */
    private String topic;

    /**
     * 消息序列化器
     */
    private Class<? extends Serializer> serializer = JacksonSerializer.class;

    /**
     * 最大重试次数
     */
    private Integer maxRetry = 3;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Class<? extends Serializer> getSerializer() {
        return serializer;
    }

    public void setSerializer(Class<? extends Serializer> serializer) {
        this.serializer = serializer;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }
}
