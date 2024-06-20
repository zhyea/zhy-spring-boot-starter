package org.chobit.spring.dlock.interceptor;

import org.chobit.spring.dlock.DLock;
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
 * Implementation of the {@link RLockOperationSource} interface
 * for working with red lock metadata in annotation format.
 *
 * <p>This class reads Spring's {@link DLock} annotations
 * and exposes corresponding redLock operation definition to Spring's infrastructure.
 * This class may also serve as base class for a custom {@link RLockOperationSource}.
 *
 * @author rui.zhang
 */
public class AnnotationRLockOperationSource implements RLockOperationSource, Serializable {


    /**
     * 缓存
     */
    private final Map<Object, Optional<RLockOperation>> attrCache =
            new ConcurrentHashMap<>(1024);


    @Override
    public RLockOperation getRedLockOperation(Method method, Class<?> targetClass) {

        if (method.getDeclaringClass() == Object.class) {
            return null;
        }

        Object cacheKey = getCacheKey(method, targetClass);

        if (!attrCache.containsKey(cacheKey)) {
            RLockOperation attr = computeRedLockAttribute(method, targetClass);
            if (null == attr) {
                attrCache.put(cacheKey, Optional.empty());
                return null;
            }
            attrCache.put(cacheKey, Optional.of(attr));
            return attr;
        } else {
            Optional<RLockOperation> opt = attrCache.get(cacheKey);
            return opt.orElse(null);
        }
    }

    private RLockOperation computeRedLockAttribute(Method method, Class<?> targetClass) {

        RLockOperation attr = computeRedLockAttribute(method);
        if (null != attr) {
            return attr;
        }

        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        if (specificMethod != method) {
            attr = computeRedLockAttribute(method);
            return attr;
        }
        return null;
    }


    private RLockOperation computeRedLockAttribute(Method method) {
        if (method.getAnnotations().length > 0) {
            return parseRedLockAttribute(method);
        }
        return null;
    }

    private RLockOperation parseRedLockAttribute(Method method) {
        AnnotationAttributes attributes =
                AnnotatedElementUtils.getMergedAnnotationAttributes(method, DLock.class);
        if (null != attributes) {
            return parseRedLockAttribute(attributes);
        } else {
            return null;
        }
    }

    private RLockOperation parseRedLockAttribute(AnnotationAttributes attributes) {
        String key = attributes.getString("key");
        Long waitTime = attributes.getNumber("waitTime");
        Long leaseTime = attributes.getNumber("leaseTime");
        TimeUnit unit = (TimeUnit) attributes.get("timeUnit");
        boolean finallyRelease = attributes.getBoolean("finallyRelease");

        RLockOperation attr = new RLockOperation();
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
