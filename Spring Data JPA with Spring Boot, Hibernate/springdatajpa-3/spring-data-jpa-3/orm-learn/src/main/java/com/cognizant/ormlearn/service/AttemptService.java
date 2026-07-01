package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Attempt;
import com.cognizant.ormlearn.repository.AttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for Attempt operations.
 * Hands-on 3 (springdata3.pdf).
 */
@Service
public class AttemptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttemptService.class);

    @Autowired
    private AttemptRepository attemptRepository;

    /**
     * Fetches a quiz attempt with its full graph (user, questions, options,
     * and the user's selected answers) populated via fetch joins.
     */
    @Transactional
    public Attempt getAttempt(int userId, int attemptId) {
        LOGGER.info("Start getAttempt: userId={}, attemptId={}", userId, attemptId);
        Attempt attempt = attemptRepository.getAttempt(userId, attemptId);
        LOGGER.info("End getAttempt");
        return attempt;
    }
}
