package com.teliolabs.tejas.gpon.dto.inventory;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionRoutesResponse {

    @JsonProperty("Response")
    private Response Response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("All_connections")
        private List<All_Connections> All_connections;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class All_Connections {
        @JsonProperty("connectionData")
        private List<ConnectionData> connectionData;
        @JsonProperty("routeData")
        private List<RouteData> routeData;
    }
}
