package com.cognizant.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Employee Management System — main entry point.
 *
 * Exercise 7 (Entity Auditing): @EnableJpaAuditing turns on Spring Data
 * JPA's auditing infrastructure so @CreatedDate / @LastModifiedDate /
 * @CreatedBy / @LastModifiedBy annotations in BaseAuditEntity are
 * populated automatically on every save.
 *
 * auditorAwareRef = "auditorAware" tells Spring which bean supplies the
 * "current user" for @CreatedBy/@LastModifiedBy (see config/AuditorAwareImpl.java).
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class EmployeeManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);
    }
}
