package com.cognizant.spring_learn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.spring_learn.model.Country;
import com.cognizant.spring_learn.service.CountryService;

@RestController
@RequestMapping("/country")
public class CountryController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CountryController.class);
    @Autowired
    private CountryService countryService;
    @GetMapping
    public Country getCountryIndia() {
        LOGGER.info("START");
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");
        Country country =
                (Country) context.getBean("country",Country.class);
        LOGGER.info("END");
        return country;
    }


    @GetMapping("/{code}")
    public Country getCountry(@PathVariable String code) {

        LOGGER.info("START");

        Country country = countryService.getCountry(code);

        LOGGER.info("END");

        return country;
    }
}