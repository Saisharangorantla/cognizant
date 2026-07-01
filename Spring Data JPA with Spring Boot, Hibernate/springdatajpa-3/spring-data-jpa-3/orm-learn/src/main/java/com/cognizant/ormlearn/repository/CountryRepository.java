package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Country entity.
 *
 * Hands-on 1 - Query Methods:
 *  1. findByNameContaining          → search countries whose name contains given text
 *  2. findByNameContainingOrderByNameAsc → same but sorted A→Z
 *  3. findByNameStartingWith        → countries starting with a given letter/prefix
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    /**
     * HO-1 Q1: Returns all countries whose name contains the given text (case-sensitive).
     * Spring Data derives SQL:  WHERE co_name LIKE '%text%'
     *
     * Example: findByNameContaining("ou") → Bouvet Island, Djibouti, Guadeloupe …
     */
    List<Country> findByNameContaining(String text);

    /**
     * HO-1 Q2: Same as above but results are sorted alphabetically (ascending).
     * Spring Data derives SQL:  WHERE co_name LIKE '%text%' ORDER BY co_name ASC
     *
     * Example: findByNameContainingOrderByNameAsc("ou") → Bouvet Island, Djibouti …
     */
    List<Country> findByNameContainingOrderByNameAsc(String text);

    /**
     * HO-1 Q3: Returns all countries whose name starts with the given prefix.
     * Spring Data derives SQL:  WHERE co_name LIKE 'prefix%'
     *
     * Example: findByNameStartingWith("Z") → Zambia, Zimbabwe
     */
    List<Country> findByNameStartingWith(String prefix);
}
