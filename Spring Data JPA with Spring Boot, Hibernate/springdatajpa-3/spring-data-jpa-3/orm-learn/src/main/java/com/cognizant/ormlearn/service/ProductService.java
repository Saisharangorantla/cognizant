package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Product;
import com.cognizant.ormlearn.model.ProductSearchCriteria;
import com.cognizant.ormlearn.repository.ProductSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Product / Criteria Query search.
 * Hands-on 6 (springdata3.pdf).
 */
@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductSearchRepository productSearchRepository;

    /**
     * Executes a dynamic product search using whatever filters are set on
     * the given criteria object (any combination, including none).
     */
    @Transactional
    public List<Product> search(ProductSearchCriteria criteria) {
        LOGGER.info("Start search");
        List<Product> results = productSearchRepository.search(criteria);
        LOGGER.info("End search: count={}", results.size());
        return results;
    }
}
