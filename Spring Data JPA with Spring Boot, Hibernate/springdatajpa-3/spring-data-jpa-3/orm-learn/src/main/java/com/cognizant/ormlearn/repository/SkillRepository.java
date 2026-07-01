package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Skill entity.
 * Used in Hands-on 6.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
