package com.acme.oidcclient.web;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The original exercise returned the raw java.security.Principal, which
 * for an OAuth2/OIDC login is really an OidcUser under the hood. We ask
 * for it directly here so we can pull out the claims that are actually
 * useful to show (name, email, subject) instead of a generic toString().
 * Hitting this endpoint while unauthenticated triggers the OAuth2 login
 * redirect automatically, since it's covered by anyRequest().authenticated().
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public Map<String, Object> user(OidcUser oidcUser) {
        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("subject", oidcUser.getSubject());
        profile.put("name", oidcUser.getFullName());
        profile.put("email", oidcUser.getEmail());
        profile.put("claims", oidcUser.getClaims());
        return profile;
    }
}
