package com.acme.oidcclient.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public landing page. If only one OAuth2 client registration is
 * configured, Spring Security will redirect an unauthenticated user
 * straight to the provider when they hit a protected URL - this page
 * mainly exists so there's somewhere to land after logging out, or to
 * link to /user manually.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "OIDC client app is running. Visit /user to sign in and see your profile.";
    }
}
