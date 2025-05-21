package com.teliolabs.tejas.l2swt.exception;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends ApiException {
    private String body;
    private String message;

    public UnAuthorizedException(String message, String body) {
        super(message, body);
        this.message = message;
        this.body = body;
    }
}
