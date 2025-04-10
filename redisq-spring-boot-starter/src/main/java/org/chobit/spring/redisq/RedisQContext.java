package org.chobit.spring.redisq;

import org.chobit.commons.concurrent.Threads;
import org.chobit.commons.utils.Collections2;
import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.config.ConsumeConfig;
import org.chobit.spring.redisq.beetle.config.ProduceConfig;
import org.chobit.spring.redisq.beetle.constants.RetryStrategyEnum;
import org.chobit.spring.redisq.beetle.consumer.ConsumeStrategy;
import org.chobit.spring.redisq.beetle.consumer.IProcessor;
import org.chobit.spring.redisq.beetle.consumer.MessageConsumer;
import org.chobit.spring.redisq.beetle.consumer.ThreadingConsumeStrategy;
import org.chobit.spring.redisq.beetle.consumer.retry.MaxRetryStrategy;
import org.chobit.spring.redisq.beetle.consumer.retry.MessageRetryStrategy;
import org.chobit.spring.redisq.beetle.consumer.retry.NoRetryStrategy;
import org.chobit.spring.redisq.beetle.producer.MessageProducer;
import org.chobit.spring.redisq.beetle.producer.MessageSender;
import org.chobit.spring.redisq.beetle.producer.Sender;
import org.chobit.spring.redisq.beetle.queue.DefaultQueueStrategy;
import org.chobit.spring.redisq.beetle.queue.QueueStrategy;
import org.chobit.spring.redisq.beetle.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.chobit.commons.utils.StrKit.isBlank;

/**
 * RedisQ启动器
 *
 * @author robin
 * @since 2025/3/30 22:16
 */
public class RedisQContext implements SmartInitializingSingleton, DisposableBean, BeanPostProcessor, ApplicationContextAware, SmartLifecycle, Ordered {


    private static final Logger logger = LoggerFactory.getLogger(RedisQContext.class);


    private final BeetleProperties properties;

    private final RedisOperator redisOperator;
    private final QueueStrategy queueStrategy;

    private final Map<String, BeetleQueue> topicProducerQueueMap;

    private final Map<String, Set<String>> topicConsumerIdMap;
    private final Map<String, BeetleQueue> topicConsumerQueueMap;
    private final Map<String, BeetleQueue> consumerIdQueueMap;

    private final MessageProducer producer;
    private final Map<String, MessageConsumer> consumers;
    private final Map<String, Set<String>> expectedProcessors = new HashMap<>(4);

    private final AtomicBoolean startConsumerSignal = new AtomicBoolean(false);
    private final AtomicBoolean destroySignal = new AtomicBoolean(false);
    private final AtomicBoolean refreshEventReceived = new AtomicBoolean(false);


    public RedisQContext(BeetleProperties properties,
                         StringRedisTemplate redisTemplate) throws Exception {
        this.properties = properties;

        RedisClient redisClient = new RedisClientImpl(redisTemplate);
        this.redisOperator = new RedisOperator(redisClient);
        this.queueStrategy = new DefaultQueueStrategy(redisOperator);

        this.topicConsumerIdMap = buildTopicConsumerIdMap();
        this.topicConsumerQueueMap = buildTopicConsumerQueues();
        this.consumerIdQueueMap = mapConsumeQueue();
        this.consumers = buildConsumers();

        this.topicProducerQueueMap = buildTopicProducerQueues();
        this.producer = buildProducer();
    }


    /**
     * 构建topic和consumerId的映射关系
     *
     * @return topic和consumerId的映射关系
     */
    private Map<String, Set<String>> buildTopicConsumerIdMap() {
        if (Collections2.isEmpty(this.properties.getConsumer())) {
            return new HashMap<>(0);
        }

        Map<String, Set<String>> result = new HashMap<>();

        for (ConsumeConfig cfg : this.properties.getConsumer()) {
            Set<String> consumerIds = result.computeIfAbsent(cfg.getTopic(), k -> new HashSet<>());
            consumerIds.add(cfg.getConsumerId());
        }

        return result;
    }


