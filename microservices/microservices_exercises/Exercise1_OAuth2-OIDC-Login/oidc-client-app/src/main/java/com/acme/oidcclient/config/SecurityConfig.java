package com.acme.oidcclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Modern (Spring Security 6 / lambda DSL) replacement for the
 * WebSecurityConfigurerAdapter-based config from the original exercise -
 * that base class was removed in Spring Security 5.7+, so a
 * SecurityFilterChain bean is the way to do this now.
 * <p>
 * The home page ("/") is left open so visitors can see a login link
 * before authenticating; everything else requires a logged-in user.
 * oauth2Login() is all it takes to get a full "Sign in with <provider>"
 * flow, including the redirect, callback handling, and building the
 * authenticated principal.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/error").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
