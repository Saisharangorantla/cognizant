package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Option entity (quiz schema).
 * Hands-on 3 (springdata3.pdf).
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
}
