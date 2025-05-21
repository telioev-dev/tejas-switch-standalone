package com.teliolabs.tejas.l2swt.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends ApiException {
    private String body;
    private String message;

    public BadRequestException(String message, String body) {
        super(message, body);
        this.body = body;
        this.message = message;
    }
}
