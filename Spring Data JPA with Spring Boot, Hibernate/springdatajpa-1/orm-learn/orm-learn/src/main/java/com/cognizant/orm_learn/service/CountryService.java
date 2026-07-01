package com.cognizant.orm_learn.service;

import com.cognizant.orm_learn.model.Country;
import com.cognizant.orm_learn.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    @Autowired
    public CountryService(CountryRepository countryRepository){
        this.countryRepository=countryRepository;
    }

    public List<Country> getAllCountries(){
        return countryRepository.findAll();
    }
}
