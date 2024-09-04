package com.teliolabs.tejas.gpon.service.client;


import com.teliolabs.tejas.gpon.config.ApplicationConfig;
import com.teliolabs.tejas.gpon.config.Endpoint;
import com.teliolabs.tejas.gpon.config.NetworkManagerConfig;
import com.teliolabs.tejas.gpon.context.ApplicationContext;
import com.teliolabs.tejas.gpon.context.AuthContext;
import com.teliolabs.tejas.gpon.dto.auth.AuthResponse;
import com.teliolabs.tejas.gpon.util.EndpointConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

        Endpoint endpoint = networkManagerConfig.getEndpoints().stream().filter(e -> e.getName().equals(EndpointConstants.AUTH)).findFirst().get();
        String authEndpoint = endpoint.getPath();

        log.info("userName: {}", userName);
        log.info("password: {}", password);

        AuthResponse authResponse = webClientBuilder.
                baseUrl(applicationConfig.getNetworkManager().getHost()).
                build().
                post().
                uri(authEndpoint).
                header("user", userName).
                header("password", password).
                retrieve().
                bodyToMono(AuthResponse.class).
                block();

        log.info("AuthResponse {}", authResponse);

        if (ObjectUtils.isNotEmpty(authResponse)) {
            AuthContext authContext = AuthContext.builder().accessToken(authResponse.getAccessToken()).build();
            applicationContext.setAuthContext(authContext);
        }
    }
}
