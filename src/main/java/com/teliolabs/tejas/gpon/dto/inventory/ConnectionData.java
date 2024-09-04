package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionData {

    @JsonProperty("CONNECTIONID")
    private int connectionId;

    @JsonProperty("CONNECTIONNAME")
    private String connectionName;

    @JsonProperty("CONTAINERTYPE")
    private String containerType;

    @JsonProperty("NCGROUPID")
    private int ncGroupId;

    @JsonProperty("GROUPCLASS")
    private int groupClass;

    @JsonProperty("CONNECTIONQUALIFIER")
    private String connectionQualifier;

    @JsonProperty("CONNECTIONALIAS")
    private String connectionAlias;

    @JsonProperty("CONNECTIONALIAS2")
    private String connectionAlias2;

    @JsonProperty("CONNECTIONRATE")
    private int connectionRate;

    @JsonProperty("SERVICERATE")
    private int serviceRate;

    @JsonProperty("MSPAPSSWITCHTYPE")
    private int mspApSwitchType;

    @JsonProperty("CONNECTIONDIRECTION")
    private int connectionDirection;

    @JsonProperty("CONNECTIONCATEGORY")
    private int connectionCategory;

    @JsonProperty("CONNECTIONSHAPE")
    private int connectionShape;

    @JsonProperty("CONNECTIONSTATE")
    private int connectionState;

    @JsonProperty("ADAPTED")
    private int adapted;

    @JsonProperty("SERVICECONNECTION")
    private int serviceConnection;

    @JsonProperty("ROUTESELECTIONMODE")
    private int routeSelectionMode;

    @JsonProperty("IGNOREALARM")
    private int ignoreAlarm;

    @JsonProperty("CUSTOMERNAME")
    private String customerName;

    @JsonProperty("CUSTOMERPRIORITY")
    private String customerPriority;

    @JsonProperty("VPNID")
    private String vpnId;

    @JsonProperty("ALIENFREQ")
    private String alienFreq;

    @JsonProperty("QUALITY")
    private String quality;

    @JsonProperty("DISCREPANCY")
    private int discrepancy;

    @JsonProperty("CREATIONTIME")
    private long creationTime;

    @JsonProperty("CREATEDBY")
    private int createdBy;

    @JsonProperty("SERVERGROUPID")
    private int serverGroupId;

    @JsonProperty("CAPACITY")
    private int capacity;

    @JsonProperty("EFFECTIVERATE")
    private String effectiveRate;

    @JsonProperty("PROTECTIONROLE")
    private int protectionRole;

    @JsonProperty("PROTECTIONTYPE")
    private int protectionType;

    @JsonProperty("REVERTIVEMODE")
    private int revertiveMode;

    @JsonProperty("SERVERLAYERRATE")
    private int serverLayerRate;

    @JsonProperty("CONNECTIONTYPE")
    private int connectionType;

    @JsonProperty("TANDEMASSOCIATIONTAG")
    private int tandemAssociationTag;

    @JsonProperty("CONNECTIONNAMEFORMAT")
    private int connectionNameFormat;

    @JsonProperty("DISPLAYSERVICERATE")
    private int displayServiceRate;

    @JsonProperty("MANUALFAULTMARKINGSTATUS")
    private int manualFaultMarkingStatus;

    @JsonProperty("LOCKEDCAPACITY")
    private int lockedCapacity;

    @JsonProperty("RESTORATIONTRIGGER")
    private int restorationTrigger;

    @JsonProperty("INDIRECTSNCNAME")
    private String indirectSncName;

    @JsonProperty("SDHCONNECTIONID")
    private String sdhConnectionId;

    @JsonProperty("SDHCLIENTID")
    private String sdhClientId;

    @JsonProperty("SDHROUTEMODTIME")
    private String sdhRouteModTime;

    @JsonProperty("OMSPTRAILID")
    private int omsPtrailId;

    @JsonProperty("SYSTEMLABEL")
    private String systemLabel;

    @JsonProperty("TCMSTATUS")
    private int tcmStatus;

    @JsonProperty("SERVICESTATE")
    private int serviceState;

    @JsonProperty("OPERATIONALSTATE")
    private int operationalState;

    @JsonProperty("INVARIANTCONNID")
    private int invariantConnId;

    @JsonProperty("ObjectIdentifier")
    private int objectIdentifier;

    @JsonProperty("CONNECTIONLEVEL")
    private int connectionLevel;

    @JsonProperty("NCGIDFORMDFY")
    private int ncgIdForMdfy;

    @JsonProperty("ASSOCIATEDNPRTLID")
    private int associatedNpRtlId;

    @JsonProperty("PREFERREDRESTORATIONMODE")
    private String preferredRestorationMode;

    @JsonProperty("BAND")
    private String band;

    @JsonProperty("hasAsonResources")
    private String hasAsonResources;

    @JsonProperty("Profile")
    private String profile;

    @JsonProperty("PROVISIONABLEWAVEKEY")
    private String provisionableWaveKey;

    @JsonProperty("WAVEKEYTYPE")
    private String waveKeyType;

    @JsonProperty("SPECTRUMWIDTH")
    private String spectrumWidth;

    @JsonProperty("OFFSET")
    private String offset;

    @JsonProperty("FECTYPE")
    private String fecType;

    @JsonProperty("ENCODING")
    private String encoding;

    @JsonProperty("WAVESHAPE")
    private String waveShape;

    @JsonProperty("PHASEENCODING")
    private String phaseEncoding;

    @JsonProperty("ASAPID")
    private int asapId;

    @JsonProperty("NETWORKSLICEID")
    private String networkSliceId;

    @JsonProperty("HOLDOFFTIME")
    private String holdOffTime;

    @JsonProperty("WAITTORESTORETIME")
    private String waitToRestoreTime;

    @JsonProperty("RESTOREPRIORITY")
    private String restorePriority;

    @JsonProperty("SWITCHDIRECTION")
    private String switchDirection;

    @JsonProperty("ANE1NAME")
    private String ane1Name;

    @JsonProperty("ZNE1NAME")
    private String zne1Name;

    @JsonProperty("ATP1NAME")
    private String atp1Name;

    @JsonProperty("A1PARENTTP")
    private String a1ParentTp;

    @JsonProperty("A1CTPTIMESLOT")
    private String a1CtpTimeSlot;

    @JsonProperty("A1TIMESLOTMAP")
    private String a1TimeSlotMap;

    @JsonProperty("ZTP1NAME")
    private String ztp1Name;

    @JsonProperty("Z1PARENTTP")
    private String z1ParentTp;

    @JsonProperty("Z1CTPTIMESLOT")
    private String z1CtpTimeSlot;

    @JsonProperty("Z1TIMESLOTMAP")
    private String z1TimeSlotMap;

    @JsonProperty("ANE2NAME")
    private String ane2Name;

    @JsonProperty("ZNE2NAME")
    private String zne2Name;

    @JsonProperty("ATP2NAME")
    private String atp2Name;

    @JsonProperty("A2PARENTTP")
    private String a2ParentTp;

    @JsonProperty("A2CTPTIMESLOT")
    private String a2CtpTimeSlot;

    @JsonProperty("A2TIMESLOTMAP")
    private String a2TimeSlotMap;

    @JsonProperty("ZTP2NAME")
    private String ztp2Name;

    @JsonProperty("Z2PARENTTP")
    private String z2ParentTp;

    @JsonProperty("Z2CTPTIMESLOT")
    private String z2CtpTimeSlot;

    @JsonProperty("Z2TIMESLOTMAP")
    private String z2TimeSlotMap;

    @JsonProperty("ALLOWASONRESOURCES")
    private String allowAsonResources;
}