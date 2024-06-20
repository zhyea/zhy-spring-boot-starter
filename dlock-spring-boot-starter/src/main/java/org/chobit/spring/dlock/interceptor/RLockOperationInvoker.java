package org.chobit.spring.dlock.interceptor;


/**
 * Abstract the invocation of a redLock operation.
 *
 * <p>Does not provide a way to transmit checked exceptions but
 * provide a special exception that should be used to wrap any
 * exception that was thrown by the underlying invocation.
 * Callers are expected to handle this issue type specifically.
 *
 * @author robin
 */
@FunctionalInterface
public interface RLockOperationInvoker {



    /**
     * Invoke the cache operation defined by this instance. Wraps any exception
     * that is thrown during the invocation in a {@link WrappedThrowableException}.
     *
     * @return the result of the operation
     * @throws WrappedThrowableException if an error occurred while invoking the operation
     */
    Object invoke() throws WrappedThrowableException;


    /**
     * Wrap any exception thrown while invoking {@link #invoke()}.
     */
    class WrappedThrowableException extends RuntimeException {

        private final Throwable original;

        public WrappedThrowableException(Throwable original) {
            super(original.getMessage(), original);
            this.original = original;
        }

        public Throwable getOriginal() {
            return this.original;
        }
    }


}
