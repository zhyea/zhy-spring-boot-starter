package org.chobit.spring.dlock.interceptor;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Interface used by {@link DLockInterceptor}.
 * Implementations know how to source RLock attributes,
 * whether from configuration, metadata attributes at source level, or elsewhere.
 *
 * @author rui.zhang
 */
public interface RLockOperationSource extends Serializable {


    /**
     * 获取方法的RedLock配置
     *
     * @param method      相关
     * @param targetClass 目标类
     * @return RedLock相关属性
     */
    @Nullable
    RLockOperation getRedLockOperation(Method method, @Nullable Class<?> targetClass);

}
