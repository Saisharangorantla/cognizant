package com.cognizant.orm_learn.service;

import com.cognizant.orm_learn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryServiceTest {
    private static final Logger logger= LoggerFactory.getLogger(CountryServiceTest.class);
    private static CountryService countryService;
     private static void testGetAllCountries(){
         logger.info("Start");
         List<Country> countries=countryService.getAllCountries();
         logger.debug("countries={}",countries);
         logger.info("end");
     }

}