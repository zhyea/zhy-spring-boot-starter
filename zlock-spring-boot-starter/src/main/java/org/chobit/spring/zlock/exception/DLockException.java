package org.chobit.spring.zlock.exception;


/**
 * 分布式锁相关异常
 *
 * @author rui.zhang
 */
public class DLockException extends RuntimeException {

	public DLockException() {
	}


	public DLockException(String message) {
		super(message);
	}
}
