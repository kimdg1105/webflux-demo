package org.example.webfluxdemo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static org.example.webfluxdemo.global.exception.CoreHttpStatus.*;

@Getter
@AllArgsConstructor
public enum CustomExceptionType implements CustomExceptionAble {
    // Common
    INTERNAL_SERVER_ERROR(CoreHttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하는 중에 서버 내부에서 오류가 발생하였습니다."),
    PARAMETER_INVALID(BAD_REQUEST, "요청값이 잘못되었습니다."),
    USER_UNAUTHORIZED(UNAUTHORIZED, "인증되지 않은 유저입니다."),
    USER_FORBIDDEN(FORBIDDEN, "권한이 없습니다."),
    INVALID_GRANT_TYPE(CoreHttpStatus.FORBIDDEN, "유효하지 않은 Grant 타입입니다."),
    AUTH_SCOPE_INVALID(CoreHttpStatus.FORBIDDEN, "유효하지 않은 Scope 입니다."),
    ;


    private final CoreHttpStatus httpStatus;
    private final String message;

    public static List<CustomExceptionType> getUnauthorizedTypes() {
        return List.of(USER_UNAUTHORIZED);
    }

    public static List<CustomExceptionType> getForbiddenTypes() {
        return List.of(USER_FORBIDDEN,
                INVALID_GRANT_TYPE,
                AUTH_SCOPE_INVALID);
    }

    public static boolean isUnauthorizedType(CustomExceptionAble customExceptionAble) {
        return getUnauthorizedTypes().contains(customExceptionAble);
    }

    public static boolean isForbiddenType(CustomExceptionAble customExceptionAble) {
        return getForbiddenTypes().contains(customExceptionAble);
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public CoreHttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
