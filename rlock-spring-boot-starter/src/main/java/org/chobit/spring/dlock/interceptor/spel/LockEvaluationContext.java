package org.chobit.spring.dlock.interceptor.spel;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * Lock specific evaluation context that adds a method parameters as SpEL variables, in a lazy manner.
 * The lazy nature eliminates unneeded parsing of classes byte code for parameter discovery.
 *
 * @author robin
 */
public class LockEvaluationContext extends MethodBasedEvaluationContext {


    public LockEvaluationContext(Object rootObject,
                                 Method method,
                                 Object[] arguments,
                                 ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
    }


}
