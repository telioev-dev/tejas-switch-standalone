package com.teliolabs.tejas.gpon.service.client;


import com.teliolabs.tejas.gpon.config.ApplicationConfig;
import com.teliolabs.tejas.gpon.context.ApplicationContext;
import com.teliolabs.tejas.gpon.util.EndpointUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.teliolabs.tejas.gpon.config.*;

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
