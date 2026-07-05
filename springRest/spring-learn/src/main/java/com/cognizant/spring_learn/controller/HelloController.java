package com.cognizant.spring_learn.controller;

import org.slf4j.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	private static final Logger logger=LoggerFactory.getLogger(HelloController.class);
	
	@GetMapping("/hello")
	public String sayHello()
	{
		logger.info("start");
		String message="hello,World";
		logger.info("End");
		return message;
	}
	
}
