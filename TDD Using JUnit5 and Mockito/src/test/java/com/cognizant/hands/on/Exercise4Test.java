package com.cognizant.hands.on;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Exercise 4: Arrange-Act-Assert (AAA) Pattern, Test Fixtures, Setup and Teardown Methods
 * 
 * This test class demonstrates:
 * 1. The AAA (Arrange-Act-Assert) pattern for organizing test code
 * 2. @BeforeEach for setup methods (executed before each test)
 * 3. @AfterEach for teardown methods (executed after each test)
 * 4. Test fixtures (shared test data)
 * 
 * AAA Pattern Explanation:
 * - ARRANGE: Set up test data and dependencies
 * - ACT: Execute the code being tested
 * - ASSERT: Verify the results meet expectations
 */
@DisplayName("Exercise 4: AAA Pattern with Setup/Teardown")
public class Exercise4Test {
    
    // ===== TEST FIXTURES =====
    // These are shared test data/resources used across multiple tests
    private Calculator calculator;
    private static final double EPSILON = 0.0001; // For floating point comparisons
    
    /**
     * @BeforeEach: Executes before EACH test method
     * This is where we set up common test fixtures and initialize resources
     * Equivalent to @Before in JUnit4
     */
    @BeforeEach
    public void setUp() {
        System.out.println("🔧 Setting up test fixtures...");
        // ARRANGE: Initialize test fixture
        calculator = new Calculator();
        System.out.println("✅ Calculator instance created");
    }
    
    /**
     * @AfterEach: Executes after EACH test method
     * This is where we clean up resources and verify teardown
     * Equivalent to @After in JUnit4
     */
    @AfterEach
    public void tearDown() {
        System.out.println("🧹 Tearing down test fixtures...");
        // Clean up resources
        calculator.reset();
        calculator = null;
        System.out.println("✅ Calculator cleaned up");
    }
    
    // ===== AAA PATTERN TESTS =====
    
    /**
     * Test 1: Addition with AAA Pattern
     * Demonstrates basic AAA structure with clear separation of concerns
     */
    @Test
    @DisplayName("Test 1: Add two positive numbers using AAA pattern")
    public void testAddPositiveNumbers() {
        // ARRANGE: Set up test data
        double num1 = 10.0;
        double num2 = 5.0;
        double expectedResult = 15.0;
        
        // ACT: Execute the operation being tested
        double actualResult = calculator.add(num1, num2);
        
        // ASSERT: Verify the result meets expectations
        assertEquals(expectedResult, actualResult, EPSILON,
                "10.0 + 5.0 should equal 15.0");
    }
    
    /**
     * Test 2: Subtraction with verification of current state
     * Shows how to verify multiple aspects of the result
     */
    @Test
    @DisplayName("Test 2: Subtract numbers and verify state")
    public void testSubtractNumbers() {
        // ARRANGE
        double minuend = 20.0;
        double subtrahend = 8.0;
        double expectedResult = 12.0;
        
        // ACT
        double actualResult = calculator.subtract(minuend, subtrahend);
        
        // ASSERT: Verify both the return value and the internal state
        assertEquals(expectedResult, actualResult, EPSILON,
                "20.0 - 8.0 should equal 12.0");
        assertEquals(expectedResult, calculator.getResult(), EPSILON,
                "Calculator's result field should store the computed value");
    }
    
    /**
     * Test 3: Multiplication with edge case (zero)
     * Demonstrates AAA with edge case testing
     */
    @Test
    @DisplayName("Test 3: Multiply with zero")
    public void testMultiplyByZero() {
        // ARRANGE
        double num1 = 50.0;
        double num2 = 0.0;
        double expectedResult = 0.0;
        
        // ACT
        double actualResult = calculator.multiply(num1, num2);
        
        // ASSERT
        assertEquals(expectedResult, actualResult, EPSILON,
                "Any number multiplied by zero should be zero");
    }
    
    /**
     * Test 4: Division with normal values
     * Demonstrates AAA pattern with method calls
     */
    @Test
    @DisplayName("Test 4: Divide two numbers")
    public void testDivideNumbers() {
        // ARRANGE: Set up test data
        double dividend = 20.0;
        double divisor = 4.0;
        double expectedResult = 5.0;
        
        // ACT: Call the method under test
        double actualResult = calculator.divide(dividend, divisor);
        
        // ASSERT: Verify the result
        assertEquals(expectedResult, actualResult, EPSILON,
                "20.0 / 4.0 should equal 5.0");
    }
    
