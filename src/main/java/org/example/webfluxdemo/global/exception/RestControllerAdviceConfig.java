package org.example.webfluxdemo.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.example.webfluxdemo.global.dto.CommonResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@RestControllerAdvice
public class RestControllerAdviceConfig {

    @ExceptionHandler(WebExchangeBindException.class)
    protected ResponseEntity<CommonResponseDto<List<CommonResponseDto.FieldError>>> handleWebExchangeBindException(WebExchangeBindException e) {
        log.info(">> handleWebExchangeBindException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(CustomExceptionType.PARAMETER_INVALID, e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonResponseDto<List<CommonResponseDto.FieldError>>> handleBindException(BindException e) {
        log.info(">> handleBindException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(CustomExceptionType.PARAMETER_INVALID, e.getBindingResult());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected HttpEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info(">> handleHttpRequestMethodNotSupportedException, message: {}", e.getBody());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(e.getBody());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info(">> handleMissingServletRequestParameterException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info(">> HttpMessageNotReadableException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info(">> IllegalArgumentException, message: {}", e.getMessage());
        e.printStackTrace();
        return CommonResponseDto.badRequest(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info(">> handleMethodArgumentTypeMismatchException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(CustomExceptionType.PARAMETER_INVALID, CustomExceptionType.PARAMETER_INVALID.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleCustomException(CustomException e) {
        final String errorMessage = isEmpty(e.getMessage()) ? e.getCustomExceptionAble().getMessage() : e.getMessage();
        final CoreHttpStatus coreHttpStatus = e.getCustomExceptionAble().getHttpStatus();

        if (ObjectUtils.isEmpty(coreHttpStatus)) {
            log.info(">> handleBadRequestException, message: {}", errorMessage);
            return CommonResponseDto.badRequest(e.getCustomExceptionAble(), errorMessage);
        }

        CustomExceptionAble customExceptionAble = e.getCustomExceptionAble();
        if (CustomExceptionType.isUnauthorizedType(customExceptionAble)) {
            log.info(">> handleBadRequestException, message: {}", errorMessage);
            return CommonResponseDto.unauthorized(customExceptionAble);
        }

        if (CustomExceptionType.isForbiddenType(customExceptionAble)) {
            log.info(">> handleBadRequestException, message: {}", errorMessage);
            return CommonResponseDto.forbidden(customExceptionAble);
        }

        if (coreHttpStatus.is5xxServerError()) {
            log.error(">> handleInternalServerException, message: {}", errorMessage);
            return CommonResponseDto.internalServerError(errorMessage);
        } else {
            log.info(">> handleBadRequestException, message: {}", errorMessage);
            return CommonResponseDto.badRequest(e.getCustomExceptionAble(), errorMessage);
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleAccessDeniedException(AccessDeniedException e) {
        log.error(">> handleInternalServerException, message: {}", e.getMessage());
        return CommonResponseDto.forbidden();
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponseDto<Object>> handleException(Exception e) {
        log.error(">> Exception", e);
        e.printStackTrace();
        return CommonResponseDto.internalServerError(CustomExceptionType.INTERNAL_SERVER_ERROR.getMessage());
    }

}
