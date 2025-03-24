package org.chobit.spring.beetle.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 基于jackson实现的消息体序列化类
 *
 * @author robin
 * @since 2025/3/19 23:20
 */
public class JacksonPayloadSerializer implements PayloadSerializer {


	private final ObjectMapper mapper = new ObjectMapper();


	@Override
	public <T> String serialize(T payload) throws SerializationException {
		try {
			return mapper.writeValueAsString(payload);
		} catch (JsonProcessingException e) {
			throw new SerializationException("Could not serialize object using Jackson.", e);
		}
	}


	@Override
	public <T> T deserialize(String payload, Class<T> payloadType) throws SerializationException {
		if (null == payload) {
			return null;
		}

		try {
			return mapper.readValue(payload, payloadType);
		} catch (JsonProcessingException e) {
			throw new SerializationException("Could not deserialize object using Jackson.", e);
		}
	}
}
