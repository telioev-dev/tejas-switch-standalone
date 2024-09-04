package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPathConnectionResponse {

    @JsonProperty("reqCompletionStatus")
    private int reqCompletionStatus;

    @JsonProperty("requestId")
    private int requestId;

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("clientLocation")
    private String clientLocation;

    @JsonProperty("clientUser")
    private String clientUser;

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("mdcId")
    private String mdcId;

    @JsonProperty("sequenceNum")
    private int sequenceNum;

    @JsonProperty("moreToCome")
    private boolean moreToCome;

    @JsonProperty("messages")
    private List<String> messages;

    @JsonProperty("errcde")
    private String errcde;

    @JsonProperty("errorParams")
    private String errorParams;

    @JsonProperty("Nadstring")
    private String Nadstring;

    @JsonProperty("nextTasks")
    private String nextTasks;

    @JsonProperty("items")
    private List<PathConnection> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PathConnection {

        @JsonProperty("className")
        public String className;
        @JsonProperty("id")
        public Integer pathConnectionId;
        @JsonProperty("key")
        public String key;
        @JsonProperty("guiLabel")
        public String guiLabel;
        @JsonProperty("vsClientState")
        public String vsClientState;
        @JsonProperty("vsClientId")
        public Integer vsClientId;
        @JsonProperty("connectionType")
        public String connectionType;
        @JsonProperty("receivedDate")
        public ZonedDateTime receivedDate;
        @JsonProperty("eventDate")
        public ZonedDateTime eventDate;
        @JsonProperty("orderId")
        public String orderId;
        @JsonProperty("groupOrderId")
        public String groupOrderId;
        @JsonProperty("groupOrderType")
        public String groupOrderType;
        @JsonProperty("groupOrderName")
        public String groupOrderName;
        @JsonProperty("connectionAlias")
        public String connectionAlias;
        @JsonProperty("orderStep")
        public String orderStep;
        @JsonProperty("stepState")
        public String stepState;
        @JsonProperty("orderType")
        public String orderType;
        @JsonProperty("layerRate")
        public String layerRate;
        @JsonProperty("effectiveRate")
        public String effectiveRate;
        @JsonProperty("displayProtectionType")
        public String displayProtectionType;
        @JsonProperty("protectionRole")
        public String protectionRole;
        @JsonProperty("state")
        public String state;
        @JsonProperty("operationalState")
        public String operationalState;
        @JsonProperty("category")
        public String category;
        @JsonProperty("alarmEnabling")
        public String alarmEnabling;
        @JsonProperty("alarmState")
        public String alarmState;
        @JsonProperty("alarmSeverity")
        public String alarmSeverity;
        @JsonProperty("serverAlmState")
        public String serverAlmState;
        @JsonProperty("TCMEnabled")
        public String tCMEnabled;
        @JsonProperty("TCMASAPEnabled")
        public String tCMASAPEnabled;
        @JsonProperty("nmlASAPName")
        public String nmlASAPName;
        @JsonProperty("frequency")
        public String frequency;
        @JsonProperty("orderNumber")
        public String orderNumber;
        @JsonProperty("customerName")
        public String customerName;
        @JsonProperty("serviceState")
        public String serviceState;
        @JsonProperty("provisionableWavekey")
        public String provisionableWavekey;
        @JsonProperty("a1NeName")
        public String a1NeName;
        @JsonProperty("a1PortName")
        public String a1PortName;
        @JsonProperty("z1NeName")
        public String z1NeName;
        @JsonProperty("z1PortName")
        public String z1PortName;
        @JsonProperty("a2NeName")
        public String a2NeName;
        @JsonProperty("z2NeName")
        public String z2NeName;
        @JsonProperty("a1NodeName")
        public String a1NodeName;
        @JsonProperty("z1NodeName")
        public String z1NodeName;
        @JsonProperty("aNodeId")
        public Integer aNodeId;
        @JsonProperty("zNodeId")
        public Integer zNodeId;
        @JsonProperty("a2NodeId")
        public Integer a2NodeId;
        @JsonProperty("z2NodeId")
        public Integer z2NodeId;
        @JsonProperty("aNodeType")
        public String aNodeType;
        @JsonProperty("zNodeType")
        public String zNodeType;
        @JsonProperty("a2NodeType")
        public String a2NodeType;
        @JsonProperty("z2NodeType")
        public String z2NodeType;
        @JsonProperty("aPortId")
        public Integer aPortId;
        @JsonProperty("zPortId")
        public Integer zPortId;
        @JsonProperty("a2PortId")
        public Integer a2PortId;
        @JsonProperty("z2PortId")
        public Integer z2PortId;
        @JsonProperty("aPortLabel")
        public String aPortLabel;
        @JsonProperty("zPortLabel")
        public String zPortLabel;
        @JsonProperty("a1NodeAndPort")
        public String a1NodeAndPort;
        @JsonProperty("z1NodeAndPort")
        public String z1NodeAndPort;
        @JsonProperty("omspTrailId")
        public Integer omspTrailId;
        @JsonProperty("pm24hour")
        public String pm24hour;
        @JsonProperty("pm15min")
        public String pm15min;
        @JsonProperty("nimEnabled")
        public String nimEnabled;
        @JsonProperty("isPMSupported")
        public String isPMSupported;
        @JsonProperty("connectionDirection")
        public String connectionDirection;
        @JsonProperty("is3rdPartyLink")
        public String is3rdPartyLink;
        @JsonProperty("thirdPartyName")
        public String thirdPartyName;
        @JsonProperty("thirdPartyDesc")
        public String thirdPartyDesc;
        @JsonProperty("nprTlId")
        public String nprTlId;
        @JsonProperty("sdhConnectionId")
        public String sdhConnectionId;
        @JsonProperty("sdhClientId")
        public String sdhClientId;
        @JsonProperty("isUsedInSdh")
        public String isUsedInSdh;
        @JsonProperty("fdn")
        public String fdn;
        @JsonProperty("mismatchType")
        public String mismatchType;
        @JsonProperty("clientRouteState")
        public String clientRouteState;
        @JsonProperty("createdBy")
        public String createdBy;
        @JsonProperty("endpoints")
        public Integer endpoints;
        @JsonProperty("inconsistentMismatchType")
        public String inconsistentMismatchType;
        @JsonProperty("inconsistentAcknowledgedBy")
        public String inconsistentAcknowledgedBy;
        @JsonProperty("inconsistentEventDate")
        public String inconsistentEventDate;
        @JsonProperty("inconsistentAckDate")
        public String inconsistentAckDate;
        @JsonProperty("isL0CRRD")
        public String isL0CRRD;
        @JsonProperty("isCurrentRouteRD")
        public String isCurrentRouteRD;
        @JsonProperty("serverConnId")
        public Integer serverConnId;
        @JsonProperty("serverConnectionCategory")
        public String serverConnectionCategory;
        @JsonProperty("serverProtectionType")
        public String serverProtectionType;
        @JsonProperty("serverConnectionRate")
        public String serverConnectionRate;
        @JsonProperty("sncConnId")
        public Integer sncConnId;
        @JsonProperty("sncConnectionCategory")
        public String sncConnectionCategory;
        @JsonProperty("isSlteSupported")
        public String isSlteSupported;
        @JsonProperty("isOtdrSupported")
        public String isOtdrSupported;
        @JsonProperty("allowAsonResources")
        public String allowAsonResources;
        @JsonProperty("hasAsonResources")
        public String hasAsonResources;
        @JsonProperty("switchCapability")
        public String switchCapability;
        @JsonProperty("scId")
        public String scId;
        @JsonProperty("tsCount")
        public String tsCount;
        @JsonProperty("associatednprtlid")
        public Integer associatednprtlid;
        @JsonProperty("encoding")
        public String encoding;
        @JsonProperty("autoInService")
        public String autoInService;
        @JsonProperty("autoInServiceTimer")
        public String autoInServiceTimer;
        @JsonProperty("isDarkFiberAssociated")
        public String isDarkFiberAssociated;
        @JsonProperty("fiberAscStatus")
        public String fiberAscStatus;
        @JsonProperty("isOtdrExternalSupported")
        public String isOtdrExternalSupported;
        @JsonProperty("otdrSummary")
        public String otdrSummary;
        @JsonProperty("lineGranularity")
        public Integer lineGranularity;
        @JsonProperty("containerType")
        public String containerType;
        @JsonProperty("singleFiberChannelPlan")
        public String singleFiberChannelPlan;
        @JsonProperty("olcState")
        public String olcState;
        @JsonProperty("npaUserLabel")
        public String npaUserLabel;
        @JsonProperty("spectrumWidth")
        public String spectrumWidth;
        @JsonProperty("dmValue")
        public Float dmValue;
        @JsonProperty("dmValueProtection")
        public Float dmValueProtection;
        @JsonProperty("dmStatus")
        public String dmStatus;
        @JsonProperty("revertiveMode")
        public String revertiveMode;
        @JsonProperty("waitToRestoreTime")
        public String waitToRestoreTime;
        @JsonProperty("isEline")
        public String isEline;
        @JsonProperty("lineModeProfileDescription")
        public String lineModeProfileDescription;
        @JsonProperty("testStatus")
        public String testStatus;
        @JsonProperty("discrepancy")
        public Integer discrepancy;
        @JsonProperty("discrepancyString")
        public String discrepancyString;
        @JsonProperty("isSubseaSupported")
        public String isSubseaSupported;
        @JsonProperty("invariantConnID")
        public String invariantConnID;
        @JsonProperty("servicereservation")
        public String servicereservation;
        @JsonProperty("reservationExpiryDate")
        public String reservationExpiryDate;
        @JsonProperty("is5gtributarysize")
        public String is5gtributarysize;
        @JsonProperty("connectionShape")
        public String connectionShape;
        @JsonProperty("openSNCP")
        public String openSNCP;
        @JsonProperty("diversityGroupName")
        public String diversityGroupName;
        @JsonProperty("diversityGroupId")
        public Integer diversityGroupId;
        @JsonProperty("diversityGroupOperState")
        public String diversityGroupOperState;
        @JsonProperty("diversityRule")
        public String diversityRule;
        @JsonProperty("asecontrolMode")
        public String asecontrolMode;
        @JsonProperty("subseaSupported")
        public String subseaSupported;
        @JsonProperty("npauserLabel")
        public String npauserLabel;
    }
}
