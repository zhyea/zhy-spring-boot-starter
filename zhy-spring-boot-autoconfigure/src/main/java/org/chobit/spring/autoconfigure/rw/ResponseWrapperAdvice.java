package org.chobit.spring.autoconfigure.rw;

import org.chobit.commons.model.response.Result;
import org.chobit.commons.utils.Collections2;
import org.chobit.commons.utils.JsonKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.chobit.commons.utils.StrKit.join;


/**
 * 接口异常处理
 *
 * @author robin
 */
@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {


    private static final Logger logger = LoggerFactory.getLogger(ResponseWrapperAdvice.class);

    private final RwProperties rwProperties;


    @Autowired
    public ResponseWrapperAdvice(RwProperties rwProperties) {
        this.rwProperties = rwProperties;
        logger.debug("ApiResponseWrapper has been enabled.");
    }


    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        boolean isApiController = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), RestController.class)
                || returnType.hasMethodAnnotation(ResponseBody.class);
        ;

        boolean effectiveResponseWrapper = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseWrapper.class)
                || returnType.hasMethodAnnotation(ResponseWrapper.class);

        effectiveResponseWrapper = rwProperties.isSilentMode() || effectiveResponseWrapper;

        return isApiController && effectiveResponseWrapper;
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
            Result<?> result = new Result<>(rwProperties.getSuccessCode(), body);
            this.setTag(returnType, result);
            return JsonKit.toJson(result);
        }

        Result<?> result = new Result<>(body);
        this.setTag(returnType, result);

        return result;
    }


    /**
     * 设置返回结果中的标签
     */
    private void setTag(MethodParameter returnType, Result<?> result) {
        Tags tags = returnType.getMethodAnnotation(Tags.class);
        if (null != tags && null != tags.value() && tags.value().length > 0) {
            result.setTags(join(tags.value()));
        } else if (Collections2.isNotEmpty(rwProperties.getTags())) {
            result.setTags(join(rwProperties.getTags()));
        }
    }
}