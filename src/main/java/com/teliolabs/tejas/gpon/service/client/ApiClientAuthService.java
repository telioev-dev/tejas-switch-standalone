package com.teliolabs.tejas.gpon.service.client;


import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.teliolabs.tejas.gpon.config.ApplicationConfig;
import com.teliolabs.tejas.gpon.config.Endpoint;
import com.teliolabs.tejas.gpon.config.NetworkManagerConfig;
import com.teliolabs.tejas.gpon.context.ApplicationContext;
import com.teliolabs.tejas.gpon.context.AuthContext;
import com.teliolabs.tejas.gpon.dto.auth.AuthResponse;
import com.teliolabs.tejas.gpon.util.EndpointConstants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ApiClientAuthService extends BaseApiClientService {

    @Autowired
    public ApiClientAuthService(ApplicationContext applicationContext, WebClient.Builder webClientBuilder, ApplicationConfig applicationConfig) {
        super(applicationContext, webClientBuilder, applicationConfig);
    }

    public void authenticate() {
        NetworkManagerConfig networkManagerConfig = applicationConfig.getNetworkManager();
        String userName = networkManagerConfig.getAuthentication().getUsername();
        String password = networkManagerConfig.getAuthentication().getPassword();

        Endpoint endpoint = networkManagerConfig.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.AUTH))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Authentication endpoint not found"));

        String authEndpoint = endpoint.getPath();

        log.info("Authenticating with userName: {}", userName);

        try {
            AuthResponse authResponse = webClientBuilder
                    .baseUrl(networkManagerConfig.getHost())
                    .build()
                    .post()
                    .uri(authEndpoint)
                    .header("user", userName)
                    .header("password", password)
                    .retrieve()
                    .bodyToMono(AuthResponse.class)
                    .block();

            log.info("AuthResponse received: {}", authResponse);

            if (authResponse != null && ObjectUtils.isNotEmpty(authResponse.getAccessToken())) {
                AuthContext authContext = AuthContext.builder()
                        .accessToken(authResponse.getAccessToken())
                        .build();
                applicationContext.setAuthContext(authContext);
                log.info("Authentication successful. Token updated.");
            } else {
                log.error("Authentication failed. No token received.");
                throw new RuntimeException("Failed to authenticate: No access token in response.");
            }
        } catch (Exception e) {
            log.error("Error during authentication", e);
            throw new RuntimeException("Authentication failed", e);
        }
    }  

}
