# PROMPTS.md

## Session 5A – AI-Generated Tests

### Part A – Baseline Coverage

Baseline coverage was measured by running:

```bash
make coverage
```

The JaCoCo report was generated at:

```text
coverage/index.html
```

### Baseline Results

For `TaxCalculator`:

* Line coverage: **48%**
* Branch coverage: **34%**

### Method-Level Branch Coverage

| Method                                   | Branch Coverage |
| ---------------------------------------- | --------------: |
| `computeIncomeTax(BigDecimal)`           |             50% |
| `isEligibleForReturn(BigDecimal, int)`   |              0% |
| `computeVAT(BigDecimal, int)`            |             42% |
| `roundToPaise(BigDecimal)`               |              0% |
| `applyExemption(BigDecimal, BigDecimal)` |             50% |

The methods with less than 50% branch coverage are:

* `isEligibleForReturn(BigDecimal, int)` – 0%
* `computeVAT(BigDecimal, int)` – 42%
* `roundToPaise(BigDecimal)` – 0%

The method selected for AI-generated test creation is:

```text
isEligibleForReturn(BigDecimal, int)
```

It is one of the methods tied for the lowest branch coverage, at 0%

## Part B – Generate Tests with Constraints

### Selected Method

The selected method is:

```java
public boolean isEligibleForReturn(BigDecimal grossIncome, int ageYears)
```

### Prompt Given to AI

Generate JUnit 5 tests for the following method.

Rules:

* Use AAA style with explicit comments.
* One assertion per test.
* Test names of the form:
  `<method>_<condition>_<expected>`.
* Cover:
  (a) null input,
  (b) empty input if applicable,
  (c) boundary values,
  (d) one representative happy path.
* Do NOT use mocks.
* Do NOT include implementation-copying assertions; each assertion must state a contract, not restate the code.

Method:

```java
public boolean isEligibleForReturn(BigDecimal grossIncome, int ageYears) {
    if (grossIncome == null || ageYears < 0) {
        return false;
    }
    BigDecimal threshold;
    if (ageYears >= 80) {
        threshold = new BigDecimal("500000");
    } else if (ageYears >= 60) {
        threshold = new BigDecimal("300000");
    } else {
        threshold = new BigDecimal("250000");
    }
    return grossIncome.compareTo(threshold) > 0;
}
```
## AI reply 

```java

@Test
    public void isEligibleForReturn_nullGrossIncome_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(null, 35);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_negativeAge_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000"), -1);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_zeroIncome_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(BigDecimal.ZERO, 35);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_underSixtyAtThreshold_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("250000"), 59);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_underSixtyAboveThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("250000.01"), 59);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_ageSixtyAtThreshold_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000"), 60);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_ageSixtyAboveThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000.01"), 60);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_ageSeventyNineAboveSeniorThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000.01"), 79);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_ageEightyAtThreshold_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("500000"), 80);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_ageEightyAboveThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("500000.01"), 80);
        // Assert
        assertTrue(eligible);
    }

```

## Part C – Assertion Review

I reviewed every assertion in the AI-generated tests to determine whether it expresses a contract or merely restates the implementation.

### Review

All generated assertions use `assertTrue()` or `assertFalse()` on the observable result of `isEligibleForReturn()`.

The tests do not reproduce the implementation logic inside the assertions. In particular, none of the assertions calculates the eligibility condition independently using the same comparisons or conditional structure as the method.

The boundary values `250000`, `300000`, and `500000` are used as test inputs to verify the observable eligibility behavior at the relevant income thresholds. The expected results are expressed directly as boolean contract outcomes.

### Result

No implementation-copying assertions were identified.

Therefore, no generated assertions were deleted or rewritten during the Part C review.

## Part D – Mutation-Guided Test Improvement

### Baseline Mutation Testing

Mutation testing was performed using:

```bash
make mutation
```

The PIT report was generated at:

```text
build/reports/pitest/index.html
```

### Baseline Mutation Results

