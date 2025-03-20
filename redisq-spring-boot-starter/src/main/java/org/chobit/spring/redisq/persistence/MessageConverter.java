package org.chobit.spring.redisq.persistence;

import org.chobit.commons.utils.LocalDateKit;
import org.chobit.spring.redisq.model.Message;
import org.chobit.spring.redisq.serialization.PayloadSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息转换器
 *
 * @author robin
 * @since 2025/3/19 23:31
 */
class MessageConverter {


	private static final String PAYLOAD_HASH_KEY = "payload";
	private static final String ID_HASH_KEY = "id";
	private static final String CREATION_HASH_KEY = "creation";
	private static final String TTL_HASH_KEY = "ttl";
	private static final String RETRY_COUNT = "retries";


	static <T> Map<String, String> toMap(Message<T> message, PayloadSerializer payloadSerializer) {
		String payload = payloadSerializer.serialize(message.getBody());

		Map<String, String> result = new HashMap<>(8);
		result.put(PAYLOAD_HASH_KEY, payload);
		result.put(ID_HASH_KEY, message.getId());
		result.put(CREATION_HASH_KEY, Long.toString(LocalDateKit.toEpochMilli(message.getCreation())));
		result.put(RETRY_COUNT, Integer.toString(message.getRetryCount()));

		if (null != message.getTimeToLiveSeconds()) {
			result.put(TTL_HASH_KEY, String.valueOf(message.getTimeToLiveSeconds()));
		}

		return result;
	}


	static <T> Message<T> toMessage(Map<String, String> data, PayloadSerializer payloadSerializer, Class<T> payloadType) {
		if (null == data || data.isEmpty()) {
			return null;
		}

		T payload = payloadSerializer.deserialize(data.get(PAYLOAD_HASH_KEY), payloadType);
		Message<T> message = new Message<>();
		message.setId(data.get(ID_HASH_KEY));
		message.setCreation(LocalDateKit.fromEpochMilli(Long.parseLong(data.get(CREATION_HASH_KEY))));
		message.setTimeToLiveSeconds(Long.parseLong(data.get(TTL_HASH_KEY)));
		message.setBody(payload);

		String retryCount = data.get(RETRY_COUNT);
		if (null != retryCount) {
			message.setRetryCount(Integer.parseInt(retryCount));
		}

		return message;
	}


}
