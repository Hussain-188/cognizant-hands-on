package com.cognizant.hands.on;

/**
 * A simple Calculator class for demonstrating AAA pattern in tests
 */
public class Calculator {
    private double result = 0;

    /**
     * Add two numbers
     */
    public double add(double a, double b) {
        result = a + b;
        return result;
    }

    /**
     * Subtract two numbers
     */
    public double subtract(double a, double b) {
        result = a - b;
        return result;
    }

    /**
     * Multiply two numbers
     */
    public double multiply(double a, double b) {
        result = a * b;
        return result;
    }

    /**
     * Divide two numbers
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        result = a / b;
        return result;
    }

    /**
     * Get the last computed result
     */
    public double getResult() {
        return result;
    }

    /**
     * Reset the calculator
     */
    public void reset() {
        result = 0;
    }
}
