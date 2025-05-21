package com.teliolabs.tejas.l2swt.util;


import org.springframework.http.HttpMethod;

import com.teliolabs.tejas.l2swt.config.Endpoint;

public class EndpointUtil {

    public static HttpMethod resolveMethod(Endpoint endpoint) {
        final String METHOD = endpoint.getMethod();
        switch (METHOD) {
            case "POST":
                return HttpMethod.POST;
            case "GET":
                return HttpMethod.GET;
            case "PUT":
                return HttpMethod.PUT;
            case "DELETE":
                return HttpMethod.DELETE;
            case "PATCH":
                return HttpMethod.PATCH;
            case "OPTIONS":
                return HttpMethod.OPTIONS;
            case "HEAD":
                return HttpMethod.HEAD;
            default:
                throw new IllegalArgumentException("Unknown HTTP Method " + METHOD);
        }
    }
}
