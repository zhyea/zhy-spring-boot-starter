package org.chobit.spring.autoconfigure.trace;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.chobit.commons.utils.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.DisposableBean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.chobit.commons.utils.StrKit.isBlank;
import static org.chobit.commons.utils.StrKit.isNotBlank;


/**
 * TraceClew Interceptor
 *
 * @author robin
 */
public class TraceClewInterceptor implements MethodInterceptor, DisposableBean, Serializable {


    private static final Logger logger = LoggerFactory.getLogger(TraceClewInterceptor.class);


    /**
     * traceId字符串标记
     */
    private static final String TRACE_ID = "trace_id";


    private final InheritableTraceHolder inheritableTraceHolder;


    public TraceClewInterceptor(InheritableTraceHolder inheritableTraceHolder) {
        this.inheritableTraceHolder = inheritableTraceHolder;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        Method method = invocation.getMethod();
        String clazz = method.getDeclaringClass().getName();

        String traceId = MDC.get(TRACE_ID);

        if (isNotBlank(traceId)) {
            if (!Objects.equals(traceId, inheritableTraceHolder.get(TRACE_ID))) {
                inheritableTraceHolder.put(TRACE_ID, traceId);
            }
            return invocation.proceed();
        }

        traceId = inheritableTraceHolder.get(TRACE_ID);
        if (isBlank(traceId)) {
            traceId = StrKit.uuid();
        }

        try {
            MDC.put(TRACE_ID, traceId);
            inheritableTraceHolder.put(TRACE_ID, traceId);

            logger.info("===TraceClew=== Add traceId to method {}#{}", clazz, method.getName());

            return invocation.proceed();
        } finally {
            MDC.remove(TRACE_ID);
            inheritableTraceHolder.remove(TRACE_ID);
            logger.info("===TraceClew=== Statistic {}#{}, method cost:{} ms", clazz, method.getName(), System.currentTimeMillis() - start);
        }
    }


    @Override
    public void destroy() throws Exception {
        inheritableTraceHolder.clear();
    }
}
