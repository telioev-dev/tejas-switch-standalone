package com.teliolabs.tejas.l2swt.service.client;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.teliolabs.tejas.l2swt.config.*;
import com.teliolabs.tejas.l2swt.context.ApplicationContext;
import com.teliolabs.tejas.l2swt.util.EndpointUtil;

@Service
@RequiredArgsConstructor
public abstract class BaseApiClientService {

    protected final ApplicationContext applicationContext;
    protected final WebClient.Builder webClientBuilder;
    protected final ApplicationConfig applicationConfig;


    protected String getEndpointHost(Endpoint endpoint) {
        return endpoint.getHost();
    }

    protected String getEndpointPath(Endpoint endpoint) {
        return endpoint.getPath();
    }

    protected HttpMethod resolveMethod(Endpoint endpoint) {
        return EndpointUtil.resolveMethod(endpoint);
    }
}
