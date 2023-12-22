package org.chobit.spring.log;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;


/**
 * @author rui.zhang
 */
public class QuickLogPointcutSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    private final StaticMethodMatcher matcher = new AnnotationMethodMatcher(QuickLog.class);


    private final Pointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return matcher.matches(method, targetClass);
        }
    };


    @NonNull
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
