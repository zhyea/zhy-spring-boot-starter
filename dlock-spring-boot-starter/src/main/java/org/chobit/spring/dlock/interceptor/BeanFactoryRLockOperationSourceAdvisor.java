package org.chobit.spring.dlock.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * @author robin
 */
public class BeanFactoryRLockOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    private DLockOperationSource lockOperationSource;


    private final DLockOperationSourcePointcut pointcut = new DLockOperationSourcePointcut() {
        @Override
        protected DLockOperationSource getRedLockOperationSource() {
            return lockOperationSource;
        }
    };


    public void setLockOperationSource(@Nullable DLockOperationSource lockOperationSource) {
        this.lockOperationSource = lockOperationSource;
    }


    @NonNull
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
