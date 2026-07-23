package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exercise 1: Hello World RESTful Web Service Controller
 * 
 * This controller handles HTTP GET requests to the /hello endpoint.
 * 
 * Method: GET
 * URL: /hello
 * Response: "Hello World!!"
 */
@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    /**
     * GET endpoint that returns "Hello World!!"
     * 
     * URL: http://localhost:8083/hello
     * Method: GET
     * 
     * @return String - "Hello World!!"
     */
    @GetMapping("/hello")
    public String sayHello() {
        logger.info("sayHello() method called - START");
        String response = "Hello World!!";
        logger.info("sayHello() method called - END");
        return response;
    }
}
