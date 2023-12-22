package org.chobit.spring.trace;

import java.lang.annotation.*;

/**
 * TraceClew 注解
 * <p>
 * 用于填充traceId
 *
 * @author rui.zhang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TraceClew {

}
