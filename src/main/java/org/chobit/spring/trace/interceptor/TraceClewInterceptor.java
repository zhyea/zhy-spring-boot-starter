package org.chobit.spring.trace.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.chobit.commons.utils.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.chobit.commons.utils.StrKit.isBlank;


/**
 * TraceClew Interceptor
 *
 * @author rui.zhang
 */
public class TraceClewInterceptor implements MethodInterceptor, Serializable {


    private static final Logger logger = LoggerFactory.getLogger(TraceClewInterceptor.class);


    /**
     * traceId字符串
     */
    private static final String TRACE_ID = "trace_id";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        Method method = invocation.getMethod();
        String clazz = method.getDeclaringClass().getName();

        boolean needFillTraceId = false;

        try {
            if (isBlank(MDC.get(TRACE_ID))) {
                needFillTraceId = true;
                MDC.put(TRACE_ID, traceId());
                logger.info("===TraceClew=== Add traceId to method {}#{}", clazz, method.getName());
            }

            return invocation.proceed();
        } finally {
            if (needFillTraceId) {
                MDC.remove(TRACE_ID);
                logger.info("===TraceClew=== Statistic {}#{}, method cost:{} ms", clazz, method.getName(), System.currentTimeMillis() - start);
            }
        }
    }


    private String traceId() {
        return StrKit.uuid();
    }

}
