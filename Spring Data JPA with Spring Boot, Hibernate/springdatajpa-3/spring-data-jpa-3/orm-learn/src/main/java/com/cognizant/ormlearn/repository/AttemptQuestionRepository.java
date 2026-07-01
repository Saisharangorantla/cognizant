package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.AttemptQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for AttemptQuestion entity (quiz schema).
 * Hands-on 3 (springdata3.pdf).
 */
@Repository
public interface AttemptQuestionRepository extends JpaRepository<AttemptQuestion, Integer> {
}
