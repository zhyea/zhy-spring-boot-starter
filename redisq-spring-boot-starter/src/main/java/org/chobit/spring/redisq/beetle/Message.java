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
	private int maxRetryCount;

	/**
	 * 消息消费重试次数
	 */
	private int leftRetryCount;

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

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	public int getLeftRetryCount() {
		return leftRetryCount;
	}

	public void setLeftRetryCount(int leftRetryCount) {
		this.leftRetryCount = leftRetryCount;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id='" + id + '\'' +
				", offset=" + offset +
				", createTime=" + createTime +
				", ttlSeconds=" + ttlSeconds +
				", maxRetryCount=" + maxRetryCount +
				", leftRetryCount=" + leftRetryCount +
				", body='" + body + '\'' +
				'}';
	}
}
