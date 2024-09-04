package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratedResponse {

    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader;

    @JsonProperty("responseFiles")
    private List<String> responseFiles;

    @Data
    public static class ResponseHeader {

        @JsonProperty("originatorApp")
        private String originatorApp;

        @JsonProperty("objectType")
        private String objectType;

        @JsonProperty("objectScope")
        private String objectScope;

        @JsonProperty("responseStatus")
        private String responseStatus;

        @JsonProperty("fileGenerationTime")
        private String fileGenerationTime;

        @JsonProperty("errorCode")
        private String errorCode;

        @JsonProperty("errorReason")
        private String errorReason;

        @JsonProperty("errorParams")
        private String errorParams;
    }
}