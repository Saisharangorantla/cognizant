package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for Attempt entity (quiz schema).
 * Hands-on 3 (springdata3.pdf).
 *
 * getAttempt(userId, attemptId):
 *   Joins, in order: user → attempt → attempt_question → question
 *                     → attempt_option → options
 *   'fetch' is applied on every one-to-many / many-to-many hop so the
 *   full object graph (questions, their attempt-options, and the
 *   underlying option master data) is populated in a SINGLE query —
 *   exactly as called out in the "IMPORTANT TAKEAWAY" note in the PDF:
 *   join links the tables, fetch populates the beans.
 */
@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    @Query(value = "SELECT a FROM Attempt a "
            + "left join fetch a.user u "
            + "left join fetch a.attemptQuestionList aq "
            + "left join fetch aq.question q "
            + "left join fetch aq.attemptOptionList ao "
            + "left join fetch ao.option o "
            + "WHERE u.id = :userId AND a.id = :attemptId")
    Attempt getAttempt(@Param("userId") int userId, @Param("attemptId") int attemptId);
}
