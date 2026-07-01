package com.cognizant.ems.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Exercise 9 — Customizing Data Source Configuration.
 *
 * Demonstrates USING the secondary "reporting" DataSource defined in
 * DataSourceConfig, via a plain JdbcTemplate rather than a second full
 * JPA EntityManagerFactory.
 *
 * NOTE on scope: wiring a second complete JPA persistence unit (separate
 * EntityManagerFactory + TransactionManager + @EnableJpaRepositories
 * basePackages split) is the "proper" multi-database JPA setup, but it's
 * a lot of extra configuration for a hands-on whose goal is to show data
 * source customization and externalized configuration. JdbcTemplate over
 * the qualified "reportingDataSource" bean demonstrates the same core
 * skill (more than one DataSource, explicitly chosen by qualifier) with
 * far less boilerplate. If your use case needs full JPA entities against
 * the second database, mirror DataSourceConfig with an additional
 * LocalContainerEntityManagerFactoryBean + JpaTransactionManager pair,
 * each pointed at "reportingDataSource".
 */
@Service
public class ReportingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingService.class);

    private final JdbcTemplate reportingJdbcTemplate;

    public ReportingService(@Qualifier("reportingDataSource") DataSource reportingDataSource) {
        this.reportingJdbcTemplate = new JdbcTemplate(reportingDataSource);
    }

    /**
     * Demonstrates a query running against the SECOND datasource,
     * independent of the primary employee/department schema.
     */
    public Integer getReportingDatabaseEcho() {
        LOGGER.info("Querying secondary 'reporting' datasource");
        Integer result = reportingJdbcTemplate.queryForObject("SELECT 1", Integer.class);
        LOGGER.info("Reporting datasource responded: {}", result);
        return result;
    }
}
