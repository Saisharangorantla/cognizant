package com.acme.productservice.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not part of the "real" API - just a quick way to prove this instance
 * actually picked up its properties from config-server rather than from
 * a local file.
 */
@RestController
public class ConfigCheckController {

    @Value("${greeting.message:not set}")
    private String greetingMessage;

    @GetMapping("/config-check")
    public String configCheck() {
        return greetingMessage;
    }
}
