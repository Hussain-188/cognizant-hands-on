# Exercise 2: E-commerce Platform Search Function

## Scenario
An e-commerce platform requires an efficient search mechanism to quickly locate products. Two search algorithms, Linear Search and Binary Search, are implemented and analyzed to determine the most suitable approach.

---

## 1. Understanding Asymptotic Notation

### Big O Notation
Big O notation describes the growth rate of an algorithm's running time with respect to the input size. It helps in evaluating and comparing the efficiency of algorithms.

Common complexities include:

- **O(1)** – Constant Time
- **O(log n)** – Logarithmic Time
- **O(n)** – Linear Time
- **O(n log n)** – Linearithmic Time
- **O(n²)** – Quadratic Time

Big O notation enables developers to predict algorithm performance as data size increases.

---

## 2. Search Operation Cases

### Linear Search

- **Best Case:** O(1)
  - Element found at the first position.

- **Average Case:** O(n)
  - Element found somewhere in the middle.

- **Worst Case:** O(n)
  - Element found at the last position or not present.

### Binary Search

- **Best Case:** O(1)
  - Element found at the middle position.

- **Average Case:** O(log n)

- **Worst Case:** O(log n)

Binary Search requires the array to be sorted.

---

## 3. Implementation

### Product Class

Each product contains:

- Product ID
- Product Name
- Category

### Search Algorithms Implemented

#### Linear Search
- Searches elements sequentially.
- Suitable for small or unsorted datasets.

#### Binary Search
- Repeatedly divides the search space into halves.
- Requires a sorted array.
- Provides significantly faster searching for large datasets.

---

## 4. Time Complexity Comparison

| Algorithm | Best Case | Average Case | Worst Case |
|------------|-----------|--------------|------------|
| Linear Search | O(1) | O(n) | O(n) |
| Binary Search | O(1) | O(log n) | O(log n) |

---

## 5. Analysis

### Linear Search

**Advantages**
- Simple to implement.
- Works on unsorted data.

**Disadvantages**
- Slow for large datasets.
- Time complexity increases linearly with input size.

### Binary Search

**Advantages**
- Highly efficient.
- Faster searching for large datasets.
- Logarithmic time complexity.

**Disadvantages**
- Requires sorted data.

---

## 6. Conclusion

For an e-commerce platform containing a large number of products, **Binary Search** is the preferred algorithm because:

- It offers faster search performance.
- Its time complexity is **O(log n)**, which is much more efficient than **O(n)**.
- It scales better as the number of products grows.

Therefore, Binary Search is more suitable for optimizing search functionality in an e-commerce platform.

---

## Output

```
Linear Search Result:
Product ID: 104, Product Name: Watch, Category: Accessories

Binary Search Result:
Product ID: 104, Product Name: Watch, Category: Accessories
```
