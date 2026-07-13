package com.cognizant.hands.on;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.slf4j.Logger;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 1: SLF4J logging with JUnit 5 and Mockito")
public class Exercise1Test {

    @Mock
    private Logger logger;

    @Test
    @DisplayName("logs error and warning messages")
    void shouldLogErrorAndWarningMessages() {
        LoggingExample loggingExample = new LoggingExample(logger);

        loggingExample.logMessages();

        verify(logger).error("This is an error message");
        verify(logger).warn("This is a warning message");
        verifyNoMoreInteractions(logger);
    }
}
