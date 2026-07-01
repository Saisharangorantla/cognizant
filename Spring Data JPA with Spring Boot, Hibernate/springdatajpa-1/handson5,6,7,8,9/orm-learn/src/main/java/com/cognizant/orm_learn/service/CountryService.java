
package com.cognizant.orm_learn.service;

import com.cognizant.orm_learn.model.Country;
import com.cognizant.orm_learn.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Country> findCountryByCountryCode(String countryCode){
        return countryRepository.findByCode(countryCode);
    }
    public Country addNewCountry(Country country){
        return countryRepository.save(country);
    }
    public Country deleteCountry(String countryName){
        return countryRepository.deleteByName(countryName);
    }
    public List<Country> countriesMatchingWith(String countryPattern) {
        return countryRepository.findByNameLikeIgnoreCase(countryPattern);

    }
    public Country updateCountry(String cid,String cname){
        Optional<Country> country = countryRepository.findById(cid);
        Country c=null;
        if(country!=null){
             c = country.get();
            c.setCode(cid);
            c.setName(cname);
            c=countryRepository.save(c);
        }
        return c;
    }

}
