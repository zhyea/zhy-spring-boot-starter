package org.chobit.spring.redisq;

import org.chobit.commons.utils.Collections2;
import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.config.ConsumeConfig;
import org.chobit.spring.redisq.beetle.config.ProduceConfig;
import org.chobit.spring.redisq.beetle.constants.RetryStrategyType;
import org.chobit.spring.redisq.beetle.consumer.ConsumeStrategy;
import org.chobit.spring.redisq.beetle.consumer.MessageConsumer;
import org.chobit.spring.redisq.beetle.consumer.MessageProcessor;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RedisQ启动器
 *
 * @author robin
 * @since 2025/3/30 22:16
 */
public class RedisQContext
		implements SmartInitializingSingleton, DisposableBean, BeanPostProcessor, ApplicationContextAware {


	private final BeetleProperties properties;


	private final RedisOperator redisOperator;
	private final QueueStrategy queueStrategy;


	private final Map<String, BeetleQueue> topicProducerQueueMap;

	private final Map<String, Set<String>> topicConsumerIdMap;
	private final Map<String, BeetleQueue> topicConsumerQueueMap;
	private final Map<String, BeetleQueue> consumerIdQueueMap;

	private final MessageProducer producer;
	private final Map<String, MessageConsumer> consumers;
	private final Map<String, String> processors;


	public RedisQContext(BeetleProperties properties,
	                     RedisTemplate<String, String> redisTemplate) throws Exception {
		this.properties = properties;

		RedisClient redisClient = new RedisClientImpl(redisTemplate);
		this.redisOperator = new RedisOperator(redisClient);
		this.queueStrategy = new DefaultQueueStrategy(redisOperator, properties.getDequeueTimeout());

		this.topicProducerQueueMap = buildTopicProducerQueues();
		this.producer = buildProducer();

		this.topicConsumerIdMap = buildTopicConsumerIdMap();
		this.topicConsumerQueueMap = buildTopicConsumerQueues();
		this.consumerIdQueueMap = mapConsumeQueue();
		this.processors = new HashMap<>(4);
		this.consumers = buildConsumers();
	}


	private Map<String, BeetleQueue> buildTopicProducerQueues() {
		List<ProduceConfig> configs = this.properties.getProducer();
		if (Collections2.isEmpty(configs)) {
			return new HashMap<>(0);
		}

		Map<String, BeetleQueue> result = new HashMap<>(configs.size());

		for (ProduceConfig cfg : configs) {
			String topic = cfg.getTopic();
			List<String> consumerIds = new ArrayList<>(redisOperator.getRegisteredConsumerIds(topic));
			BeetleQueue queue = new RedisBeetleQueue(topic, queueStrategy, redisOperator, consumerIds);
			result.put(cfg.getTopic(), queue);
		}
		return result;
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

		this.topicConsumerIdMap.forEach((topic, configs) -> {
			BeetleQueue queue = new RedisBeetleQueue(topic, queueStrategy, redisOperator, new ArrayList<>(configs));
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
			MessageSender sender = new MessageSender(queue, serializer, cfg);

			senders.put(cfg.getTopic(), sender);
		}

		return new MessageProducer(senders);
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
			BeetleQueue queue = this.consumerIdQueueMap.get(consumerId);

			result.put(consumerId, new MessageConsumer(consumerId, queue, consumeStrategy, retryStrategy));
			this.processors.put(consumerId, cfg.getProcessor());
		}

		return result;
	}


	/**
	 * 根据配置构建重试策略
	 *
	 * @param config 重试配置
	 * @return 重试策略
	 */
	private MessageRetryStrategy buildRetryStrategy(ConsumeConfig.ConsumeRetryConfig config) {
		if (config.getType() == RetryStrategyType.NO) {
			return NoRetryStrategy.getInstance();
		}

		assert config.getMax() > 0;

		return new MaxRetryStrategy(config.getMax());
	}


	public MessageProducer getProducer() {
		return producer;
	}

	@Override
	public void destroy() throws Exception {
		consumers.values().forEach(MessageConsumer::stop);
	}

	@Override
	public void afterSingletonsInstantiated() {
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
		if (bean instanceof MessageProcessor) {
			addProcessor(beanName, bean);
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}
}
