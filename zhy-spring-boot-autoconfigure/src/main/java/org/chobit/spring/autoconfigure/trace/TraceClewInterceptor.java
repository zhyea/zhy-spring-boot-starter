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


    private final InheritableTraceHolder inheritableTraceHolder;


    private final TraceClewProperties traceClewProperties;


    public TraceClewInterceptor(InheritableTraceHolder inheritableTraceHolder, TraceClewProperties properties) {
        this.inheritableTraceHolder = inheritableTraceHolder;
        this.traceClewProperties = properties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        String traceFlag = traceClewProperties.getFlag();
        if (isBlank(traceFlag)) {
            traceFlag = "traceId";
        }

        Method method = invocation.getMethod();
        String clazz = method.getDeclaringClass().getName();

        String traceId = MDC.get(traceFlag);

        if (isNotBlank(traceId)) {
            if (!Objects.equals(traceId, inheritableTraceHolder.get(traceFlag))) {
                inheritableTraceHolder.put(traceFlag, traceId);
            }
            return invocation.proceed();
        }

        // traceId不存在，需要手动填充
        traceId = inheritableTraceHolder.get(traceFlag);
        if (isBlank(traceId)) {
            traceId = StrKit.uuid();
            inheritableTraceHolder.put(traceFlag, traceId);
        }

        try {
            MDC.put(traceFlag, traceId);

            logger.debug("===TraceClew=== Add traceId to method {}#{}", clazz, method.getName());

            return invocation.proceed();
        } finally {
            MDC.remove(traceFlag);
            inheritableTraceHolder.remove(traceFlag);
            logger.debug("===TraceClew=== Statistic {}#{}, method cost:{} ms", clazz, method.getName(), System.currentTimeMillis() - start);
        }
    }


    @Override
    public void destroy() throws Exception {
        inheritableTraceHolder.clear();
    }
}
