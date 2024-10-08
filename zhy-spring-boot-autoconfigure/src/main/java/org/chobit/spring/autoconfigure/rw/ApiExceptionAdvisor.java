package org.chobit.spring.autoconfigure.rw;


import org.chobit.commons.model.response.Result;
import org.chobit.commons.utils.Collections2;
import org.chobit.spring.autoconfigure.rw.exception.RwException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.chobit.commons.constans.Symbol.COMMA;
import static org.chobit.commons.constans.Symbol.EMPTY;
import static org.chobit.commons.utils.StrKit.join;


/**
 * Api返回值包装切面定义
 *
 * @author robin
 */
@RestControllerAdvice
public class ApiExceptionAdvisor {


	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionAdvisor.class);


	private final RwProperties rwProperties;

	@Autowired
	public ApiExceptionAdvisor(RwProperties rwProperties) {
		this.rwProperties = rwProperties;
	}


	/**
	 * 自定义业务异常返回值处理
	 *
	 * @param e 异常信息
	 * @return 封装后的异常返回值
	 */
	@ResponseBody
	@ExceptionHandler(RwException.class)
	public Object rwExceptionHandler(RwException e) {

		Result<?> r = new Result<>(e.getCode());
		r.setMsg(e.getMessage());
		logger.warn("发现服务端异常：{}", r.getMsg(), e);

		wrapTags(r);

		return r;
	}


	/**
	 * 参数校验异常返回值处理
	 *
	 * @param ex 异常信息
	 * @return 封装后的异常返回值
	 */
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<?> paramExceptionHandler(MethodArgumentNotValidException ex) {

		String msg = EMPTY;
		if (ex.hasFieldErrors()) {
			msg = (null == ex.getFieldError() ? EMPTY : ex.getFieldError().getDefaultMessage());
		} else if (ex.hasGlobalErrors()) {
			msg = (null == ex.getGlobalError() ? EMPTY : ex.getGlobalError().getDefaultMessage());
		}

		String logMsg = ex.getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(COMMA));

		Result<?> r = new Result<>(rwProperties.getFailCode());
		r.setMsg(msg);

		logger.warn("请求参数错误, total:{}, detail:{}", ex.getErrorCount(), logMsg);

		wrapTags(r);

		return r;
	}


	/**
	 * 未知BindException处理
	 *
	 * @param ex 异常信息
	 * @return 封装后的异常返回值
	 */
	@ResponseBody
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<?> bindExceptionHandler(BindException ex) {

		String msg = ex.getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(COMMA));

		Result<?> r = new Result<>(rwProperties.getFailCode());
		r.setMsg("未知异常");

		logger.error("发现未知异常: {}", msg, ex);

		wrapTags(r);

		return r;
	}


	/**
	 * 未知异常返回值处理
	 *
	 * @param e 异常信息
	 * @return 封装后的异常返回值
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<?> exceptionHandler(Exception e) {

		Result<?> r = new Result<>(rwProperties.getFailCode());
		r.setMsg("未知异常");

		logger.error("发现未知异常: {}", r.getMsg(), e);

		wrapTags(r);

		return r;
	}



	private void wrapTags(Result<?> result){
		if(Collections2.isNotEmpty(rwProperties.getTags())){
			result.setTags(join(rwProperties.getTags()));
		}
	}

}