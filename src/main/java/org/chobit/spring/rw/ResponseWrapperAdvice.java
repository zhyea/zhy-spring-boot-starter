package org.chobit.spring.rw;

import org.chobit.commons.model.response.Result;
import org.chobit.commons.utils.JsonKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 接口异常处理
 *
 * @author robin
 */
@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {


	private final RwProperties rwProperties;

	@Autowired
	public ResponseWrapperAdvice(RwProperties rwProperties) {
		this.rwProperties = rwProperties;
	}

	@Override
	public boolean supports(MethodParameter returnType,
	                        Class<? extends HttpMessageConverter<?>> converterType) {
		boolean isApiController = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), RestController.class)
				|| returnType.hasMethodAnnotation(ResponseBody.class);;

		boolean existsResponseWrapper = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseWrapper.class)
				|| returnType.hasMethodAnnotation(ResponseWrapper.class);

		return isApiController && existsResponseWrapper;
	}


	@Override
	public Object beforeBodyWrite(@Nullable Object body,
	                              MethodParameter returnType,
	                              MediaType selectedContentType,
	                              Class<? extends HttpMessageConverter<?>> selectedConverterType,
	                              ServerHttpRequest request,
	                              ServerHttpResponse response) {
		if (body instanceof Result) {
			return body;
		}
		if (selectedConverterType.equals(StringHttpMessageConverter.class)) {
			return JsonKit.toJson(new Result<>(rwProperties.getSuccessCode(), body));
		}
		return new Result<>(body);
	}
}