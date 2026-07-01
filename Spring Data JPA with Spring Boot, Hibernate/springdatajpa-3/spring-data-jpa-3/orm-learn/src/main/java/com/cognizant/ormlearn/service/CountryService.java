package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Country operations.
 * Hands-on 1.
 */
@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    private CountryRepository countryRepository;

    /** Returns all countries whose name contains the given text. */
    @Transactional
    public List<Country> getAllCountriesContaining(String text) {
        LOGGER.info("Start getAllCountriesContaining: text={}", text);
        List<Country> list = countryRepository.findByNameContaining(text);
        LOGGER.info("End getAllCountriesContaining: count={}", list.size());
        return list;
    }

    /** Returns countries whose name contains text, sorted A→Z. */
    @Transactional
    public List<Country> getAllCountriesContainingSorted(String text) {
        LOGGER.info("Start getAllCountriesContainingSorted: text={}", text);
        List<Country> list = countryRepository.findByNameContainingOrderByNameAsc(text);
        LOGGER.info("End getAllCountriesContainingSorted: count={}", list.size());
        return list;
    }

    /** Returns countries whose name starts with the given prefix. */
    @Transactional
    public List<Country> getAllCountriesStartingWith(String prefix) {
        LOGGER.info("Start getAllCountriesStartingWith: prefix={}", prefix);
        List<Country> list = countryRepository.findByNameStartingWith(prefix);
        LOGGER.info("End getAllCountriesStartingWith: count={}", list.size());
        return list;
    }
}
