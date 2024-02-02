package org.example.webfluxdemo.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CustomExceptionAble customExceptionAble;


    public CustomException(CustomExceptionAble customExceptionAble) {
        this(customExceptionAble, null);
    }

    public CustomException(CustomExceptionAble customExceptionAble, String message) {
        super(message);
        this.customExceptionAble = customExceptionAble;
    }

}

