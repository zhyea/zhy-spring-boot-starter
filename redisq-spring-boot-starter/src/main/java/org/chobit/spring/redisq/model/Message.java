package org.chobit.spring.redisq.model;


import java.util.Calendar;

/**
 * 消息封装类
 *
 * @param <T> 消息体类型
 * @author robin
 */
public class Message<T> {

	/**
	 * 消息ID
	 */
	private String id;


	private Calendar creation;

	/**
	 * 消息留存时间，单位秒
	 */
	private Long ttlSeconds;

	/**
	 * 消息重新消费次数
	 */
	private int retryCount;

	/**
	 * 消息体
	 */
	private T body;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getCreation() {
		return creation;
	}

	public void setCreation(Calendar creation) {
		this.creation = creation;
	}

	public Long getTtlSeconds() {
		return ttlSeconds;
	}

	public void setTtlSeconds(Long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
