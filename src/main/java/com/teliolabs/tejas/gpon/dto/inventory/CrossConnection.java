package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrossConnection {

    @JsonProperty("userLabel")
    private String userLabel;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("rate")
    private String rate;

    @JsonProperty("sncType")
    private String sncType;

    @JsonProperty("frequency")
    private String frequency;

    @JsonProperty("fixedXC")
    private String fixedXC;

    @JsonProperty("width")
    private String width;

    @JsonProperty("guardband")
    private String guardband;

    @JsonProperty("usagelabel")
    private String usagelabel;

    @JsonProperty("connectionName")
    private String connectionName;

    @JsonProperty("sncid")
    private String sncId;

    @JsonProperty("xcId")
    private String xcId;

    @JsonProperty("zaXcId")
    private String zaXcId;

    @JsonProperty("waveKeyConfiguration")
    private String waveKeyConfiguration;

    @JsonProperty("zaWaveKeyConfiguration")
    private String zaWaveKeyConfiguration;

    @JsonProperty("azWaveKey1")
    private String azWaveKey1;

    @JsonProperty("azWaveKey2")
    private String azWaveKey2;

    @JsonProperty("zaWaveKey1")
    private String zaWaveKey1;

    @JsonProperty("zaWaveKey2")
    private String zaWaveKey2;

    @JsonProperty("waveKeyingPref")
    private String waveKeyingPref;

    @JsonProperty("zawaveKeyingPref")
    private String zaWaveKeyingPref;

    @JsonProperty("azDupsUnlocked")
    private String azDupsUnlocked;

    @JsonProperty("zaDupsUnlocked")
    private String zaDupsUnlocked;

    @JsonProperty("ObjectIdentifier")
    private String objectIdentifier;

    @JsonProperty("A2End")
    private EndPoint a2End;

    @JsonProperty("Z1End")
    private EndPoint z1End;

    @JsonProperty("Z2End")
    private EndPoint z2End;

    @JsonProperty("A1End")
    private EndPoint a1End;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EndPoint {
        @JsonProperty("portRole")
        private String portRole;

        @JsonProperty("parentPortName")
        private String parentPortName;

        @JsonProperty("NEName")
        private String neName;

        @JsonProperty("PortName")
        private String portName;
    }
}

