package com.acme.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SlowThirdPartyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlowThirdPartyServiceApplication.class, args);
    }
}
