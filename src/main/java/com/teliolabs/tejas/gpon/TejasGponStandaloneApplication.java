package com.teliolabs.tejas.gpon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.teliolabs.tejas.gpon","com.teliolabs.tejas.gpon.dto"})
public class TejasGponStandaloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(TejasGponStandaloneApplication.class, args);
    }

}
