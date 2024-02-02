package org.example.webfluxdemo.global.exception;

public interface CustomExceptionAble {

    String getName();

    CoreHttpStatus getHttpStatus();

    String getMessage();

}

