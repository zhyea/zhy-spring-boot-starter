package org.chobit.spring.redisq.producer;

import java.util.concurrent.TimeUnit;

public interface MessageSender {


	MessageSender withTimeToLive(long time, TimeUnit timeUnit);


	MessageSender withTargetConsumer(String consumerId);


}
