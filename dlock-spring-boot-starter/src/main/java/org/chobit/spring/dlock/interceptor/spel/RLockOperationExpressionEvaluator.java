package org.chobit.spring.dlock.interceptor.spel;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class handling the SpEL expression parsing.
 * Meant to be used as a reusable, thread-safe component.
 *
 * <p>Performs internal caching for performance reasons using {@link AnnotatedElementKey}.
 *
 * @author rui.zhang
 */
public class RLockOperationExpressionEvaluator extends CachedExpressionEvaluator {


    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);


    /**
     * Create an {@link EvaluationContext}.
     *
     * @param method       the method
     * @param args         the method arguments
     * @param target       the target object
     * @param targetClass  the target class
     * @param targetMethod the target method
     * @param beanFactory  the bean factory
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Method method,
                                                     Object[] args,
                                                     Object target,
                                                     Class<?> targetClass,
                                                     Method targetMethod,
                                                     @Nullable BeanFactory beanFactory) {

        RLockExpressionRootObject rootObject =
                new RLockExpressionRootObject(method, args, target, targetClass);
        RLockEvaluationContext evaluationContext =
                new RLockEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer());
        if (null != beanFactory) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }


    @Nullable
    public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
    }


    /**
     * Clear all caches.
     */
    void clear() {
        this.keyCache.clear();
    }
}
