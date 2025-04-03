package org.chobit.spring.redisq.beetle.consumer;

import org.chobit.spring.redisq.beetle.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.String.format;

/**
 * 多线程消费策略
 *
 * @author robin
 * @since 2025/3/21 22:33
 */
public class ThreadingConsumeStrategy implements ConsumeStrategy {

	private static final Logger logger = LoggerFactory.getLogger(ThreadingConsumeStrategy.class);


	private static final long MAX_WAIT_MILLIS_WHEN_STOPPING_THREADS = 30 * 1000;


	private final int numThreads;
	private final List<ConsumeThread> consumeThreads;


	public ThreadingConsumeStrategy(int numThreads) {
		this.numThreads = numThreads;
		this.consumeThreads = new ArrayList<>(numThreads);
	}


	@Override
	public void start(String topic, Callable<Message> callback) {
		for (int i = 0; i < numThreads; i++) {
			ConsumeThread consumeThread = new ConsumeThread(callback);

			consumeThread.setName(format("beetle-consumer[%s]-%s", topic, i));
			consumeThread.start();

			consumeThreads.add(consumeThread);
			logger.debug("Started message consumer thread [{}]", consumeThread.getName());
		}
	}


	@Override
	public void stop() {
		try {
			for (ConsumeThread thread : this.consumeThreads) {
				logger.debug("Stopping message consuming thread [{}]", thread.getName());
				thread.stopRequested = true;
			}
			this.waitForAllThreadsToTerminate();
		} finally {
			consumeThreads.clear();
		}
	}


	private void waitForAllThreadsToTerminate() {
		for (ConsumeThread thread : this.consumeThreads) {
			try {
				thread.join(MAX_WAIT_MILLIS_WHEN_STOPPING_THREADS);
			} catch (InterruptedException e) {
				logger.warn("Unable to join thread [{}].", thread.getName(), e);
			}
		}
	}


	/**
	 * 消费线程定义
	 */
	private static class ConsumeThread extends Thread {

		private boolean stopRequested = false;
		private final Callable<Message> callback;

		public ConsumeThread(Callable<Message> callback) {
			assert null != callback;
			this.callback = callback;
		}

		@Override
		public void run() {
			while (!stopRequested && !isInterrupted()) {
				try {
					Message message = callback.call();
					// TODO handle message
				} catch (Throwable t) {
					logger.error("Exception while handling next queue item.", t);
				}
			}
		}
	}
	// ----------------------
}
