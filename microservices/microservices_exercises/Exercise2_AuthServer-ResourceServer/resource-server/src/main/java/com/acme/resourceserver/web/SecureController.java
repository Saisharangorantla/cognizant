package com.acme.resourceserver.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Two endpoints: one that just needs any valid access token, and one
 * that additionally checks for the "message.read" scope granted to the
 * messaging-client registered in auth-server. Scope-based authorization
 * checks are automatically available as SCOPE_xxx authorities once
 * oauth2ResourceServer().jwt() is configured.
 */
@RestController
public class SecureController {

    @GetMapping("/secure")
    public String secure() {
        return "This is a secure endpoint";
    }

    @GetMapping("/secure/message")
    @PreAuthorize("hasAuthority('SCOPE_message.read')")
    public String secureMessage(Jwt jwt) {
        return "Hello, " + jwt.getSubject() + ". You have the message.read scope.";
    }
}
