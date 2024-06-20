package org.chobit.spring.dlock.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * @author rui.zhang
 */
public class BeanFactoryRLockOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Nullable
    private RLockOperationSource redLockOperationSource;


    private final RLockOperationSourcePointcut pointcut = new RLockOperationSourcePointcut() {
        @Override
        protected RLockOperationSource getRedLockOperationSource() {
            return redLockOperationSource;
        }
    };


    public void setRedLockOperationSource(@Nullable RLockOperationSource redLockOperationSource) {
        this.redLockOperationSource = redLockOperationSource;
    }


    @NonNull
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