| Metric                     | Baseline |
| -------------------------- | -------: |
| Mutations generated        |       42 |
| Mutations killed           |       25 |
| Mutations survived         |       11 |
| Mutations with no coverage |        6 |
| Mutation coverage          |      60% |
| Test strength              |      69% |

The PIT report showed 11 surviving mutants. One surviving mutant in the previously selected method `isEligibleForReturn(BigDecimal, int)` was selected for targeted test generation.

### Selected Surviving Mutant

The selected mutant was:

```text
Method: isEligibleForReturn(BigDecimal, int)
Mutation: changed conditional boundary
Original condition: ageYears < 0
```

This mutant survived because the existing test used `ageYears = -1`. Both the original condition and the boundary-mutated condition produce the same result for `-1`, so the existing test could not distinguish the original implementation from the mutant.

### Prompt Given to AI

```text
The PIT mutation report shows that this mutant survived:

- Method: isEligibleForReturn(BigDecimal, int)
- Original condition: ageYears < 0
- Mutant: conditional boundary changed
- The existing test suite already contains a test for ageYears = -1, but the mutant survives.

Write ONE new JUnit 5 test only that kills this surviving mutant.

Requirements:
- Test only the boundary between ageYears < 0 and ageYears >= 0.
- Use ageYears = 0 as the boundary value.
- Choose a grossIncome value that makes the original implementation return true.
- Use AAA style with explicit // Arrange, // Act, and // Assert comments.
- Use exactly one assertion.
- Do not modify any existing tests.
- Do not use mocks.
- Test name must follow <method>_<condition>_<expected>.
- After the test, give one sentence explaining why it kills the mutant.
```

### AI-Generated Test

```java
@Test
public void isEligibleForReturn_ageZero_true() {
    // Arrange
    TaxCalculator c = new TaxCalculator();
    BigDecimal grossIncome = new BigDecimal("250000.01");

    // Act
    boolean eligible = c.isEligibleForReturn(grossIncome, 0);

    // Assert
    assertTrue(eligible);
}
```

### Why This Test Kills the Mutant

The test uses the boundary value `ageYears = 0`.

For the original condition `ageYears < 0`, age `0` is valid, and `grossIncome = 250000.01` is above the applicable threshold of `250000`, so the method returns `true`.

For the boundary-mutated condition, age `0` is treated as invalid and the method returns `false`.

Therefore, the new test distinguishes the original implementation from the mutant and kills the mutant.

### Mutation Testing After Adding the Test

After adding the AI-generated test, mutation testing was run again using:

```bash
make mutation
```

The results improved to:

| Metric                     | Before | After |
| -------------------------- | -----: | ----: |
| Mutations generated        |     42 |    42 |
| Mutations killed           |     25 |    26 |
| Mutations survived         |     11 |    10 |
| Mutations with no coverage |      6 |     6 |
| Mutation coverage          |    60% |   62% |
| Test strength              |    69% |   72% |

The PIT report explicitly confirmed that the selected mutant was killed:

```text
isEligibleForReturn : changed conditional boundary → KILLED
Killed by: TaxCalculatorTest.isEligibleForReturn_ageZero_true()
```

Thus, the AI-generated targeted test successfully killed the selected surviving mutant.

## Part E – Reflect

### Coverage After Changes

After adding the AI-generated tests for `isEligibleForReturn`, the final JaCoCo coverage for `TaxCalculator` was:

* **Line coverage: 68%**
* **Branch coverage: 52%**

Compared with the baseline results:

* Line coverage increased from **48% to 68%**.
* Branch coverage increased from **34% to 52%**.

### Mutation Testing

The mutation score before adding the targeted test was **60%**.

After adding the targeted `isEligibleForReturn` tests, the mutation score increased to **62%**.

The additional test also helped kill the previously surviving boundary mutation in `isEligibleForReturn`, confirming that the new test was exercising an important boundary condition.

### Reflection

Mutation-testing feedback felt more useful than coverage feedback because coverage showed which code was executed, while mutation testing showed whether the tests were strong enough to detect changes in the program's behavior.
