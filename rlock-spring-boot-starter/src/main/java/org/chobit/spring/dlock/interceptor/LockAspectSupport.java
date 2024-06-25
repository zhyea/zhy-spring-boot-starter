package org.chobit.spring.dlock.interceptor;

import org.chobit.spring.common.OperationInvoker;
import org.chobit.spring.dlock.exception.RLockException;
import org.chobit.spring.dlock.interceptor.spel.LockOperationExpressionEvaluator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static jodd.util.StringUtil.isBlank;

/**
 * Base class for lock aspects, such as the {@link LockInterceptor} or an AspectJ aspect.
 * This enables the underlying Spring caching infrastructure to be used easily to implement an aspect for any aspect system.
 * <p>
 * Subclasses are responsible for calling relevant methods in the correct order.
 * <p>
 * Uses the Strategy design pattern. A {@link LockOperationSource} is used for determining caching operations.
 *
 * @author robin
 */
class LockAspectSupport implements BeanFactoryAware, InitializingBean {


    private static final Logger logger = LoggerFactory.getLogger(LockAspectSupport.class);

    private final Map<LockOperationKey, LockOperationMetadata> metadataCache = new ConcurrentHashMap<>(1024);

    private final LockOperationExpressionEvaluator evaluator = new LockOperationExpressionEvaluator();

    private LockOperationSource lockOperationSource;

    private BeanFactory beanFactory;

    private RedissonClient redissonClient;

    public LockAspectSupport() {
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public LockOperationSource getLockOperationSource() {
        return lockOperationSource;
    }

    public void setLockOperationSource(LockOperationSource lockOperationSource) {
        this.lockOperationSource = lockOperationSource;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void afterPropertiesSet() {
        if (null == this.beanFactory) {
            throw new IllegalStateException("Make sure to run within a BeanFactory containing a LockInterceptor bean!");
        }
        if (null == getLockOperationSource()) {
            throw new IllegalStateException(
                    "'lockAttributeSource' is required: If there are no 'lockAttributeSource', then don't use a lock aspect.");
        }
    }


    protected Object execute(final OperationInvoker invoker, Object target, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = getTargetClass(target);
        LockOperationSource operationSource = getLockOperationSource();
        if (null != operationSource) {
            LockOperation operation = operationSource.getLockOperation(method, targetClass);
            LockOperationContext context = createOperationContext(operation, method, args, target, targetClass);
            return this.execute(invoker, context);
        }
        return invoker.invoke();
    }


    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    private Object execute(final OperationInvoker invoker, LockOperationContext context) throws Throwable {
        String key = generateKey(context);

        logger.info("begin redisson lock with key: {}", key);

        if (isBlank(key)) {
            logger.error("obtain lock key error");
            throw new RLockException("failed to get lock key");
        }

        long waitTime = context.metadata.operation.getWaitTime();
        long leaseTime = context.metadata.operation.getLeaseTime();
        TimeUnit timeUnit = context.metadata.operation.getTimeUnit();

        boolean finallyRelease = context.metadata.operation.isFinallyRelease();
        boolean lockResult = false;
        RLock lock = null;
        try {
            lock = redissonClient.getLock(key);
            lockResult = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (!lockResult) {
                logger.error("failed to lock with key: {}", key);
                throw new RLockException("failed to lock with key:" + key);
            }

            logger.debug("lock succeed with key: {}", key);

            return invoker.invoke();

        } catch (Exception e) {
            logger.error("an error occurred during lock", e);
            throw e;
        } finally {
            if (finallyRelease && lockResult) {
                lock.unlock();
                logger.debug("unlock with the key: {}", key);
            }
            logger.info("release redisson lock with key: {}", key);
        }
    }


    private String generateKey(LockOperationContext context) {
        Object key = context.generateKey();
        if (null == key) {
            throw new IllegalArgumentException("Null key returned for lock operation (maybe you are " +
                    "using named params on classes without debug info?) " + context.metadata.operation);
        }
        return key.toString();
    }


    protected LockOperationContext createOperationContext(LockOperation operation,
                                                          Method method,
                                                          Object[] args,
                                                          Object target,
                                                          Class<?> targetClass) {
        LockOperationMetadata metadata = createLockOperationMetadata(operation, method, targetClass);
        return new LockOperationContext(metadata, args, target);
    }


    /**
     * Return the {@link LockOperationMetadata} for the specified operation.
     * <p>Resolve the {@link org.springframework.cache.interceptor.KeyGenerator} to be
     * used for the operation.
     *
     * @param operation   the operation
     * @param method      the method on which the operation is invoked
     * @param targetClass the target type
     * @return the resolved metadata for the operation
     */
    protected LockOperationMetadata createLockOperationMetadata(LockOperation operation,
                                                                Method method,
                                                                Class<?> targetClass) {
        LockOperationKey operationKey = new LockOperationKey(operation, method, targetClass);
        LockOperationMetadata metadata = this.metadataCache.get(operationKey);
        if (null == metadata) {
            metadata = new LockOperationMetadata(operation, method, targetClass);
            this.metadataCache.put(operationKey, metadata);
        }
        return metadata;
    }


    private static final class LockOperationKey implements Comparable<LockOperationKey> {

        private final LockOperation operation;

        private final AnnotatedElementKey methodKey;

        private LockOperationKey(LockOperation operation, Method method, Class<?> targetClass) {
            this.operation = operation;
            this.methodKey = new AnnotatedElementKey(method, targetClass);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof LockOperationKey)) {
                return false;
            }
            LockOperationKey otherKey = (LockOperationKey) other;
            return (this.operation.equals(otherKey.operation) && this.methodKey.equals(otherKey.methodKey));
        }

        @Override
        public int hashCode() {
            return (this.operation.hashCode() * 31 + this.methodKey.hashCode());
        }

        @Override
        public String toString() {
            return this.operation + " on " + this.methodKey;
        }

        @Override
        public int compareTo(LockOperationKey other) {
            int result = this.operation.getKey().compareTo(other.operation.getKey());
            if (result == 0) {
                result = this.methodKey.compareTo(other.methodKey);
            }
            return result;
        }
    }


