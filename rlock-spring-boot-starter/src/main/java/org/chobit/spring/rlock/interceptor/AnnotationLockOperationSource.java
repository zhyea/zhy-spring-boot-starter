package org.chobit.spring.rlock.interceptor;

import org.chobit.spring.rlock.RLock;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the {@link LockOperationSource} interface
 * for working with lock metadata in annotation format.
 *
 * <p>This class reads Spring's {@link RLock} annotations
 * and exposes corresponding lock operation definition to Spring's infrastructure.
 * This class may also serve as base class for a custom {@link LockOperationSource}.
 *
 * @author robin
 */
public class AnnotationLockOperationSource implements LockOperationSource, Serializable {


    /**
     * 缓存
     */
    private final Map<Object, Optional<LockOperation>> attrCache =
            new ConcurrentHashMap<>(1024);


    @Override
    public LockOperation getLockOperation(Method method, Class<?> targetClass) {

        if (method.getDeclaringClass() == Object.class) {
            return null;
        }

        Object cacheKey = getCacheKey(method, targetClass);

        if (!attrCache.containsKey(cacheKey)) {
            LockOperation attr = computeLockAttribute(method, targetClass);
            if (null == attr) {
                attrCache.put(cacheKey, Optional.empty());
                return null;
            }
            attrCache.put(cacheKey, Optional.of(attr));
            return attr;
        } else {
            Optional<LockOperation> opt = attrCache.get(cacheKey);
            return opt.orElse(null);
        }
    }

    private LockOperation computeLockAttribute(Method method, Class<?> targetClass) {

        LockOperation attr = computeLockAttribute(method);
        if (null != attr) {
            return attr;
        }

        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        if (specificMethod != method) {
            attr = computeLockAttribute(method);
            return attr;
        }
        return null;
    }


    private LockOperation computeLockAttribute(Method method) {
        if (method.getAnnotations().length > 0) {
            return parseLockAttribute(method);
        }
        return null;
    }

    private LockOperation parseLockAttribute(Method method) {
        AnnotationAttributes attributes =
                AnnotatedElementUtils.getMergedAnnotationAttributes(method, RLock.class);
        if (null != attributes) {
            return parseLockAttribute(attributes);
        } else {
            return null;
        }
    }

    private LockOperation parseLockAttribute(AnnotationAttributes attributes) {
        String key = attributes.getString("key");
        Long waitTime = attributes.getNumber("waitTime");
        Long leaseTime = attributes.getNumber("leaseTime");
        TimeUnit unit = (TimeUnit) attributes.get("timeUnit");
        boolean finallyRelease = attributes.getBoolean("finallyRelease");

        LockOperation attr = new LockOperation();
        attr.setKey(key);
        attr.setWaitTime(waitTime);
        attr.setLeaseTime(leaseTime);
        attr.setTimeUnit(unit);
        attr.setFinallyRelease(finallyRelease);
        
        return attr;
    }


    private Object getCacheKey(Method method, Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }


}
