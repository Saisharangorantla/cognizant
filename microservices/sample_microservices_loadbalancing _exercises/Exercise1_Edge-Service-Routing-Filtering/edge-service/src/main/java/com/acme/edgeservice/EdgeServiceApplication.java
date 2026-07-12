package com.acme.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the edge service.
 * <p>
 * This application sits in front of our downstream services and is
 * responsible for routing incoming requests to the right place and for
 * applying cross-cutting concerns (logging, headers, etc.) via Spring
 * Cloud Gateway filters.
 */
@SpringBootApplication
public class EdgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }
}
