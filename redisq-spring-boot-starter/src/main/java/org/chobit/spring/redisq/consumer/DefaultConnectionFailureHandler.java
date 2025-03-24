package org.chobit.spring.redisq.consumer;

import org.slf4j.Logger;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认连接失败处理器
 *
 * @author robin
 * @since 2025/3/21 23:16
 */
public class DefaultConnectionFailureHandler implements ConnectionFailureHandler {


	private static final long DEFAULT_MILLIS_TO_WAIT_AFTER_CONNECTION_FAILURE = 2000;
	private static final int DEFAULT_CONNECTION_FAILURES_BEFORE_ERROR_LOG = 10;


	private final AtomicInteger connectionFailuresCounter = new AtomicInteger(0);
	private final Logger logger;

	private long millisToWaitAfterConnectionFailure;
	private int connectionFailuresBeforeErrorLog;


	public DefaultConnectionFailureHandler(Logger logger) {
		this.logger = logger;
		this.millisToWaitAfterConnectionFailure = DEFAULT_MILLIS_TO_WAIT_AFTER_CONNECTION_FAILURE;
		this.connectionFailuresBeforeErrorLog = DEFAULT_CONNECTION_FAILURES_BEFORE_ERROR_LOG;
	}


	@Override
	public void onFail(RedisConnectionFailureException e) {
		int count = incrementFailureCounterAndGet();

		if (shouldLogError(count)) {
			logger.error("Could not connect to Redis after {} attempts, retrying in {}ms...",
					this.connectionFailuresCounter, this.millisToWaitAfterConnectionFailure, e);
			this.resetErrorCount();
		} else {
			logger.warn("Error connecting to Redis ({}), retrying in {}ms...", e.getMessage(),
					this.millisToWaitAfterConnectionFailure);
		}

		this.waitAfterConnectionFailure();
	}


	private void resetErrorCount() {
		connectionFailuresCounter.set(0);
	}


	private int incrementFailureCounterAndGet() {
		return connectionFailuresCounter.incrementAndGet();
	}


	private void waitAfterConnectionFailure() {
		try {
			Thread.sleep(this.millisToWaitAfterConnectionFailure);
		} catch (InterruptedException e) {
			// no-op
		}
	}


	private boolean shouldLogError(int count) {
		return (count - 1) == this.connectionFailuresBeforeErrorLog;
	}


	public void setMillisToWaitAfterConnectionFailure(long millisToWaitAfterConnectionFailure) {
		this.millisToWaitAfterConnectionFailure = millisToWaitAfterConnectionFailure;
	}


	public void setConnectionFailuresBeforeErrorLog(int connectionFailuresBeforeErrorLog) {
		this.connectionFailuresBeforeErrorLog = connectionFailuresBeforeErrorLog;
	}
}
