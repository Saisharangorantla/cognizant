package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Product;
import com.cognizant.ormlearn.model.ProductSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Hands-on 6 (springdata3.pdf) — Criteria Query.
 *
 * Demonstrates the "Amazon laptop search" scenario from the PDF: the user
 * searches by keyword, then optionally narrows results using ANY
 * COMBINATION of filters (review, hard disk, RAM, CPU speed, OS, weight,
 * CPU). Because the active filter set varies per search, a single fixed
 * HQL/JPQL string cannot express the WHERE clause — the predicate list
 * has to be built at RUNTIME. Criteria Query solves exactly this problem.
 *
 * Core building blocks (per the PDF's "Reference" link):
 *   - CriteriaBuilder : factory for predicates (equal, ge, le, like, and, ...)
 *   - CriteriaQuery    : the query definition itself (select, where)
 *   - Root             : represents the FROM clause (the Product entity)
 *   - TypedQuery        : the final, executable, type-safe query
 *
 * This is implemented as a plain @Repository class (not a Spring Data JPA
 * interface) because Criteria Query needs direct access to EntityManager.
 */
@Repository
public class ProductSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Builds and executes a dynamic Product search query.
     * Every predicate below is added ONLY if the corresponding criteria
     * field is non-null — exactly mirroring "the user might select
     * options available in one or more of the criteria categories".
     */
    public List<Product> search(ProductSearchCriteria criteria) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        // Free-text keyword match against product name (e.g. "laptop")
        if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(product.get("name")),
                    "%" + criteria.getKeyword().toLowerCase() + "%"));
        }

        // Customer review >= minReview
        if (criteria.getMinReview() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    product.get("review"), criteria.getMinReview()));
        }

        // Hard Disk Size >= minHardDiskGb
        if (criteria.getMinHardDiskGb() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    product.get("hardDiskGb"), criteria.getMinHardDiskGb()));
        }

        // RAM Size >= minRamGb
        if (criteria.getMinRamGb() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    product.get("ramGb"), criteria.getMinRamGb()));
        }

        // CPU Speed >= minCpuSpeedGhz
        if (criteria.getMinCpuSpeedGhz() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    product.get("cpuSpeedGhz"), criteria.getMinCpuSpeedGhz()));
        }

        // Operating System = exact match
        if (criteria.getOperatingSystem() != null && !criteria.getOperatingSystem().isBlank()) {
            predicates.add(cb.equal(
                    product.get("operatingSystem"), criteria.getOperatingSystem()));
        }

        // Weight <= maxWeightKg
        if (criteria.getMaxWeightKg() != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    product.get("weightKg"), criteria.getMaxWeightKg()));
        }

        // CPU = exact match (e.g. "Intel i7")
        if (criteria.getCpu() != null && !criteria.getCpu().isBlank()) {
            predicates.add(cb.equal(product.get("cpu"), criteria.getCpu()));
        }

        // Combine every active predicate with AND.
        // If no criteria were selected at all, predicates is empty and
        // every Product row is returned (mirrors "fresh search, no filters").
        query.select(product).where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
