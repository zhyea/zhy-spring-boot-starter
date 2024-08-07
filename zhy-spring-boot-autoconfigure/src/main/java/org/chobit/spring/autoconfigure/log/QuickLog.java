package org.chobit.spring.autoconfigure.log;

import java.lang.annotation.*;

/**
 * 用于自动填充日志
 *
 * @author robin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QuickLog {


    /**
     * 业务描述
     *
     * @return 业务描述
     */
    String biz() default "";


    /**
     * 是否允许异常
     * <p>
     * 出现异常时是否打印error级别的日志
     *
     * @return true 允许, false 不允许
     */
    boolean allowExcept() default false;
}
