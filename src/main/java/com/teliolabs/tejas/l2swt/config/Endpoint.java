package com.teliolabs.tejas.l2swt.config;

import lombok.Data;

@Data
public class Endpoint {
    private String name;
    private String host;
    private String path;
    private String method;
}
