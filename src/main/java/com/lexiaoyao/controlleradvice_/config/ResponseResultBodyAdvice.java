package com.lexiaoyao.controlleradvice_.config;

import com.lexiaoyao.controlleradvice_.annos.ResponseResultBody;
import com.lexiaoyao.controlleradvice_.model.Result;
import com.lexiaoyao.controlleradvice_.model.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

@RestControllerAdvice
@Slf4j
public class ResponseResultBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Class<? extends Annotation> ANNOTATION_TYPE = ResponseResultBody.class;

    /**
     * 判断类或者方法是否使用了 @ResponseResultBody
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }

    /**
     * 当类或者方法使用了 @ResponseResultBody 就会调用这个方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 防止重复包裹的问题出现
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }

    /**
     * 提供对标准Spring MVC异常的处理
     *
     * @param ex the target exception
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Result<?>> exceptionHandler(Exception ex) {
        log.error("ExceptionHandler: {}", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof ResultException) {
            return this.handleResultException((ResultException) ex, headers);
        }
        // TODO: 这里可以自定义其他的异常拦截
        return this.handleException(ex, headers);
    }

    /**
     * 对ResultException类返回返回结果的处理
     */
    private ResponseEntity<Result<?>> handleResultException(ResultException ex, HttpHeaders headers) {
        Result<?> body = Result.failure(ex.getResultStatus());
        HttpStatus status = ex.getResultStatus().getHttpStatus();
        return this.handleExceptionInternal(ex, body, headers, status);
    }

    /**
     * 异常类的统一处理
     */
    private ResponseEntity<Result<?>> handleException(Exception ex, HttpHeaders headers) {
        Result<?> body = Result.failure();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return this.handleExceptionInternal(ex, body, headers, status);
    }

    private ResponseEntity<Result<?>> handleExceptionInternal(
            Exception ex, Result<?> body, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(body, headers, status);
    }

}