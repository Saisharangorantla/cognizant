package com.cognizant.ems.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Exercise 9 — Customizing Data Source Configuration.
 *
 * Demonstrates managing MULTIPLE data sources in one application:
 *   1. The PRIMARY datasource (configured via the standard
 *      spring.datasource.* properties, auto-configured by Spring Boot —
 *      no bean needed here, Spring Boot wires it for us automatically).
 *   2. A SECONDARY "reporting" datasource (configured via the externalized
 *      reporting.datasource.* properties in application.properties),
 *      explicitly defined as a bean below since Spring Boot only
 *      auto-configures ONE DataSource by default.
 *
 * @Primary on the primary bean tells Spring which DataSource to inject
 * wherever a plain `DataSource` or the default JPA EntityManagerFactory
 * is required, resolving ambiguity now that two DataSource beans exist.
 *
 * NOTE: this class declares the primary datasource bean EXPLICITLY (rather
 * than relying purely on Spring Boot's implicit auto-configuration) so
 * that both datasources are visible side-by-side and the @Primary
 * distinction is explicit. If you only need the implicit
 * single-datasource behavior, you can delete primaryDataSource() and
 * keep relying on spring.datasource.* auto-configuration alone.
 */
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "reportingDataSource")
    @ConfigurationProperties(prefix = "reporting.datasource")
    public DataSource reportingDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
