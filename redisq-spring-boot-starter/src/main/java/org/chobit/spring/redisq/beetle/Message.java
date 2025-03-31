package org.chobit.spring.redisq.beetle;

/**
 * 队列消息
 *
 * @author robin
 * @since 2025/3/25 7:19
 */
public class Message {


	/**
	 * 消息ID
	 */
	private String id;


	private Long offset;


	/**
	 * 消息创建时间
	 */
	private long createTime;


	/**
	 * 消息剩余有效时间，单位秒
	 */
	private Long ttlSeconds;


	/**
	 * 消息消费重试次数
	 */
	private int retryCount;


	/**
	 * 消息体
	 */
	private String body;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
