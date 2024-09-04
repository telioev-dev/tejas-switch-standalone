package com.teliolabs.tejas.gpon.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topology {
    private String connectionName;
    private String connectionRate;
    private String allocationCost;
    private String connectionState;
    private String direction;
    private String operationalState;
    private String serviceState;
    private String otnLatency;
    private String a2zFiberLength;
    private String z2aFiberLength;
    private String a2zFiberType;
    private String z2aFiberType;
    @JsonProperty("A2End")
    private End a2End;
    @JsonProperty("Z1End")
    private End z1End;
    @JsonProperty("Z2End")
    private End z2End;
    @JsonProperty("A1End")
    private End a1End;
}
