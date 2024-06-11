package org.chobit.spring.trace;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.chobit.commons.utils.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.Serializable;
import java.lang.reflect.Method;

import static org.chobit.commons.utils.StrKit.isNotBlank;


/**
 * TraceClew Interceptor
 *
 * @author robin
 */
public class TraceClewInterceptor implements MethodInterceptor, Serializable {


    private static final Logger logger = LoggerFactory.getLogger(TraceClewInterceptor.class);


    /**
     * traceId字符串标记
     */
    private static final String TRACE_ID = "trace_id";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        Method method = invocation.getMethod();
        String clazz = method.getDeclaringClass().getName();


        if (isNotBlank(MDC.get(TRACE_ID))) {
            return invocation.proceed();
        }

        try {
            MDC.put(TRACE_ID, traceId());
            logger.info("===TraceClew=== Add traceId to method {}#{}", clazz, method.getName());

            return invocation.proceed();
        } finally {
            MDC.remove(TRACE_ID);
            logger.info("===TraceClew=== Statistic {}#{}, method cost:{} ms", clazz, method.getName(), System.currentTimeMillis() - start);
        }
    }


    private String traceId() {
        return StrKit.uuid();
    }

}
