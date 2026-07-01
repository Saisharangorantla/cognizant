package com.cognizant.orm_learn.repository;

import com.cognizant.orm_learn.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country,String> {
      public List<Country> findByCode(String code);
      public List<Country> findByNameLikeIgnoreCase(String name);
      public Country deleteByName(String Country);
}
