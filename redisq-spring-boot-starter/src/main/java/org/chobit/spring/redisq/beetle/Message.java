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

	/**
	 * 消息偏移量
	 */
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


	private int retryCount;

	/**
	 * 消息体
	 */
	private String body;


	/**
	 * 获取消息ID
	 *
	 * @return 消息ID
	 */
	public String getId() {
		return id;
	}


	/**
	 * 设置消息ID
	 *
	 * @param id 消息ID
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * 获取消息创建时间
	 *
	 * @return 消息创建时间
	 */
	public long getCreateTime() {
		return createTime;
	}


	/**
	 * 设置消息创建时间
	 *
	 * @param createTime 消息创建时间
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	/**
	 * 获取消息剩余有效时间，单位秒
	 *
	 * @return 消息剩余有效时间，单位秒
	 */
	public Long getTtlSeconds() {
		return ttlSeconds;
	}


	/**
	 * 设置消息剩余有效时间，单位秒
	 *
	 * @param ttlSeconds 消息剩余有效时间，单位秒
	 */
	public void setTtlSeconds(Long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}


	/**
	 * 获取消息重试次数
	 *
	 * @return 消息重试次数
	 */
	public int getMaxRetryCount() {
		return maxRetryCount;
	}


	/**
	 * 设置消息重试次数
	 *
	 * @param maxRetryCount 消息重试次数
	 */
	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}


	/**
	 * 获取消息剩余重试次数
	 *
	 * @return 消息剩余重试次数
	 */
	public int getLeftRetryCount() {
		return leftRetryCount;
	}


	/**
	 * 设置消息剩余重试次数
	 *
	 * @param leftRetryCount 消息剩余重试次数
	 */
	public void setLeftRetryCount(int leftRetryCount) {
		this.leftRetryCount = leftRetryCount;
	}


	/**
	 * 获取消息重试次数
	 *
	 * @return 消息重试次数
	 */
	public int getRetryCount() {
		return retryCount;
	}


	/**
	 * 设置消息重试次数
	 *
	 * @param retryCount 消息重试次数
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}


	/**
	 * 获取消息偏移量
	 *
	 * @return 消息偏移量
	 */
	public Long getOffset() {
		return offset;
	}


	/**
	 * 设置消息偏移量
	 *
	 * @param offset 消息偏移量
	 */
	public void setOffset(Long offset) {
		this.offset = offset;
	}


	/**
	 * 获取消息体
	 *
	 * @return 消息体
	 */
	public String getBody() {
		return body;
	}


	/**
	 * 设置消息体
	 *
	 * @param body 消息体
	 */
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
