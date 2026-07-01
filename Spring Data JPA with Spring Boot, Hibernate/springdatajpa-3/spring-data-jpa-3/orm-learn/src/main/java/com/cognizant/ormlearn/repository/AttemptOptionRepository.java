package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.AttemptOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for AttemptOption entity (quiz schema).
 * Hands-on 3 (springdata3.pdf).
 */
@Repository
public interface AttemptOptionRepository extends JpaRepository<AttemptOption, Integer> {
}
