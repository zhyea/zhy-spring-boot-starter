package org.chobit.spring.rlock.interceptor;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Interface used by {@link LockInterceptor}.
 * Implementations know how to source RLock attributes,
 * whether from configuration, metadata attributes at source level, or elsewhere.
 *
 * @author robin
 */
public interface LockOperationSource extends Serializable {


    /**
     * 获取方法的RLock配置
     *
     * @param method      相关
     * @param targetClass 目标类
     * @return RLock相关属性
     */
    @Nullable
    LockOperation getLockOperation(Method method, @Nullable Class<?> targetClass);

}
