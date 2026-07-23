package com.cognizant.springlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Application for JWT Authentication Service Exercise
 * 
 * This application demonstrates JWT (JSON Web Token) based authentication
 * in a Spring Boot 3 REST API.
 * 
 * Three major steps covered:
 * 1. Create Authentication Controller and configure SecurityConfig
 * 2. Read Authorization header and decode username/password
 * 3. Generate JWT token based on validated user
 * 
 * To test, use:
 * curl -s -u user:password http://localhost:8090/authenticate
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.cognizant.springlearn"})
public class SpringLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);
    }
}
