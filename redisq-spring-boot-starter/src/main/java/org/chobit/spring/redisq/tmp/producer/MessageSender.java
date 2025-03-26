package org.chobit.spring.redisq.tmp.producer;

import java.util.concurrent.TimeUnit;

public interface MessageSender<T> {


	MessageSender<T> withTimeToLive(long time, TimeUnit timeUnit);


	MessageSender<T> withTargetConsumer(String consumerId);


	void submit();

}
