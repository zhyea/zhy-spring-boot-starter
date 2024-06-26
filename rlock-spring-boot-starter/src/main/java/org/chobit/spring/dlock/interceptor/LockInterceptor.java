package org.chobit.spring.dlock.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.chobit.spring.common.OperationInvoker;
import org.springframework.cache.interceptor.CacheOperationInvoker;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * Lock Interceptor
 *
 * @author robin
 */
public class LockInterceptor extends LockAspectSupport implements MethodInterceptor, Serializable {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        OperationInvoker invoker = () -> {
            try {
                return invocation.proceed();
            } catch (Throwable t) {
                throw new OperationInvoker.WrappedThrowableException(t);
            }
        };

        try {
            return execute(invoker, invocation.getThis(), method, invocation.getArguments());
        } catch (CacheOperationInvoker.ThrowableWrapper th) {
            throw th.getOriginal();
        }
    }
}
