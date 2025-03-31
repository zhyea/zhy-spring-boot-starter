package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.BeetleQueue;
import org.chobit.spring.redisq.beetle.config.ConsumeConfig;
import org.chobit.spring.redisq.beetle.consumer.MessageConsumer;
import org.chobit.spring.redisq.beetle.producer.MessageProducer;
import org.chobit.spring.redisq.beetle.producer.MessageSender;
import org.chobit.spring.redisq.beetle.producer.Sender;
import org.chobit.spring.redisq.beetle.queue.DefaultQueueStrategy;
import org.chobit.spring.redisq.beetle.queue.QueueStrategy;
import org.chobit.spring.redisq.beetle.serialization.Serializer;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RedisQ启动器
 *
 * @author robin
 * @since 2025/3/30 22:16
 */
public class RedisQContext {


	private final BeetleProperties properties;
	private final RedisTemplate<String, String> redisTemplate;


	private final RedisClient redisClient;
	private final RedisOperator redisOperator;
	private final QueueStrategy queueStrategy;

	private final Map<String, Set<String>> topicConsumerIdMap;
	private final Map<String, BeetleQueue> topicQueueMap;
	private final Map<String, BeetleQueue> consumerIdQueueMap;

	private final MessageProducer producer;
	private final Map<String, MessageConsumer> consumers;


	public RedisQContext(BeetleProperties properties,
	                     RedisTemplate<String, String> redisTemplate) throws Exception {
		this.properties = properties;
		this.redisTemplate = redisTemplate;

		this.redisClient = new RedisClientImpl(redisTemplate);
		this.redisOperator = new RedisOperator(redisClient);
		this.queueStrategy = new DefaultQueueStrategy(redisOperator, properties.getDequeueTimeout());

		this.topicConsumerIdMap = buildTopicConsumerIdMap();
		this.topicQueueMap = buildQueues();
		this.consumerIdQueueMap = mapConsumeQueue();


		this.producer = buildProducer();
		this.consumers = buildConsumers();

	}


	/**
	 * 构建topic和consumerId的映射关系
	 *
	 * @return topic和consumerId的映射关系
	 */
	private Map<String, Set<String>> buildTopicConsumerIdMap() {
		assert null != this.properties.getConsumer();

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
	private Map<String, BeetleQueue> buildQueues() {
		assert null != this.topicConsumerIdMap;

		final Map<String, BeetleQueue> result = new HashMap<>(this.topicConsumerIdMap.size());

		this.topicConsumerIdMap.forEach((topic, configs) -> {
			result.put(topic, new RedisBeetleQueue(topic, queueStrategy, redisOperator, new ArrayList<>(configs)));
		});

		return result;
	}


	/**
	 * 构建消费ID和队列的映射关系
	 *
	 * @return 消费ID和队列的映射关系
	 */
	private Map<String, BeetleQueue> mapConsumeQueue() {
		assert null != this.topicQueueMap;
		assert null != this.topicConsumerIdMap;

		Map<String, BeetleQueue> result = new HashMap<>(this.topicConsumerIdMap.size());

		this.topicQueueMap.forEach((topic, queue) -> {
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
		Serializer serializer = properties.getProducer().getSerializer().newInstance();
		Map<String, Sender> senders = this.topicQueueMap.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey,
						e -> new MessageSender(e.getValue(), serializer, properties.getProducer())));
		return new MessageProducer(senders);
	}


	private Map<String, MessageConsumer> buildConsumers() {
		assert null != this.consumerIdQueueMap;

		Map<String, MessageConsumer> result = new HashMap<>(this.consumerIdQueueMap.size());
		this.consumerIdQueueMap.forEach((consumerId, queue) -> {
			result.put(consumerId, new MessageConsumer(consumerId, queue, properties.getConsumer().get(consumerId)));
		});
	}

}
