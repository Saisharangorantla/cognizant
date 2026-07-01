package com.cognizant.orm_learn;

import com.cognizant.orm_learn.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OrmLearnApplication {
	private static final Logger logger= LoggerFactory.getLogger(OrmLearnApplication.class);
	public static void main(String[] args) {

		org.springframework.context.ApplicationContext context =SpringApplication.run(OrmLearnApplication.class, args);


		logger.info("Inside Main");
	}

}
