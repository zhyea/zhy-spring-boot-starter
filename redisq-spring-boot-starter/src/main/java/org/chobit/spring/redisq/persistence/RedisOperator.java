package org.chobit.spring.redisq.persistence;

import org.chobit.spring.redisq.MessageQueue;
import org.chobit.spring.redisq.model.Message;
import org.chobit.spring.redisq.serialization.PayloadSerializer;
import org.springframework.data.redis.core.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.chobit.commons.utils.StrKit.isBlank;
import static org.chobit.spring.redisq.tools.KeyFactory.*;


/**
 * redis 操作类
 *
 * @author robin
 * @since 2021-03-20
 */
public class RedisOperator {


	private final RedisTemplate<String, String> redisTemplate;
	private final PayloadSerializer payloadSerializer;

	public RedisOperator(RedisTemplate<String, String> redisTemplate, PayloadSerializer payloadSerializer) {
		this.redisTemplate = redisTemplate;
		this.payloadSerializer = payloadSerializer;
	}


	public void ensureConsumerRegistered(String queueName, String consumerId) {
		String registerConsumerKey = keyForRegisteredConsumers(queueName);

		redisTemplate.boundSetOps(registerConsumerKey)
				.add(consumerId);
	}


	public Collection<String> getRegisteredConsumers(String queueName) {
		String registerConsumerKey = keyForRegisteredConsumers(queueName);

		return redisTemplate.boundSetOps(registerConsumerKey)
				.members();
	}


	public <T> void addMessage(String queueName, Message<T> message) {
		assert null != message;
		assert null != message.getTimeToLiveSeconds();

		String msgId = generateNextMessageId(queueName);
		message.setId(msgId);
		if (null == message.getCreation()) {
			message.setCreation(LocalDateTime.now());
		}

		this.saveMessage(queueName, message);
	}


	private <T> void saveMessage(String queueName, Message<T> message) {

		assert null != message;
		assert null != message.getTimeToLiveSeconds();
		assert null != message.getId();

		Map<String, String> map = MessageConverter.toMap(message, payloadSerializer);

		String messageKey = keyForMessage(queueName, message.getId());

		redisTemplate.opsForHash().putAll(messageKey, map);
		redisTemplate.expire(messageKey, message.getTimeToLiveSeconds(), TimeUnit.SECONDS);
	}


	private <T> Message<T> loadMessageById(String queueName, String messageId, Class<T> payloadType) {
		String messageKey = keyForMessage(queueName, messageId);

		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(messageKey);
		Map<String, String> messageData = ops.entries();

		return MessageConverter.toMessage(messageData, payloadSerializer, payloadType);
	}


	public String dequeueMessageFromHead(String queueName, String consumerId, long timeoutSeconds) {
		String queueKey = keyForConsumerSpecificQueue(queueName, consumerId);

		return redisTemplate.boundListOps(queueKey)
				.leftPop(timeoutSeconds, TimeUnit.SECONDS);
	}


	public <T> Message<T> peekNextMessageInQueue(String queueName, String consumerId, Class<T> payloadType) {
		String queueKey = keyForConsumerSpecificQueue(queueName, consumerId);

		String nextId = redisTemplate.boundListOps(queueKey)
				.index(0);
		if (nextId == null) {
			return null;
		}

		return this.loadMessageById(queueName, nextId, payloadType);
	}


	/**
	 * Peeks messages in the specified queue (for the specified consumer).
	 *
	 * @param rangeStart zero-based index of first item to retrieve
	 * @param rangeEnd   zero-based index of last item to retrieve
	 */
	private <T> List<Message<T>> peekMessagesInQueue(String queueName, String consumerId, long rangeStart, long rangeEnd, Class<T> payloadType) {

		String queueKey = keyForConsumerSpecificQueue(queueName, consumerId);

		List<String> messageIds = redisTemplate.boundListOps(queueKey)
				.range(rangeStart, rangeEnd);

		List<Message<T>> messages = new ArrayList<>(messageIds.size());
		for (String id : messageIds) {
			messages.add(loadMessageById(queueName, id, payloadType));
		}
		return messages;
	}


	/**
	 * Peeks messages in the specified queue (for the default consumer).
	 *
	 * @param rangeStart zero-based index of first item to retrieve
	 * @param rangeEnd   zero-based index of last item to retrieve
	 */
	public <T> List<Message<T>> peekMessagesInQueue(MessageQueue queue, long rangeStart, long rangeEnd, Class<T> payloadType) {
		return peekMessagesInQueue(queue.getTopicName(), queue.getDefaultConsumerId(), rangeStart, rangeEnd, payloadType);
	}

	public void emptyQueue(String queueName) {
		Collection<String> consumerIds = getRegisteredConsumers(queueName);

		for (String consumerId : consumerIds) {
			String queueKey = keyForConsumerSpecificQueue(queueName, consumerId);
			redisTemplate.delete(queueKey);
		}
	}

	private String generateNextMessageId(String queueName) {
		String keyOfNextId = keyForNextId(queueName);
		return String.valueOf(redisTemplate.opsForValue().increment(keyOfNextId, 1));
	}

	public Long getQueueSizeForConsumer(String queueName, String consumerId) {
		return redisTemplate.opsForList().size(keyForConsumerSpecificQueue(queueName, consumerId));
	}

	public void enqueueMessageAtTail(String queueName, String consumerId, String messageId) {
		if (isBlank(messageId)) {
			throw new IllegalArgumentException("Message must have been persisted before being enqueued.");
		}

		String key = keyForConsumerSpecificQueue(queueName, consumerId);

		redisTemplate.opsForList().rightPush(key, messageId);
	}

	public void enqueueMessageInSet(String queueName, String consumerId, String messageId) {
		if (isBlank(messageId)) {
			throw new IllegalArgumentException("Message must have been persisted before being enqueued.");
		}

		String key = keyForConsumerSpecificQueue(queueName, consumerId);

		redisTemplate.opsForSet().add(key, messageId);
	}

	public void notifyWaitersOnSet(String queueName, String consumerId) {
		String key = keyForConsumerSpecificQueueNotificationList(queueName, consumerId);

		redisTemplate.opsForList().rightPush(key, "x");
	}

	public void waitOnSet(String queueName, String consumerId, long timeoutSeconds) {
		String key = keyForConsumerSpecificQueueNotificationList(queueName, consumerId);

		redisTemplate.opsForList().leftPop(key, timeoutSeconds, TimeUnit.SECONDS);
	}

	public String randomPopFromSet(String queueName, String consumerId) {
		String key = keyForConsumerSpecificQueue(queueName, consumerId);

		BoundSetOperations<String, String> ops = redisTemplate.boundSetOps(key);
		return ops.pop();
	}

	public boolean tryObtainLockForQueue(String queueName, String consumerId, long expirationTimeout, TimeUnit unit) {
		BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(keyForConsumerSpecificQueueLock(queueName, consumerId));

		boolean lockAcquired = ops.setIfAbsent("1");
		if (lockAcquired) {
			ops.expire(expirationTimeout, unit);
			return true;
		}
		return false;
	}


	public void releaseLockForQueue(String queueName, String consumerId) {
		redisTemplate.delete(keyForConsumerSpecificQueueLock(queueName, consumerId));
	}


}
