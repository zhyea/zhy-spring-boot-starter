package org.chobit.spring.dlock.exception;


/**
 * 分布式锁相关异常
 *
 * @author robin
 */
public class RLockException extends RuntimeException {


	public RLockException() {
	}


	public RLockException(String message) {
		super(message);
	}
}
