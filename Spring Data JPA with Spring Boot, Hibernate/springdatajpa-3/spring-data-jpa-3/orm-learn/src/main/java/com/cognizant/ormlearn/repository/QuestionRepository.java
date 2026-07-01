package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Question entity (quiz schema).
 * Hands-on 3 (springdata3.pdf).
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
