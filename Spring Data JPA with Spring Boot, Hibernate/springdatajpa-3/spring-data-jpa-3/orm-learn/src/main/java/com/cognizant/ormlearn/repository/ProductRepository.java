package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Product entity (basic CRUD).
 * Hands-on 6 (springdata3.pdf — Criteria Query).
 * Dynamic search lives in ProductSearchRepository, since Criteria Query
 * needs direct access to EntityManager rather than a derived query method.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
