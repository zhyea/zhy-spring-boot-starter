package org.chobit.spring.log;

import org.chobit.spring.OperationInvoker;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;


/**
 * TraceClew Interceptor
 *
 * @author robin
 */
public class QuickLogInterceptor extends QuickLogAspectSupport implements MethodInterceptor, Serializable {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        OperationInvoker invoker = () -> {
            try {
                return invocation.proceed();
            } catch (Throwable t) {
                throw new OperationInvoker.WrappedThrowableException(t);
            }
        };


        try {
            return execute(invoker, invocation.getThis(), invocation.getMethod(), invocation.getArguments());
        } catch (OperationInvoker.WrappedThrowableException wte) {
            throw wte.getOriginal();
        }

    }


}