    /**
     * 构建队列
     *
     * @return topic 和 消费队列的映射
     */
    private Map<String, BeetleQueue> buildTopicConsumerQueues() {
        if (Collections2.isEmpty(this.topicConsumerIdMap)) {
            new HashMap<>(0);
        }

        final Map<String, BeetleQueue> result = new HashMap<>(this.topicConsumerIdMap.size());

        this.topicConsumerIdMap.forEach((topic, consumerIds) -> {
            Set<String> tmp = redisOperator.getRegisteredConsumerIds(topic);
            if (Collections2.isEmpty(tmp)) {
                tmp = new HashSet<>(consumerIds);
            } else {
                tmp.addAll(consumerIds);
            }
            BeetleQueue queue = new RedisBeetleQueue(topic, queueStrategy, redisOperator, new ArrayList<>(tmp));
            result.put(topic, queue);
        });

        return result;
    }


    /**
     * 构建消费ID和队列的映射关系
     *
     * @return 消费ID和队列的映射关系
     */
    private Map<String, BeetleQueue> mapConsumeQueue() {
        if (Collections2.isEmpty(this.topicConsumerQueueMap)
                || Collections2.isEmpty(this.topicConsumerIdMap)) {
            return new HashMap<>(0);
        }

        Map<String, BeetleQueue> result = new HashMap<>(this.topicConsumerIdMap.size());

        this.topicConsumerQueueMap.forEach((topic, queue) -> {
            Set<String> consumerIds = this.topicConsumerIdMap.get(topic);
            assert null != consumerIds && !consumerIds.isEmpty();

            consumerIds.forEach(e -> result.put(e, queue));
        });

        return result;
    }


    /**
     * 构建消息消费者
     *
     * @return 消息消费者
     */
    private Map<String, MessageConsumer> buildConsumers() {
        if (Collections2.isEmpty(this.consumerIdQueueMap)) {
            return new HashMap<>(0);
        }

        Map<String, MessageConsumer> result = new HashMap<>(this.consumerIdQueueMap.size());
        for (ConsumeConfig cfg : this.properties.getConsumer()) {
            MessageRetryStrategy retryStrategy = buildRetryStrategy(cfg.getRetry());
            ConsumeStrategy consumeStrategy = new ThreadingConsumeStrategy(cfg.getConsumeThreads());
            String consumerId = cfg.getConsumerId();
            if (isBlank(consumerId)) {
                throw new RedisQConfigException("consumer id is empty!");
            }

            BeetleQueue queue = this.consumerIdQueueMap.get(consumerId);

            result.put(consumerId, new MessageConsumer(consumerId, queue, consumeStrategy, retryStrategy));
            if (isBlank(cfg.getProcessor())) {
                throw new RedisQConfigException("processor is empty for consumer: " + consumerId);
            }

            Set<String> set = this.expectedProcessors
                    .computeIfAbsent(cfg.getProcessor(), k -> new HashSet<>(4));
            set.add(consumerId);
        }

        return result;
    }


    /**
     * 根据配置构建重试策略
     *
     * @param retryStrategy 重试策略
     * @return 重试策略
     */
    private MessageRetryStrategy buildRetryStrategy(RetryStrategyEnum retryStrategy) {
        if (retryStrategy == RetryStrategyEnum.NO || null == retryStrategy) {
            return NoRetryStrategy.getInstance();
        }

        return new MaxRetryStrategy();
    }


    public MessageProducer getProducer() {
        return producer;
    }


    @Override
    public void destroy() throws Exception {
        destroySignal.set(true);

    }


