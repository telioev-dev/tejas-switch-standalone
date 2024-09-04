package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteData {

    @JsonProperty("SERVERID")
    private int serverId;

    @JsonProperty("NCGROUPID")
    private int ncGroupId;

    @JsonProperty("SERVERLINKNAME")
    private String serverLinkName;

    @JsonProperty("CONTAINERTYPE")
    private String containerType;

    @JsonProperty("CONNECTIONTYPE")
    private int connectionType;

    @JsonProperty("CONNECTIONRATE")
    private int connectionRate;

    @JsonProperty("PROTECTIONTYPE")
    private int protectionType;

    @JsonProperty("IMMEDIATESERVER")
    private String immediateServer;

    @JsonProperty("FREQUENCY")
    private int[] frequency;

    @JsonProperty("ROUTEPROTECTIONTYPE")
    private int[] routeProtectionType;

    @JsonProperty("ROUTESEQUENCE")
    private int[] routeSequence;
}