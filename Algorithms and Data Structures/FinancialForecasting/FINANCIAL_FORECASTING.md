# Exercise 7: Financial Forecasting

## Scenario

A financial forecasting tool predicts future values using past growth rates. Recursion is used to express the repeated growth calculation in a compact way.

---

## 1. Understanding Recursive Algorithms

Recursion is a technique where a method calls itself to solve a smaller version of the same problem.

A recursive solution is useful when:

- The problem can be broken into smaller subproblems.
- The current step depends on the result of the previous step.
- The logic is repetitive and follows a predictable pattern.

For financial forecasting, each future period depends on the previous projected value, which makes recursion a natural fit.

---

## 2. Setup

The forecasting method takes:

- A current value
- An array of historical growth rates
- The number of future periods to project

The recursive method repeatedly applies the growth rate pattern until the required number of periods has been reached.

---

## 3. Implementation

The recursive algorithm works as follows:

- Base case: if `periodsAhead` is `0`, return a multiplier of `1.0`.
- Recursive case: calculate the multiplier for one fewer period and apply the next growth rate.
- The final forecast is the current value multiplied by the recursive growth factor.

This is implemented in [src/ForecastingAlgorithms.java](src/ForecastingAlgorithms.java).

Example growth pattern:

- 5%
- 4%
- 6%

These growth rates repeat across future periods in the forecast.

---

## 4. Time Complexity

For a forecast `n` periods ahead:

- Time complexity: `O(n)`
- Space complexity: `O(n)` because of recursive call stack usage

Each period is processed once, so the runtime grows linearly with the number of periods forecast.

---

## 5. Optimization

To avoid excessive computation and reduce overhead:

- Use memoization when the same forecasted sub-results are requested multiple times.
- Prefer an iterative loop for production use if recursion is not required, because it removes recursive call stack overhead.
- Validate inputs early to avoid unnecessary work on invalid data.

In this exercise, memoization is used for the recursive growth multiplier so repeated subproblems are not recomputed.

---

## 6. Output

Running [src/ForecastingTest.java](src/ForecastingTest.java) produces a forecast similar to:

```text
Financial Forecasting Result:
Current Value: 1000.0
Growth Rates: 5.0%, 4.0%, 6.0%
Periods Ahead: 5
Forecasted Future Value: 1264.01
```
