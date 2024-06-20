package org.chobit.spring.zlock.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheOperationInvoker;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * RedLock Interceptor
 *
 * @author rui.zhang
 */
public class RLockInterceptor extends RLockAspectSupport implements MethodInterceptor, Serializable {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        RLockOperationInvoker invoker = () -> {
            try {
                return invocation.proceed();
            } catch (Throwable t) {
                throw new RLockOperationInvoker.WrappedThrowableException(t);
            }
        };

        try {
            return execute(invoker, invocation.getThis(), method, invocation.getArguments());
        } catch (CacheOperationInvoker.ThrowableWrapper th) {
            throw th.getOriginal();
        }
    }
}
