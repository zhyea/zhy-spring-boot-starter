package org.chobit.spring.dlock.interceptor;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * A Pointcut that matches if the underlying {@link LockOperationSource} has an attribute for a given method.
 *
 * @author robin
 */
public abstract class LockOperationSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {


    @Override
    public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
        LockOperationSource ros = getLockOperationSource();
        return (null != ros && null != ros.getLockOperation(method, targetClass));
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LockOperationSourcePointcut)) {
            return false;
        }
        LockOperationSourcePointcut otherPc = (LockOperationSourcePointcut) other;
        return ObjectUtils.nullSafeEquals(this.getLockOperationSource(), otherPc.getLockOperationSource());
    }


    @Override
    public int hashCode() {
        return LockOperationSourcePointcut.class.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + getLockOperationSource();
    }


    /**
     * 获取RLock Attribute 源
     *
     * @return RLock Attribute 源
     */
    @Nullable
    protected abstract LockOperationSource getLockOperationSource();

}
