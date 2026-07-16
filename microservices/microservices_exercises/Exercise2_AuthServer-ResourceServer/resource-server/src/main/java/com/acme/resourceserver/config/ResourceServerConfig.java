package com.acme.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Modern replacement for the exercise's WebSecurityConfigurerAdapter
 * subclass. Every request needs a valid, signed JWT; validation happens
 * automatically against auth-server's published JWK set, resolved from
 * the issuer-uri in application.yml (Spring performs OIDC/OAuth2
 * discovery against that issuer the first time a token needs checking).
 * {@code @EnableMethodSecurity} turns on the {@code @PreAuthorize} scope
 * check used on the /secure/message endpoint.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
