package com.teliolabs.tejas.l2swt.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private String body;

    public ApiException(String message, String body) {
        super(message);
        this.body = body;
    }
}
