package org.chobit.spring.dlock.interceptor;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * A Pointcut that matches if the underlying {@link DLockOperationSource} has an attribute for a given method.
 *
 * @author robin
 */
public abstract class DLockOperationSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {


    @Override
    public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
        DLockOperationSource ros = getRedLockOperationSource();
        return (null != ros && null != ros.getLockOperation(method, targetClass));
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DLockOperationSourcePointcut)) {
            return false;
        }
        DLockOperationSourcePointcut otherPc = (DLockOperationSourcePointcut) other;
        return ObjectUtils.nullSafeEquals(this.getRedLockOperationSource(), otherPc.getRedLockOperationSource());
    }


    @Override
    public int hashCode() {
        return DLockOperationSourcePointcut.class.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + getRedLockOperationSource();
    }


    /**
     * 获取RedLock Attribute 源
     *
     * @return RedLock Attribute 源
     */
    @Nullable
    protected abstract DLockOperationSource getRedLockOperationSource();

}
