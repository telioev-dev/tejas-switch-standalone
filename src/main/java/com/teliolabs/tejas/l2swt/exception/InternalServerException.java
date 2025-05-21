package com.teliolabs.tejas.l2swt.exception;

import lombok.Getter;

@Getter
public class InternalServerException extends ApiException {
    private String body;
    private String message;

    public InternalServerException(String message, String body) {
        super(message, body);
        this.body = body;
        this.message = message;
    }
}
