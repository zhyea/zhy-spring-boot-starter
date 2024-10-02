package org.chobit.spring.autoconfigure.rw;


import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;


/**
 * 接口返回值中的标签
 *
 * @author robin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
public @interface Tags {


    /**
     * 标签信息
     *
     * @return 标签集合
     */
    String[] value() default {};

}