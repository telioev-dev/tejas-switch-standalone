package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WdmPort {
    @JsonProperty("IdClass")
    private int idClass;
    private String accessPortRole;
    private String alarmStatus;
    private String boardType;
    private String cardType;
    private String clonePortPresent;
    private String compModule;
    private String consistSt;
    private String currentFrequency;
    private String designatedPortMode;
    private String direction;
    private String displayLabel;
    private String encoding;
    private String fdnMapper;
    private String fecType;
    private String frequency;
    private String interworkingMode;
    private String invMuxPrtId;
    private String involvedOnInternalCable;
    private String involvedOnPhyConn;
    private int neId;
    private String neLabel;
    private int nodeId;
    private String nodeLabel;
    private String olcState;
    private String operationalState;
    private String otuSignalType;
    private String portBitRate;
    @JsonProperty("id")
    private int portId;
    private String key;
    private String className;
    private String primaryState;
    private String productName;
    private String secondaryState;
    private String uploadSt;
    private String usedDir;
    private String usedOnOtn;
    private String guiLabel;
    private String wdmClientSignalType;
    private String wdmInterfaceType;
    private String wdmPhysicalPortRate;
    private String wdmPortType;
    private String wdmTransmissionMode;
    private int width;
}
