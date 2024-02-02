package org.example.webfluxdemo.global.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.webfluxdemo.global.code.Descriptions;
import org.example.webfluxdemo.global.code.Examples;
import org.example.webfluxdemo.global.exception.CustomExceptionAble;
import org.example.webfluxdemo.global.exception.CustomExceptionType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.example.webfluxdemo.global.utils.DateUtils.ISO_DATETIME_PATTERN;

@Slf4j
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseDto<T> {

    private static final String UTF_8 = "UTF-8";

    @Schema(description = Descriptions.CODE, example = Examples.OK, requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATETIME_PATTERN)
    private LocalDateTime responseDateTime;
    private T data;

    @Builder
    public CommonResponseDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.responseDateTime = LocalDateTime.now();
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> ok() {
        CommonResponseDto<T> responseDto = CommonResponseDto.<T>builder()
                .code(HttpStatus.OK.name())
                .build();
        return getOkResponseEntity(responseDto);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> ok(T object) {
        CommonResponseDto<T> responseDto = CommonResponseDto.<T>builder()
                .code(HttpStatus.OK.name())
                .data(object)
                .build();
        return getOkResponseEntity(responseDto);
    }

    public static ResponseEntity<Resource> downloadOk(Resource downloadResource) {
        return getDownloadOkResponseEntity(downloadResource);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> created() {
        CommonResponseDto<T> responseDto = CommonResponseDto.<T>builder()
                .code(HttpStatus.OK.name())
                .build();
        return getCreatedResponseEntity(responseDto);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> created(T object) {
        CommonResponseDto<T> responseDto = CommonResponseDto.<T>builder()
                .code(HttpStatus.OK.name())
                .data(object)
                .build();
        return getCreatedResponseEntity(responseDto);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> accepted() {
        CommonResponseDto<T> responseDto = CommonResponseDto.<T>builder()
                .code(HttpStatus.OK.name())
                .build();
        return getAcceptedResponseEntity(responseDto);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> badRequest(String message) {
        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(CustomExceptionType.PARAMETER_INVALID.name())
                .message(isEmpty(message) ? CustomExceptionType.PARAMETER_INVALID.getMessage() : message)
                .build();
        return getBadRequestResponseEntity(response);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> badRequest(CustomExceptionAble type, String additionalMessage) {
        final String errorMessage = type.getMessage().equals(additionalMessage) ?
                type.getMessage() : format("%s %s", type.getMessage(), additionalMessage);

        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(type.getName())
                .message(errorMessage)
                .build();
        return getBadRequestResponseEntity(response);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> badRequest(CustomExceptionAble type, String message, T data) {
        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(type.getName())
                .message(message)
                .data(data)
                .build();
        return getBadRequestResponseEntity(response);
    }

    public static ResponseEntity<CommonResponseDto<List<FieldError>>> badRequest(CustomExceptionAble type, BindingResult bindingResult) {
        CommonResponseDto<List<FieldError>> response = CommonResponseDto.<List<FieldError>>builder()
                .code(type.getName())
                .message(type.getMessage())
                .data(FieldError.of(bindingResult))
                .build();
        return getBadRequestResponseEntity(response);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> unauthorized() {
        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(CustomExceptionType.USER_UNAUTHORIZED.name())
                .message(CustomExceptionType.USER_UNAUTHORIZED.getMessage())
                .build();
        return getUnauthorizedResponseEntity(response);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> unauthorized(CustomExceptionAble customExceptionAble) {
        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(customExceptionAble.getName())
                .message(customExceptionAble.getMessage())
                .build();
        return getUnauthorizedResponseEntity(response);
    }


    public static <T> ResponseEntity<CommonResponseDto<T>> internalServerError(String message) {
        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(CustomExceptionType.INTERNAL_SERVER_ERROR.name())
                .message(message)
                .build();
        return getInternalServerErrorResponseEntity(response);
    }

    public static <T> ResponseEntity<CommonResponseDto<T>> internalServerError(CustomExceptionAble type) {
        CommonResponseDto<T> response = CommonResponseDto.<T>builder()
                .code(type.getName())
                .message(type.getMessage())
                .build();
        return getInternalServerErrorResponseEntity(response);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getOkResponseEntity(CommonResponseDto<T> responseDto) {
        return ResponseEntity.ok(responseDto);
    }

    private static ResponseEntity<Resource> getDownloadOkResponseEntity(Resource downloadResource) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadResource.getFilename() + "\"")
                .body(downloadResource);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getCreatedResponseEntity(CommonResponseDto<T> responseDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getAcceptedResponseEntity(CommonResponseDto<T> responseDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(responseDto);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getBadRequestResponseEntity(CommonResponseDto<T> response) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getUnauthorizedResponseEntity(CommonResponseDto<T> response) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getForbiddenResponseEntity(CommonResponseDto<T> response) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    private static <T> ResponseEntity<CommonResponseDto<T>> getInternalServerErrorResponseEntity(CommonResponseDto<T> response) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
