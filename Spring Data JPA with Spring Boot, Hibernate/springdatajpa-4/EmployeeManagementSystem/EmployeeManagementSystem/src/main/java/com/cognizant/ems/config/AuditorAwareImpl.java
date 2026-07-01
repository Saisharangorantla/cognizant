package com.cognizant.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Exercise 7 — Entity Auditing.
 *
 * Supplies the "current user" used to populate @CreatedBy / @LastModifiedBy.
 *
 * In a real application this would read the authenticated principal from
 * Spring Security's SecurityContextHolder. Since this hands-on project
 * does not include Spring Security, a fixed system-user placeholder is
 * returned instead — swap the body of getCurrentAuditor() for your
 * security context lookup once authentication is added.
 */
@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {

    @Bean
    public AuditorAware<String> auditorAware() {
        return this;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO: replace with SecurityContextHolder.getContext().getAuthentication().getName()
        // once Spring Security is added to the project.
        return Optional.of("system");
    }
}
