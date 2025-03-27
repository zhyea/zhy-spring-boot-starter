package org.chobit.spring.redisq.beetle.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 依赖jackson实现的序列化器
 *
 * @author robin
 * @since 2025/3/27 8:29
 */
public class JacksonCodec implements Serializer, Deserializer {

	private final ObjectMapper mapper = new ObjectMapper();


	@Override
	public String serialize(Object payload) {
		try {
			return mapper.writeValueAsString(payload);
		} catch (IOException e) {
			throw new SerializationException("Could not serialize object using Jackson.", e);
		}
	}


	@Override
	public <T> T deserialize(String payload, Class<T> type) throws SerializationException {
		if (payload == null) {
			return null;
		}

		try {
			return mapper.readValue(payload, type);
		} catch (IOException e) {
			throw new SerializationException("Could not serialize object using Jackson.", e);
		}
	}
}
