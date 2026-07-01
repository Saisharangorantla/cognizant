package com.cognizant.ems.dto;

import org.springframework.beans.factory.annotation.Value;

/**
 * Exercise 8 — Interface-based Projection.
 *
 * Spring Data JPA generates a runtime proxy implementing this interface,
 * backed only by the columns the underlying query actually selects
 * (typically just id/name/email here) — avoiding the cost of loading the
 * full Employee entity (and its lazy Department association) when only a
 * summary view is needed, e.g. for a dropdown list or table of names.
 *
 * Plain getters (getId, getName, getEmail) map directly to the matching
 * Employee properties by name — this is a "closed" projection.
 */
public interface EmployeeSummary {

    Long getId();

    String getName();

    String getEmail();

    /**
     * "Open" projection example: @Value lets you compose a derived value
     * from multiple source properties using a SpEL expression, evaluated
     * against the backing Employee entity (target).
     */
    @Value("#{target.name + ' (' + target.department.name + ')'}")
    String getNameWithDepartment();
}
