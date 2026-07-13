package com.cognizant.hands.on;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {
    private final Logger logger;

    public LoggingExample() {
        this(LoggerFactory.getLogger(LoggingExample.class));
    }

    LoggingExample(Logger logger) {
        this.logger = logger;
    }

    public void logMessages() {
        logger.error("This is an error message");
        logger.warn("This is a warning message");
    }

    public static void main(String[] args) {
        new LoggingExample().logMessages();
    }
}