package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalConnection {
    @JsonProperty("ObjectId")
    private String objectId;

    @JsonProperty("EventType")
    private String eventType;

    @JsonProperty("ClassType")
    private String classType;

    @JsonProperty("IdClass")
    private Integer idClass;

    @JsonProperty("sharedRiskGroups")
    private List<String> sharedRiskGroups;

    @JsonProperty("a2PortLabel")
    private String a2PortLabel;

    @JsonProperty("aNodeId")
    private Integer aNodeId;

    @JsonProperty("aPortLabel")
    private String aPortLabel;

    @JsonProperty("aTozFiberLength")
    private String aTozFiberLength;

    @JsonProperty("aTozSpanLoss")
    private String aTozSpanLoss;

    @JsonProperty("aTozSpanLossL")
    private String aTozSpanLossL;

    @JsonProperty("alarmStatus")
    private String alarmStatus;

    @JsonProperty("allPathCost")
    private Integer allPathCost;

    @JsonProperty("asapId")
    private Integer asapId;

    @JsonProperty("asapName")
    private String asapName;

    @JsonProperty("asonAdmState")
    private String asonAdmState;

    @JsonProperty("asonAutoRestoration")
    private String asonAutoRestoration;

    @JsonProperty("asonWTR")
    private Integer asonWTR;

    @JsonProperty("clientSignalType")
    private String clientSignalType;

    @JsonProperty("clientTechnology")
    private String clientTechnology;

    @JsonProperty("colorProfileId")
    private Integer colorProfileId;

    @JsonProperty("commissionedStatus")
    private String commissionedStatus;

    @JsonProperty("flexGridSupport")
    private String flexGridSupport;

    @JsonProperty("aseLongLinkStatus")
    private String aseLongLinkStatus;

    @JsonProperty("id")
    private int connectionId;

    @JsonProperty("key")
    private String key;

    @JsonProperty("className")
    private String className;

    @JsonProperty("currentFrequency")
    private String currentFrequency;

    @JsonProperty("defTime")
    private Long defTime;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("implSt")
    private String implSt;

    @JsonProperty("interShelf")
    private String interShelf;

    @JsonProperty("spanType")
    private String spanType;

    @JsonProperty("gridType")
    private String gridType;

    @JsonProperty("connecAdminState")
    private String connecAdminState;

    @JsonProperty("lastCalculatedSpanLossDate")
    private String lastCalculatedSpanLossDate;

    @JsonProperty("latency")
    private Integer latency;

    @JsonProperty("linkType")
    private String linkType;

    @JsonProperty("nadString")
    private String nadString;

    @JsonProperty("operationalState")
    private String operationalState;

    @JsonProperty("otdrScanStatus")
    private String otdrScanStatus;

    @JsonProperty("otdrSupported")
    private String otdrSupported;

    @JsonProperty("otdrDfSupported")
    private String otdrDfSupported;

    @JsonProperty("otdrExtAssocSupported")
    private String otdrExtAssocSupported;

    @JsonProperty("otnMapper")
    private Integer otnMapper;

    @JsonProperty("otuSignalType")
    private String otuSignalType;

    @JsonProperty("overlaySupport")
    private String overlaySupport;

    @JsonProperty("pm15m")
    private String pm15m;

    @JsonProperty("pm24h")
    private String pm24h;

    @JsonProperty("serviceState")
    private String serviceState;

    @JsonProperty("shape")
    private String shape;

    @JsonProperty("sltePresent")
    private String sltePresent;

    @JsonProperty("srgPresent")
    private String srgPresent;

    @JsonProperty("thrdPartyNtwDescription")
    private String thrdPartyNtwDescription;

    @JsonProperty("thrdPartyNtwName")
    private String thrdPartyNtwName;

    @JsonProperty("txFrequency")
    private String txFrequency;

    @JsonProperty("ultraLongSpan")
    private String ultraLongSpan;

    @JsonProperty("updatedTime")
    private String updatedTime;

    @JsonProperty("guiLabel")
    private String guiLabel;

    @JsonProperty("wdmConnectionType")
    private String wdmConnectionType;

    @JsonProperty("wdmLinkType")
    private String wdmLinkType;

    @JsonProperty("wdmProtConnType")
    private String wdmProtConnType;

    @JsonProperty("wrkSt")
    private String wrkSt;

    @JsonProperty("z2PortLabel")
    private String z2PortLabel;

    @JsonProperty("zNodeId")
    private Integer zNodeId;

    @JsonProperty("zPortLabel")
    private String zPortLabel;

    @JsonProperty("zToaFiberLength")
    private String zToaFiberLength;

    @JsonProperty("zToaSpanLoss")
    private String zToaSpanLoss;

    @JsonProperty("zToaSpanLossL")
    private String zToaSpanLossL;

    @JsonProperty("colorProfileName")
    private String colorProfileName;

    @JsonProperty("colorString")
    private String colorString;

    @JsonProperty("band")
    private String band;

    @JsonProperty("protection")
    private String protection;

    @JsonProperty("otdrSummary")
    private String otdrSummary;

    @JsonProperty("olcState")
    private String olcState;

    @JsonProperty("npaUserLabel")
    private String npaUserLabel;

    @JsonProperty("mostRecentNote")
    private String mostRecentNote;

    @JsonProperty("fiberAscStatus")
    private String fiberAscStatus;

    @JsonProperty("isDarkFiberAssociated")
    private String isDarkFiberAssociated;

    @JsonProperty("clusterLink")
    private String clusterLink;

    @JsonProperty("asapStatus")
    private String asapStatus;

    @JsonProperty("lowlinkUtilThreshold")
    private String lowlinkUtilThreshold;

    @JsonProperty("highlinkUtilThreshold")
    private String highlinkUtilThreshold;

    @JsonProperty("linkUtilProfileName")
    private String linkUtilProfileName;

    @JsonProperty("linkUtilRatio")
    private String linkUtilRatio;

    @JsonProperty("olpAssociatedLink")
    private String olpAssociatedLink;

    @JsonProperty("noCommandToNe")
    private String noCommandToNe;

    @JsonProperty("consistSt")
    private String consistSt;

    @JsonProperty("subseaLink")
    private String subseaLink;
}
