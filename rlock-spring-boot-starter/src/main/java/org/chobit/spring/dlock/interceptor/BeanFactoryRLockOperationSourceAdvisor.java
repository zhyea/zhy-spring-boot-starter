package org.chobit.spring.dlock.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * @author robin
 */
public class BeanFactoryRLockOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    private LockOperationSource lockOperationSource;


    private final LockOperationSourcePointcut pointcut = new LockOperationSourcePointcut() {
        @Override
        protected LockOperationSource getLockOperationSource() {
            return lockOperationSource;
        }
    };


    public void setLockOperationSource(@Nullable LockOperationSource lockOperationSource) {
        this.lockOperationSource = lockOperationSource;
    }


    @NonNull
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
