package org.chobit.spring.dlock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * RedLock注解定义
 *
 * @author rui.zhang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DLock {


    /**
     * RedLock Key
     *
     * @return RedLock key
     */
    String key();


    /**
     * 等待时间
     *
     * @return 等待时间
     */
    long waitTime() default 0;


    /**
     * 持有锁的时长
     *
     * @return 持有锁的时长
     */
    long leaseTime() default 1;


    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;


    /**
     * 最终是否需要释放锁
     *
     * @return 是否需要释放锁
     */
    boolean finallyRelease() default true;


}
