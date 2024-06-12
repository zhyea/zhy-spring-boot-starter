package org.chobit.spring.response;


import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;


/**
 * 接口返回值包装注解
 *
 * @author robin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
public @interface ResponseWrapper {


}