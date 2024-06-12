package org.chobit.spring.response;


import org.chobit.commons.enums.CommonStatusCode;
import org.chobit.commons.model.response.Result;
import org.chobit.spring.response.exception.RwServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author robin
 */
@RestControllerAdvice
public class ApiExceptionAdvisor {


    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionAdvisor.class);


    /**
     * Api异常返回值处理
     *
     * @param e 异常信息
     * @return 封装后的异常返回值
     */
    @ResponseBody
    @ExceptionHandler(value = RwServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object mockoExceptionHandler(RwServerException e) {

        Result<?> r = new Result<>(e.getCode());
        r.setMsg(e.getMessage());

        logger.warn("发现服务端异常：{}", r.getMsg(), e);

        return r;
    }


    /**
     * Api异常返回值处理
     *
     * @param ex 异常信息
     * @return 封装后的异常返回值
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> paramExceptionHandler(MethodArgumentNotValidException ex) {

        String msg = "";
        StringBuilder logMsg = new StringBuilder();
        if (ex.hasFieldErrors()) {
            msg = (null == ex.getFieldError() ? "" : ex.getFieldError().getDefaultMessage());
        } else if (ex.hasGlobalErrors()) {
            msg = (null == ex.getGlobalError() ? "" : ex.getGlobalError().getDefaultMessage());
        }

        ex.getAllErrors().forEach(e -> {
            if (logMsg.length() > 0) {
                logMsg.append(", ");
            }
            logMsg.append(e.getDefaultMessage());
        });

        Result<?> r = new Result<>(CommonStatusCode.FAIL);
        r.setMsg(msg);

        logger.warn("请求参数错误, total:{}, detail: {}", ex.getErrorCount(), logMsg);

        return r;
    }


    /**
     * Api异常返回值处理
     *
     * @param e 异常信息
     * @return 封装后的异常返回值
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> exceptionHandler(Exception e) {

        Result<?> r = new Result<>(CommonStatusCode.FAIL);
        r.setMsg("未知异常");

        logger.warn("发现未知异常：{}", r.getMsg(), e);

        return r;
    }

}