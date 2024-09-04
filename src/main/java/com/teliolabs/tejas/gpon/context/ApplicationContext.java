package com.teliolabs.tejas.gpon.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ApplicationContext {
    private AuthContext authContext;
}
