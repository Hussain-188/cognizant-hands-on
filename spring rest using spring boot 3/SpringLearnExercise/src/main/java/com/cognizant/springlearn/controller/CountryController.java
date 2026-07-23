package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.Country;

@RestController
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    /**
     * REST API endpoint to get India country details from Spring XML configuration
     * URL: /country
     * Method: GET
     * Response: Country object converted to JSON
     *
     * @return Country object with code="IN" and name="India"
     */
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public Country getCountryIndia() {
        logger.info("getCountryIndia() method called - START");

        // Load the Spring ApplicationContext from country.xml configuration
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        logger.debug("ApplicationContext loaded from country.xml");

        // Retrieve the India bean from Spring container
        Country country = context.getBean("country", Country.class);
        logger.debug("Country bean retrieved: {}", country.toString());

        // Close the ApplicationContext to release resources
        ((ClassPathXmlApplicationContext) context).close();
        logger.debug("ApplicationContext closed");

        logger.info("getCountryIndia() method called - END");
        return country;
    }
}