    /**
     * Test 5: Division by zero (exception testing)
     * Demonstrates AAA pattern for exception testing
     */
    @Test
    @DisplayName("Test 5: Division by zero throws exception")
    public void testDivisionByZeroThrowsException() {
        // ARRANGE
        double dividend = 10.0;
        double divisor = 0.0;
        
        // ACT & ASSERT: Verify that an exception is thrown
        // Using assertThrows (JUnit5 feature)
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.divide(dividend, divisor),
                "Division by zero should throw IllegalArgumentException"
        );
        
        // Additional assertion on the exception message
        assertTrue(exception.getMessage().contains("Division by zero"),
                "Exception message should mention division by zero");
    }
    
    /**
     * Test 6: Sequential operations (chaining state)
     * Demonstrates AAA pattern with multiple operations
     */
    @Test
    @DisplayName("Test 6: Sequential operations")
    public void testSequentialOperations() {
        // ARRANGE
        double initialValue = 100.0;
        double subtractValue = 20.0;
        double multiplyValue = 2.0;
        double expectedResult = 160.0; // (100 - 20) * 2
        
        // ACT: Perform multiple operations
        calculator.subtract(initialValue, subtractValue);
        double result1 = calculator.getResult();
        
        calculator.multiply(result1, multiplyValue);
        double finalResult = calculator.getResult();
        
        // ASSERT: Verify the final result
        assertEquals(expectedResult, finalResult, EPSILON,
                "Sequential operations: (100 - 20) * 2 should equal 160");
    }
    
    /**
     * Test 7: Testing the reset functionality
     * Demonstrates AAA with state verification
     */
    @Test
    @DisplayName("Test 7: Reset clears the calculator state")
    public void testResetClearsState() {
        // ARRANGE
        calculator.add(10.0, 5.0);
        assertNotEquals(0, calculator.getResult(), "Calculator should have a value before reset");
        
        // ACT
        calculator.reset();
        
        // ASSERT
        assertEquals(0, calculator.getResult(), EPSILON,
                "reset() should clear the calculator result to 0");
    }
    
    /**
     * Test 8: Negative numbers
     * Demonstrates AAA pattern with negative value testing
     */
    @Test
    @DisplayName("Test 8: Operations with negative numbers")
    public void testNegativeNumbers() {
        // ARRANGE
        double negativeNum1 = -10.0;
        double negativeNum2 = -5.0;
        
        // ACT
        double addResult = calculator.add(negativeNum1, negativeNum2);
        
        // ASSERT
        assertEquals(-15.0, addResult, EPSILON,
                "Sum of two negative numbers should be negative");
    }
    
    /**
     * Test 9: Floating point precision
     * Demonstrates AAA with EPSILON for floating point comparisons
     */
    @Test
    @DisplayName("Test 9: Verify floating point precision using EPSILON")
    public void testFloatingPointPrecision() {
        // ARRANGE
        double num1 = 0.1;
        double num2 = 0.2;
        double expectedResult = 0.3; // Note: 0.1 + 0.2 in binary floating point is ~0.3000000004
        
        // ACT
        double result = calculator.add(num1, num2);
        
        // ASSERT: Using EPSILON instead of exact equality
        assertEquals(expectedResult, result, EPSILON,
                "0.1 + 0.2 should equal 0.3 (within EPSILON tolerance)");
    }
    
    /**
     * Test 10: Assertion methods demonstration
     * Demonstrates various assertion methods used with AAA pattern
     */
    @Test
    @DisplayName("Test 10: Various assertion methods")
    public void testAssertionMethods() {
        // ARRANGE
        calculator.add(10.0, 5.0);
        
        // ACT & ASSERT: Multiple assertion methods
        assertEquals(15.0, calculator.getResult(), EPSILON);
        assertNotNull(calculator, "Calculator should not be null");
        assertTrue(calculator.getResult() > 0, "Result should be positive");
        assertFalse(calculator.getResult() < 0, "Result should not be negative");
    }
}