    /**
     * Metadata of lock operation that does not depend on a particular invocation
     * which makes it a good candidate for lock.
     */
    protected static class LockOperationMetadata {

        private final LockOperation operation;

        private final Method method;

        private final Class<?> targetClass;

        private final Method targetMethod;

        private final AnnotatedElementKey methodKey;

        public LockOperationMetadata(LockOperation operation,
                                     Method method,
                                     Class<?> targetClass) {
            this.operation = operation;
            this.method = method;
            this.targetClass = targetClass;
            this.targetMethod = (!Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(method, targetClass) : this.method);
            this.methodKey = new AnnotatedElementKey(this.targetMethod, targetClass);
        }
    }


    /**
     * A context for a {@link LockOperation}.
     */
    protected class LockOperationContext {

        private final LockOperationMetadata metadata;

        private final Object[] args;

        private final Object target;

        public LockOperationContext(LockOperationMetadata metadata, Object[] args, Object target) {
            this.metadata = metadata;
            this.args = extractArgs(metadata.method, args);
            this.target = target;
        }

        private Object[] extractArgs(Method method, Object[] args) {
            if (!method.isVarArgs()) {
                return args;
            }
            Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
            Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
            System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
            System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
            return combinedArgs;
        }

        /**
         * Compute the key for the given caching operation.
         */
        @Nullable
        protected Object generateKey() {
            if (isBlank(this.metadata.operation.getKey())) {
                throw new RLockException("The key for RLock is blank.");
            }
            EvaluationContext evaluationContext = createEvaluationContext();
            return evaluator.key(this.metadata.operation.getKey(), this.metadata.methodKey, evaluationContext);
        }

        private EvaluationContext createEvaluationContext() {
            return evaluator.createEvaluationContext(this.metadata.method, this.args,
                    this.target, this.metadata.targetClass, this.metadata.targetMethod, beanFactory);
        }
    }

}