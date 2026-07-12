package com.acme.jwtauth.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reachable only with a valid Bearer token, thanks to JwtTokenFilter
 * populating the SecurityContext before this request is dispatched.
 */
@RestController
public class SecureController {

    @GetMapping("/secure")
    public String secure(Authentication authentication) {
        return "This is a secure endpoint. Hello, " + authentication.getName() + "!";
    }
}
