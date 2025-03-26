package org.chobit.spring.redisq.tmp.model;


import java.time.LocalDateTime;

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


	private LocalDateTime creation;

	/**
	 * 消息留存时间，单位秒
	 */
	private Long timeToLiveSeconds;

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

	public LocalDateTime getCreation() {
		return creation;
	}

	public void setCreation(LocalDateTime creation) {
		this.creation = creation;
	}

	public Long getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}

	public void setTimeToLiveSeconds(Long timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
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

	public void setPayload(T body) {
		this.body = body;
	}
}
