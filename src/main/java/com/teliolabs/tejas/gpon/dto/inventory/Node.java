package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private Integer idClass;
    private String alarmSyntesis;
    private String asonCtrPlaneType;
    private String cTAccessStatus;
    private String comment1;
    private String comment2;
    private String communicationState;
    private String ethPresent;
    private String hierarchySubnet;
    private String isMultiNEs;
    private String localization;
    private String latitude;
    private String longitude;
    private String altitude;
    private String mibAlignmentState;
    private String neAlignment;
    private String neSubType;
    @JsonProperty("id")
    private Integer nodeId;
    private String key;
    private String loopbackIp;
    private String secondaryAddress;
    private String className;
    private String otnConfDownldSt;
    private Integer parentId;
    private String parentLabel;
    private String position;
    private String sdhPresent;
    private String supervisionState;
    private String guiLabel;
    private String version;
    private String wdmPresent;
    private String confDownldSt;
    private String nadString;
    private String productName;
    private String reachable;
    private String shortProductName;
    private String siteName;
    private String nodeType;
    private String emlNeType;
    private String comment3;
    private String associationPresent;
    private String neSubRelease;
    private String associatedPtnNeId;
    private String comments;
    private String actualRelease;
    private String newCommunicationState;
    private String newSupervisionState;
    private String alignmentState;
    private Long creationDate;
    private String latestNote;
    private String scheduledForGri;
    private String systemAbnormalState;
    private String systemMode;
}
