# Exercise 4: Arrange-Act-Assert (AAA) Pattern with Setup/Teardown Methods

## Overview

This exercise demonstrates how to organize tests using the **Arrange-Act-Assert (AAA) pattern** and how to use **setup (@BeforeEach) and teardown (@AfterEach)** methods in JUnit5.

## What You'll Learn

### 1. **Arrange-Act-Assert (AAA) Pattern**

The AAA pattern is a best practice for structuring test code with three distinct phases:

```
┌─────────────────────────────────────────┐
│  ARRANGE: Set up test data and state    │
├─────────────────────────────────────────┤
│  ACT: Execute the code being tested     │
├─────────────────────────────────────────┤
│  ASSERT: Verify the results             │
└─────────────────────────────────────────┘
```

**Benefits:**

- **Clarity**: Clear separation between setup, execution, and verification
- **Readability**: Easy to understand what each test does
- **Maintainability**: Easier to modify and debug tests
- **Reusability**: Shared setup code reduces duplication

### 2. **Test Fixtures**

Test fixtures are **shared test data and resources** used across multiple tests.

**In Exercise4Test.java:**

```java
private Calculator calculator;                    // Shared fixture
private static final double EPSILON = 0.0001;    // Shared constant
```

### 3. **@BeforeEach (Setup Method)**

Executes **before EACH test method** in JUnit5.

```java
@BeforeEach
public void setUp() {
    calculator = new Calculator();  // Initialize shared resource
}
```

**Key Points:**

- Replaced @Before (JUnit4) in JUnit5
- Use for initializing test dependencies
- Runs before every test method

### 4. **@AfterEach (Teardown Method)**

Executes **after EACH test method** in JUnit5.

```java
@AfterEach
public void tearDown() {
    calculator.reset();
    calculator = null;  // Clean up resources
}
```

**Key Points:**

- Replaced @After (JUnit4) in JUnit5
- Use for cleaning up resources
- Ensures no state leaks between tests

### 5. **Other Setup/Teardown Annotations (Reference)**

| Annotation            | JUnit4       | JUnit5      | Frequency                      |
| --------------------- | ------------ | ----------- | ------------------------------ |
| Class-level setup     | @BeforeClass | @BeforeAll  | Once before ALL tests in class |
| Class-level teardown  | @AfterClass  | @AfterAll   | Once after ALL tests in class  |
| Method-level setup    | @Before      | @BeforeEach | Before EACH test method        |
| Method-level teardown | @After       | @AfterEach  | After EACH test method         |

## Code Structure

### Calculator.java

A simple domain class with basic arithmetic operations:

- `add(double a, double b)` - Addition
- `subtract(double a, double b)` - Subtraction
- `multiply(double a, double b)` - Multiplication
- `divide(double a, double b)` - Division (throws exception on division by zero)
- `getResult()` - Returns last computed result
- `reset()` - Clears the calculator

### Exercise4Test.java

The test class demonstrates AAA pattern across **10 different test scenarios:**

| Test # | Name                              | Focus                                  |
| ------ | --------------------------------- | -------------------------------------- |
| 1      | testAddPositiveNumbers            | Basic addition, simple AAA structure   |
| 2      | testSubtractNumbers               | State verification                     |
| 3      | testMultiplyByZero                | Edge case testing                      |
| 4      | testDivideNumbers                 | Normal division operation              |
| 5      | testDivisionByZeroThrowsException | Exception testing                      |
| 6      | testSequentialOperations          | Multiple operations/chaining state     |
| 7      | testResetClearsState              | State reset verification               |
| 8      | testNegativeNumbers               | Negative value handling                |
| 9      | testFloatingPointPrecision        | Floating-point comparison with EPSILON |
| 10     | testAssertionMethods              | Various assertion methods              |

## Key Concepts

### AAA Pattern Example

```java
@Test
public void testAddPositiveNumbers() {
    // ARRANGE: Set up test data
    double num1 = 10.0;
    double num2 = 5.0;
    double expectedResult = 15.0;

    // ACT: Execute the operation
    double actualResult = calculator.add(num1, num2);

    // ASSERT: Verify the result
    assertEquals(expectedResult, actualResult, EPSILON);
}
```

### Floating Point Comparison

When comparing floating-point numbers, use **EPSILON** to account for precision issues:

```java
private static final double EPSILON = 0.0001;
assertEquals(expected, actual, EPSILON);  // Allows tiny differences
```

### Exception Testing (JUnit5)

Use `assertThrows` for testing exceptions:

```java
IllegalArgumentException exception = assertThrows(
    IllegalArgumentException.class,
    () -> calculator.divide(10.0, 0.0)
);
assertTrue(exception.getMessage().contains("zero"));
```

### Setup/Teardown Execution Flow

```
Test Method 1:
  ├─ @BeforeEach (setUp)
  ├─ @Test (testAddPositiveNumbers)
  ├─ @AfterEach (tearDown)

Test Method 2:
  ├─ @BeforeEach (setUp)
  ├─ @Test (testSubtractNumbers)
  ├─ @AfterEach (tearDown)

... and so on for each test method
```

## JUnit5 vs JUnit4 Annotations

| Feature               | JUnit4       | JUnit5                |
| --------------------- | ------------ | --------------------- |
| Test Method           | @Test        | @Test                 |
| Setup (per method)    | @Before      | @BeforeEach           |
| Teardown (per method) | @After       | @AfterEach            |
| Setup (per class)     | @BeforeClass | @BeforeAll            |
| Teardown (per class)  | @AfterClass  | @AfterAll             |
| DisplayName           | N/A          | @DisplayName          |
| Import Package        | org.junit    | org.junit.jupiter.api |

## Best Practices

✅ **DO:**

- Use clear, descriptive test names
- Follow the AAA pattern consistently
- Keep setup/teardown methods focused
- Use @BeforeEach for common initialization
- Use @AfterEach for resource cleanup
- Write one assertion concept per test (or use related assertions)
- Use EPSILON for floating-point comparisons

❌ **DON'T:**

- Mix concerns in test methods
- Reuse test data inappropriately
- Leave resources without cleanup
- Have interdependent tests
- Use @BeforeAll/@AfterAll unnecessarily
- Ignore floating-point precision issues

## Running the Tests

```bash
# Using Maven
mvn test

# Run specific test class
mvn test -Dtest=Exercise4Test

# Run with verbose output
mvn test -X
```

## Expected Output

When you run Exercise4Test, you should see:

- 10 test methods execute
- For each test: setUp() runs, test executes, tearDown() runs
- Console output showing fixture setup and teardown phases
- All assertions pass (green tests)

## Next Steps

1. Run Exercise4Test to see AAA pattern in action
2. Notice how @BeforeEach and @AfterEach execute for each test
3. Modify tests to understand AAA pattern better
4. Create your own domain class and test it using AAA pattern

---

**Author:** Cognizant Hands-On Exercise  
**Topic:** Test-Driven Development with JUnit5 and Mockito  
**Difficulty Level:** Beginner to Intermediate
