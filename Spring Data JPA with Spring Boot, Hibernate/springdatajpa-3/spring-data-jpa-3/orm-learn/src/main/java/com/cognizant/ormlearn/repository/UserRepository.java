package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User entity (quiz schema).
 * Hands-on 3 (springdata3.pdf).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
