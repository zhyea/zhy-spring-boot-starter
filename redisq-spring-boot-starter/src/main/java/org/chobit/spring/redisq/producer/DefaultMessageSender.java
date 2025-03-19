package org.chobit.spring.redisq.producer;

import java.util.concurrent.TimeUnit;

public class DefaultMessageSender implements MessageSender {


	private long timeToLive;
	private TimeUnit timeUnit;
	private String targetConsumer;


	@Override
	public MessageSender withTimeToLive(long time, TimeUnit timeUnit) {
		this.timeToLive = time;
		this.timeUnit = timeUnit;
		return this;
	}

	@Override
	public MessageSender withTargetConsumer(String consumerId) {
		this.targetConsumer = consumerId;
		return this;
	}

}
