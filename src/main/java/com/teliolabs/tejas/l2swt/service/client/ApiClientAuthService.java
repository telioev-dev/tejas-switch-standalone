package com.teliolabs.tejas.l2swt.service.client;

import java.time.LocalDateTime;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.teliolabs.tejas.l2swt.config.ApplicationConfig;
import com.teliolabs.tejas.l2swt.config.Endpoint;
import com.teliolabs.tejas.l2swt.config.NetworkManagerConfig;
import com.teliolabs.tejas.l2swt.context.ApplicationContext;
import com.teliolabs.tejas.l2swt.context.AuthContext;
import com.teliolabs.tejas.l2swt.dto.auth.AuthResponse;
import com.teliolabs.tejas.l2swt.util.EndpointConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiClientAuthService extends BaseApiClientService {

    // token expiry buffer
    private static final int TOKEN_EXPIRY_MINUTES = 14;

    // local cached expiry
    private LocalDateTime tokenExpiryTime;

    @Autowired
    public ApiClientAuthService(
            ApplicationContext applicationContext,
            WebClient.Builder webClientBuilder,
            ApplicationConfig applicationConfig) {

        super(applicationContext, webClientBuilder, applicationConfig);
    }

    /**
     * Returns valid token.
     * Re-authenticates automatically if token expired.
     */
    public synchronized String getValidToken() {

        AuthContext authContext =
                applicationContext.getAuthContext();

        // no token yet
        if (authContext == null
                || authContext.getAccessToken() == null) {

            log.info("No token found. Authenticating...");
            authenticate();

            return applicationContext
                    .getAuthContext()
                    .getAccessToken();
        }

        // token expired
        if (tokenExpiryTime == null
                || LocalDateTime.now()
                        .isAfter(tokenExpiryTime)) {

            log.info("Token expired. Re-authenticating...");
            authenticate();
        }

        return applicationContext
                .getAuthContext()
                .getAccessToken();
    }

    /**
     * Performs authentication and updates token.
     */
    public synchronized void authenticate() {

        NetworkManagerConfig networkManagerConfig =
                applicationConfig.getNetworkManager();

        String userName =
                networkManagerConfig
                        .getAuthentication()
                        .getUsername();

        String password =
                networkManagerConfig
                        .getAuthentication()
                        .getPassword();

        Endpoint endpoint =
                networkManagerConfig.getEndpoints().stream()
                        .filter(e ->
                                e.getName()
                                        .equals(
                                                EndpointConstants.AUTH))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Authentication endpoint not found"));

        String authEndpoint = endpoint.getPath();

        log.info("Authenticating with userName: {}", userName);

        try {

            AuthResponse authResponse =
                    webClientBuilder
                            .baseUrl(networkManagerConfig.getHost())
                            .build()
                            .post()
                            .uri(authEndpoint)
                            .header("user", userName)
                            .header("password", password)
                            .retrieve()
                            .bodyToMono(AuthResponse.class)
                            .block();

            log.info("AuthResponse received");

            if (authResponse != null
                    && ObjectUtils.isNotEmpty(
                            authResponse.getAccessToken())) {

                AuthContext authContext =
                        AuthContext.builder()
                                .accessToken(
                                        authResponse.getAccessToken())
                                .build();

                applicationContext.setAuthContext(authContext);

                // IMPORTANT
                tokenExpiryTime =
                        LocalDateTime.now()
                                .plusMinutes(
                                        TOKEN_EXPIRY_MINUTES);

                log.info(
                        "Authentication successful. Token updated. Expiry: {}",
                        tokenExpiryTime);

            } else {

                log.error(
                        "Authentication failed. No token received.");

                throw new RuntimeException(
                        "Failed to authenticate: No access token in response.");
            }

        } catch (Exception e) {

            log.error("Error during authentication", e);

            throw new RuntimeException(
                    "Authentication failed",
                    e);
        }
    }
}