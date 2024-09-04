package com.teliolabs.tejas.gpon.util;


import com.teliolabs.tejas.gpon.config.Endpoint;
import org.springframework.http.HttpMethod;

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
