package com.teliolabs.tejas.l2swt.context;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ApplicationContext {
    private AuthContext authContext;
    

}