    @Override
    public void afterSingletonsInstantiated() {
        this.startConsumer();
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {
        consumers.values().forEach(MessageConsumer::stop);
        this.startConsumerSignal.set(false);
    }

    @Override
    public boolean isRunning() {
        return startConsumerSignal.get();
    }

    private void startConsumer() {
        if (Collections2.isEmpty(this.consumerIdQueueMap)) {
            logger.info("No consumer found, RedisQ will not start consumer.");
            return;
        }

        Threads.newThread(() -> {
            while (!startConsumerSignal.get()) {
                if (destroySignal.get()) {
                    return;
                }

                if (refreshEventReceived.get()) {
                    logger.error("Cannot find processors: {}, RedisQ consumers start failed.", expectedProcessors);
                    return;
                }

                try {
                    TimeUnit.MICROSECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "redisq-consumer-auto-start-thread", false).start();

        consumers.values().forEach(MessageConsumer::start);
    }


    /**
     * 此处会在bean注入完成后，判断这个bean是否是需要的Processor实例
     *
     * @param bean     注入完成的bean
     * @param beanName bean名称
     * @return bean实例
     * @throws BeansException 出错时会抛出
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IProcessor) {
            addProcessor(beanName, (IProcessor) bean);
        }
        return bean;
    }


    private void addProcessor(String beanName, IProcessor processor) {
        if (this.expectedProcessors.containsKey(beanName)) {
            this.addProcessor0(beanName, processor);
        } else if (this.expectedProcessors.containsKey(processor.getClass().getName())) {
            this.addProcessor0(processor.getClass().getName(), processor);
        }

        if (expectedProcessors.isEmpty()) {
            this.startConsumerSignal.set(true);
        }
    }


    private void addProcessor0(String beanName, IProcessor processor) {
        Set<String> consumerIds = this.expectedProcessors.remove(beanName);
        if (Collections2.isEmpty(consumerIds)) {
            return;
        }
        for (String consumerId : consumerIds) {
            MessageConsumer consumer = consumers.get(consumerId);
            if (null != consumer) {
                consumer.setProcessor(processor);
            }
        }
    }


    /**
     * 构建topic和消息队列的映射关系
     *
     * @return topic和消息队列的映射
     */
    private Map<String, BeetleQueue> buildTopicProducerQueues() {
        List<ProduceConfig> configs = this.properties.getProducer();
        if (Collections2.isEmpty(configs)) {
            return new HashMap<>(0);
        }

        Map<String, BeetleQueue> result = new HashMap<>(configs.size());

        for (ProduceConfig cfg : configs) {
            String topic = cfg.getTopic();

            Set<String> consumerIds = redisOperator.getRegisteredConsumerIds(topic);
            if (Collections2.isEmpty(consumerIds)) {
                consumerIds = new HashSet<>(8);
            }
            Set<String> tmp = topicConsumerIdMap.getOrDefault(topic, new HashSet<>(0));
            consumerIds.addAll(tmp);

            BeetleQueue queue = new RedisBeetleQueue(topic, queueStrategy, redisOperator, new ArrayList<>(consumerIds));
            result.put(cfg.getTopic(), queue);
        }
        return result;
    }


    /**
     * 构建消息生产者
     *
     * @return 消息生产者
     * @throws Exception 异常信息
     */
    private MessageProducer buildProducer() throws Exception {
        if (Collections2.isEmpty(this.properties.getProducer())) {
            return null;
        }

        Map<String, Sender> senders = new HashMap<>(this.properties.getProducer().size());

        for (ProduceConfig cfg : this.properties.getProducer()) {
            Serializer serializer = cfg.getSerializer().newInstance();
            BeetleQueue queue = topicProducerQueueMap.get(cfg.getTopic());
            Integer maxRetryCount = cfg.getMaxRetry();
            Long ttlSeconds = this.properties.getTtlSeconds();
            MessageSender sender = new MessageSender(queue, serializer, ttlSeconds, maxRetryCount);

            senders.put(cfg.getTopic(), sender);
        }

        return new MessageProducer(senders);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) applicationContext).addApplicationListener(new ContextRefreshListener());
        }
    }


    private void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived.set(true);
    }

    /**
     * ApplicationListener endpoint that receives events from this servlet's WebApplicationContext
     * only, delegating to {@code onApplicationEvent} on the FrameworkServlet instance.
     */
    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            RedisQContext.this.onApplicationEvent(event);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
}
