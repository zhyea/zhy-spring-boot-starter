package org.chobit.spring.dlock.exception;


/**
 * 分布式锁相关异常
 *
 * @author robin
 */
public class DLockException extends RuntimeException {


	public DLockException() {
	}


	public DLockException(String message) {
		super(message);
	}
}
