package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.Message;

import java.util.HashMap;
import java.util.Map;

import static org.chobit.commons.utils.StrKit.isNotBlank;

/**
 * 将消息转换为Map
 *
 * @author robin
 * @since 2025/3/26 8:28
 */
final class MessageConverter {


	private static final String FIELD_ID = "id";
	private static final String FIELD_CREATE_TIME = "ct";
	private static final String FIELD_TTL = "ttl";
	private static final String FIELD_RETRY_COUNT = "rc";
	private static final String FIELD_PAYLOAD = "body";


	/**
	 * 将消息转换为Map, 用于存储到Redis中
	 *
	 * @param message 消息
	 * @return Map
	 */
	static Map<String, String> toMap(Message message) {

		Map<String, String> result = new HashMap<>(8);
		result.put(FIELD_ID, message.getId());
		result.put(FIELD_CREATE_TIME, Long.toString(message.getCreateTime()));
		result.put(FIELD_RETRY_COUNT, Integer.toString(message.getRetryCount()));
		result.put(FIELD_PAYLOAD, message.getBody());

		if (null != message.getTtlSeconds()) {
			result.put(FIELD_TTL, message.getTtlSeconds().toString());
		}

		return result;
	}


	/**
	 * 将Map转换为消息
	 *
	 * @param data Map
	 * @return 消息
	 */
	static Message toMessage(Map<String, String> data) {
		if (null == data || data.isEmpty()) {
			return null;
		}

		Message message = new Message();
		message.setId(data.get(FIELD_ID));
		message.setCreateTime(Long.parseLong(data.get(FIELD_CREATE_TIME)));
		message.setRetryCount(Integer.parseInt(data.get(FIELD_RETRY_COUNT)));
		message.setBody(data.get(FIELD_PAYLOAD));

		String retryCount = data.get(FIELD_RETRY_COUNT);
		if (isNotBlank(retryCount)) {
			message.setRetryCount(Integer.parseInt(retryCount));
		}

		return message;
	}

}
