package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class End {
    @JsonProperty("NEName")
    private String neName;
    @JsonProperty("PortName")
    private String portName;
    @JsonProperty("PortRate")
    private String portRate;
    @JsonProperty("txRepeater")
    private String txRepeater;
    @JsonProperty("rxRepeater")
    private String rxRepeater;
    @JsonProperty("oscWC")
    private String oscWC;
}
