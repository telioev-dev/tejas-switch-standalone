package com.teliolabs.tejas.gpon.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthContext {
    private String accessToken;
}
