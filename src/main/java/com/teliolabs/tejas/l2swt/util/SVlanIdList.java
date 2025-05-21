package com.teliolabs.tejas.l2swt.util;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SVlanIdList {
    @JsonProperty("type")
    public String type;
    @JsonProperty("vlan-id-list")
    public ArrayList<VlanIdList> vlanIdList;
}
