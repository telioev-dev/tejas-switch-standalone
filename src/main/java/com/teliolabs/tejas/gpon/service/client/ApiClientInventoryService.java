package com.teliolabs.tejas.gpon.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teliolabs.tejas.gpon.config.ApplicationConfig;
import com.teliolabs.tejas.gpon.config.Endpoint;
import com.teliolabs.tejas.gpon.config.NetworkManagerConfig;
import com.teliolabs.tejas.gpon.context.ApplicationContext;
import com.teliolabs.tejas.gpon.util.EndpointConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class ApiClientInventoryService extends BaseApiClientService {

    private final ObjectMapper objectMapper;

    @Autowired
    public ApiClientInventoryService(ApplicationContext applicationContext, WebClient.Builder webClientBuilder, ApplicationConfig applicationConfig, ObjectMapper objectMapper) {
        super(applicationContext, webClientBuilder, applicationConfig);
        this.objectMapper = objectMapper;
    }


    public String fetchTejasCircuitData() {
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
        Endpoint endpoint = networkManager.getEndpoints().stream().filter(e -> e.getName().
                equals(EndpointConstants.GET_CIRCUIT_DATA)).findFirst().get();
        String nodes = webClientBuilder.
                baseUrl(getEndpointHost(endpoint)).
                build().method(resolveMethod(endpoint)).
                uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint)).queryParam("category", "ont").
                        queryParam("size", "500").
                        build("TTLEMS-GPON-1|10.76.198.217")).
                headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken())).
                retrieve().
                bodyToMono(String.class).
                block();
        log.info("nodes: {}", nodes);
        return nodes;
    }
}